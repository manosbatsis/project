package com.topideal.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.rocketmq.common.namesrv.TopAddressing;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import org.apache.commons.collections.map.Flat3Map;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP_LOG;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.CodeGeneratorUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.ConsumeMonitorInventoryDao;
import com.topideal.entity.vo.ImportErrorMessage;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.service.DataOperationsService;
import com.topideal.tools.CollectionEnum;

/**
 * 数据处理
 * @author 杨创
 *
 */
@Service
public class DataOperationsServiceImpl implements DataOperationsService {
	
	@Autowired
	private RMQProducer rmqProducer;
	@Autowired
	private ConsumeMonitorInventoryDao consumeMonitorInventoryDao;
	@Autowired
	private JSONMongoDao jsonMongoDao;
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(DataOperationsServiceImpl.class);
	
	@Override
	public Map<String, Object> getRollbackList(String orderCodes) throws Exception {
		List<String> orderCodeList=new ArrayList<>();
		List<String> codeList=null;
		if (StringUtils.isNotBlank(orderCodes)) {
			codeList = Arrays.asList(orderCodes.split(","));
			for (String code : codeList) {
				orderCodeList.add("'"+code+"'");
			}
		}
		
		Map<String, Object>map=new HashMap<>();
		map.put("orderCodeList", codeList);
		// 失败监控
		List<Map<String, Object>> rollbackList = consumeMonitorInventoryDao.getRollbackList(map);
		//获取库存监控回滚成功失败数据量
		List<Map<String, Object>> inventoryRollbackCount = consumeMonitorInventoryDao.getInventoryRollbackCount(map);
		int inventorySuccessNum=0;
		int inventoryFailuerNum=0;
		for (Map<String, Object> inventoryMap : inventoryRollbackCount) {
			String status = (String) inventoryMap.get("status");
			Long inventoryTotalCount =  (Long) inventoryMap.get("totalCount");
			if (DERP_LOG.LOG_STATUS_1.equals(status))inventorySuccessNum=Integer.valueOf(inventoryTotalCount.toString());
			if (DERP_LOG.LOG_STATUS_0.equals(status))inventoryFailuerNum=Integer.valueOf(inventoryTotalCount.toString());
		}
		//获取业务监控回滚成功失败数据量
		int orderSuccessNum=0;
		int orderFailuerNum=0;
		List<Map<String, Object>> orderRollbackCount = consumeMonitorInventoryDao.getOrderRollbackCount(map);
		for (Map<String, Object> orderMap : orderRollbackCount) {
			String status = (String) orderMap.get("status");
			Long orderTotalCount =  (Long) orderMap.get("totalCount");
			if (DERP_LOG.LOG_STATUS_1.equals(status))orderSuccessNum=Integer.valueOf(orderTotalCount.toString());
			if (DERP_LOG.LOG_STATUS_0.equals(status))orderFailuerNum=Integer.valueOf(orderTotalCount.toString());
		}
		Map<String, Object>resultMap=new HashMap<>();
		resultMap.put("rollbackList", rollbackList);
		//resultMap.put("inventorySuccessNum", inventorySuccessNum);
		//resultMap.put("inventoryFailuerNum", inventoryFailuerNum);
		resultMap.put("inventoryTotalNum", inventorySuccessNum+inventoryFailuerNum);
		
		//resultMap.put("orderSuccessNum", orderSuccessNum);
		//resultMap.put("orderFailuerNum", orderFailuerNum);
		resultMap.put("orderTotalNum", orderSuccessNum+orderFailuerNum);
		
		return resultMap;
	}
    /**
	 * 查询电商订单金额更新日志list
	 * */
	@Override
	public Map<String, Object> getAmountCoverLogList(String indexCode) throws Exception {
		Map<String, Object> resultMap=new HashMap<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("state", 0);//0-失败 1-成功
		params.put("requestMessage.indexCode", indexCode);
		List<net.sf.json.JSONObject> logJsonList = jsonMongoDao.findAll(params, CollectionEnum.MQ_ORDER_LOG.getCollectionName());
		//统计已消费完成的数量
		Long overCount = jsonMongoDao.count(params, CollectionEnum.MQ_ORDER_LOG.getCollectionName());

		List<Map<String,Object>> logList = new ArrayList<>();
		if(logJsonList==null||logJsonList.size()<=0){
			resultMap.put("overCount",overCount);
			resultMap.put("logList",logList);
			return resultMap;
		}
		for(net.sf.json.JSONObject json : logJsonList){
			Map<String,Object> logMap = new HashedMap();
			logMap.put("point",json.get("point"));
			logMap.put("model",json.get("model"));
			logMap.put("keyword",json.get("keyword"));
			logMap.put("expMsg",json.get("expMsg"));
			logList.add(logMap);
		}
		resultMap.put("overCount",overCount);
		resultMap.put("logList",logList);
		return resultMap;
	}

	@Override
	public Map importAmountCover(List<Map<String, String>> data) throws Exception {

		Map retMap = new HashMap();
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;//检查通过数量
		int failure = 0;//检查失败数量
		Map<String,Map<String,Object>> orderMapAll = new LinkedHashMap<>();//存储所有合并好的订单数据 key=商家+单号 value=order

		//存储判断是否存在相同公司+单号相同的数据 用于检查电商表体id必填 key=公司+单号 value=false-不存在  true-存在
		Map<String,Boolean> merchantCodeMap = new HashedMap();
		Map<String,Map<String,BigDecimal>> orderGoodsAmoutMap = new HashMap<>();
		for (int i = 0; i < data.size(); i++) {
			Map<String, String> map = data.get(i);

			/**1.必填字段校验*/
			String topidealCode = map.get("商家卓志编码");
			if (checkIsNullOrNot(i, topidealCode, "商家卓志编码不能为空",resultList)){
				failure++;
				continue;
			}

			String code = map.get("外部订单号");
			if (checkIsNullOrNot(i, code, "外部订单号不能为空",resultList)){
				failure++;
				continue;
			}
			code = code.trim();

			String payment = map.get("订单实付金额");
			if (checkIsNullOrNot(i, payment, "订单实付金额不能为空",resultList)){
				failure++;
				continue;
			}
			String taxes = map.get("税费");
			if (checkIsNullOrNot(i, taxes, "税费不能为空",resultList)){
				failure++;
				continue;
			}
			String wayFrtFee = map.get("运费");
			if (checkIsNullOrNot(i, wayFrtFee, "运费不能为空",resultList)){
				failure++;
				continue;
			}
			String discount = map.get("优惠减免金额");
			if (checkIsNullOrNot(i, discount, "优惠减免金额不能为空",resultList)){
				failure++;
				continue;
			}

			String key = topidealCode + "_" + code;
			if(merchantCodeMap.containsKey(key)){//存在公司+单号相同的数据
				setErrorMsg(i, "卓志编码：" + topidealCode + ",外部订单号：" + code + "存在商家+外部单号相同的数据", resultList);
				failure++;
				continue;
			}

			Map<String,Object> orderMap = new HashMap<>();
			orderMap.put("topidealCode",topidealCode);
			orderMap.put("code",code);
			orderMap.put("payment",Double.valueOf(payment));
			orderMap.put("taxes",Double.valueOf(taxes));
			orderMap.put("wayFrtFee",Double.valueOf(wayFrtFee));
			orderMap.put("discount",Double.valueOf(discount));
			orderMapAll.put(topidealCode + "_" + code, orderMap);
			merchantCodeMap.put(key, false);
			success++;
		}

        if(failure>0||orderMapAll.isEmpty()){
			success = 0;
			retMap.put("success", success);
			retMap.put("failure", failure);
			if(resultList.size()>1000){
				retMap.put("message", resultList.subList(0,1000));
			}else{
				retMap.put("message", resultList);
			}

			return retMap;
		}

        int orderTotalCount = orderMapAll.size();//合并后总单量
        String indexCode = CodeGeneratorUtil.getNo("U");//批次跟踪码
		for(Map orderTemp : orderMapAll.values()){
			JSONObject jsonObject = new JSONObject(orderTemp);
			jsonObject.put("indexCode",indexCode);
			System.out.println("报文:"+jsonObject.toString());
			SendResult send = rmqProducer.send(jsonObject.toString(),MQOrderEnum.AMOUNT_COVER.getTopic(), MQOrderEnum.AMOUNT_COVER.getTags());
			System.out.println("发送结果:"+send);
		}

		retMap.put("indexCode", indexCode);//批次跟踪码
		retMap.put("orderTotalCount", orderTotalCount);//合并后总单量
		retMap.put("success", success);
		retMap.put("failure", failure);
		retMap.put("message", resultList);
		return retMap;
	}

	/**
	 * 库存回滚
	 */
	@Override
	public boolean sendMS(String json,String topic, String tags) throws Exception {
		// 推送库存
		if((topic.equals("derp-bxm-mq") && tags.equals("derp-syndata-bxm"))
		    || (topic.equals("derp-bxm-mq") && tags.equals("sys-xm-data"))) {
			//同步备份库，根据选择区间，循环执行每一天
			net.sf.json.JSONObject jsonData = net.sf.json.JSONObject.fromObject(json);
			Map<String, Object> jsonMap = jsonData;
			String startDate = (String) jsonMap.get("startDate");
			String endDate = (String) jsonMap.get("endDate");
			List<String> dateList = TimeUtils.getYearMonthDateList(startDate,endDate);
			
			for(String selectDate:dateList) {
				net.sf.json.JSONObject mqJson = new net.sf.json.JSONObject();
				mqJson.put("topic", topic);
				mqJson.put("tags", tags);
				mqJson.put("selectTime", selectDate);
				LOGGER.debug("========selectTime:"+ selectDate+"======json"+mqJson.toString());
				SendResult send = rmqProducer.send(mqJson.toString(),topic, tags);
				System.out.println(send);
				Thread.sleep(500);
			}
		}else {
		
			SendResult send = rmqProducer.send(json,topic, tags);
			System.out.println(send);
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map importOrder100Data(Map<Integer, List<List<Object>>> data) {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int failure = 0;
		
		for (int i = 0; i < 1; i++) {// 表头
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(), objects.get(j).toArray());
				
				try {
					String shopCode = map.get("店铺编码"); 
					if(checkIsNullOrNot(j, shopCode, "店铺编码不能为空", resultList)) {
						continue ;
					}
					shopCode = shopCode.trim() ;
					
					String orderNo = map.get("订单号") ;
					if(checkIsNullOrNot(j, orderNo, "订单号不能为空", resultList)) {
						continue ;
					}
					orderNo = orderNo.trim();
					
					String startDate = map.get("开始时间") ;
					if(checkIsNullOrNot(j, startDate, "开始时间不能为空", resultList)) {
						failure += 1; 
						continue ;
					}else if(valiteDate(startDate, j, "开始时间格式有误", resultList)) {
						failure += 1; 
						continue ;
					}

					startDate = startDate.trim();
					String reg = "\\d{4}-\\d{1,2}-\\d{1,2}" ;
					Pattern pattern =Pattern.compile(reg);
					Matcher matcher = pattern.matcher(startDate);
					
					if(matcher.matches()) {
						startDate += " 00:00:00" ;
					}
					
					String endDate = map.get("结束时间") ;
					if(checkIsNullOrNot(j, endDate, "结束时间不能为空", resultList)) {
						continue ;
					}else if(valiteDate(endDate, j, "结束时间格式有误", resultList)) {
						failure += 1; 
						continue ;
					}
					endDate = endDate.trim();
					matcher = pattern.matcher(endDate);
					
					if(matcher.matches()) {
						endDate += " 00:00:00" ;
					}
					
					//比较是否开始时间大于结束时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
					long startTime = sdf.parse(startDate).getTime();
					long endTime = sdf.parse(endDate).getTime();
					
					if(startTime > endTime) {
						setErrorMsg(j, "开始时间不能大于结束时间", resultList);
						failure += 1; 
						continue ;
					}

					String isEnforce = map.get("是否强制抓单");
					if (StringUtils.isNotBlank(isEnforce)) {
						if (!("1".equals(isEnforce) || "0".equals(isEnforce))) {
							setErrorMsg(j, "是否强制抓单值只能为0或1", resultList);
							failure += 1;
							continue ;
						}
					}

					String deliveryTime = map.get("发货时间");
					if ("1".equals(isEnforce)) {
						if(checkIsNullOrNot(j, deliveryTime, "发货时间不能为空", resultList)) {
							failure += 1;
							continue ;
						}else if(valiteDate(deliveryTime, j, "发货时间格式有误", resultList)) {
							failure += 1;
							continue ;
						}
					}

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("shopCode", shopCode) ;
					jsonObject.put("orderNo", orderNo) ;
					jsonObject.put("tag", "2") ;
					jsonObject.put("startDate", startDate) ;
					jsonObject.put("endDate", endDate) ;
					jsonObject.put("isEnforce", isEnforce) ;
					jsonObject.put("deliveryTime", deliveryTime) ;

					String jsonStr = jsonObject.toJSONString();
				
					SendResult send = rmqProducer.send(jsonStr,MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTopic(), MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTags());
					System.out.println(send);
					success ++ ;
				} catch (Exception e) {
					failure ++ ;
					setErrorMsg(j, "请求MQ失败", resultList);
					continue ;
				}
			}
		}
		
		Map map = new HashMap();
		map.put("success", success);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
		
	}

	/**
	 * 把两个数组组成一个map
	 * 
	 * @param keys
	 *            键数组
	 * @param values
	 *            值数组
	 * @return 键值对应的map
	 */
	private Map<String, String> toMap(Object[] keys, Object[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				map.put((String) keys[i], values[i].toString());
			}
			return map;
		}
		return null;
	}
	
	/**
	 * 判断输入字段是否为空
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index , String content , String msg ,List<ImportErrorMessage> resultList ) {
		if(StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);
			
			return true ;
			
		}else {
			return false ;
		}
		
	}
	
	/**
	 * 错误时，设置错误内容
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
	}
	
	/**
	 * 校验时间
	 */
	private boolean valiteDate(String dateStr , int index , String msg , List<ImportErrorMessage> resultList) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String reg = "\\d{4}-\\d{1,2}-\\d{1,2}" ;
		Pattern pattern =Pattern.compile(reg);
		Matcher matcher = pattern.matcher(dateStr);
		
		if(matcher.matches()) {
			dateStr += " 00:00:00" ;
		}
		
		try {
			sdf.parse(dateStr) ;
			return false ;
		} catch (ParseException e) {
			setErrorMsg(index, msg, resultList);
			return true ;
		}
	}

	/**
	 * 批量导入库存回滚单
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map importInventoryRollbackData(Map<Integer, List<List<Object>>> data) {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int failure = 0;
		String orderCodes="";
		for (int i = 0; i < 1; i++) {// 表头
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				try {
					Map<String, String> map = this.toMap(data.get(i).get(0).toArray(), objects.get(j).toArray());
					
					String code = map.get("库存单号");
					if (StringUtils.isBlank(orderCodes)) {
						orderCodes=orderCodes+code;
					}else {
						orderCodes=orderCodes+","+code;
					}
					
					
					
					if(checkIsNullOrNot(j, code, "库存单号不能为空", resultList)) {
						failure += 1; 
						continue ;
					}
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", code) ;
					
					String jsonStr = jsonObject.toJSONString();
					
					SendResult send = rmqProducer.send(jsonStr,MQInventoryEnum.INVENTORY_DATA_ROLL_BACK.getTopic(), MQInventoryEnum.INVENTORY_DATA_ROLL_BACK.getTags());
					System.out.println(send);
					success ++ ;
				
				} catch (Exception e) {
					failure ++ ;
					setErrorMsg(j, "请求MQ失败", resultList);
					continue ;
				}
			}
			
		}
		
		Map map = new HashMap();
		map.put("success", success);
		map.put("failure", failure);
		map.put("message", resultList);
		map.put("orderCodes", orderCodes);
		return map;
	}

	
}
