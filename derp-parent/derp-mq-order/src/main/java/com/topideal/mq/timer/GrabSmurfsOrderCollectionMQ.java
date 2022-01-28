package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.api.smurfs.s01.SmurfsOrderCollectionRequest;
import com.topideal.common.constant.DERP;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.MerchantShopRelMongoDao;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.GrabSmurfsOrderCollectionService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 抓取蓝精灵订单采集发货和完成订单MQ
 */
@Component
public class GrabSmurfsOrderCollectionMQ extends ConsumerMQAbstract{

	@Autowired
	private GrabSmurfsOrderCollectionService grabSmurfsOrderCollectionService;
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao;// 商家和店铺中间表
	@Autowired
	private RMQProducer rocketMQProducer;//MQ

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(GrabSmurfsOrderCollectionMQ.class);

	public GrabSmurfsOrderCollectionMQ(){
		super.setTags(MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTags());
		super.setTopics(MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTopic());
	}



	@Override
	public ConsumeConcurrentlyStatus execute(String jsonStr,String keys,String topics,String tags) {

		
		LOGGER.info("===================抓取蓝精灵订单采集发货和完成订单==================");
		LOGGER.info("抓取蓝精灵已经发货和已完成订单 接收定时器开始"+jsonStr);
		try {
			JSONObject jsonTagData = JSONObject.fromObject(jsonStr);
			String tag = (String)jsonTagData.get("tag");// "" 定时器动触发 ,"2"手动触发
			String startDate = (String)jsonTagData.get("startDate");// 开始时间
			String endDate = (String)jsonTagData.get("endDate");//订单创建时间结束
			String orderNo = (String)jsonTagData.get("orderNo");// 订单号
			String isEnforce = (String) jsonTagData.get("isEnforce"); //是否强制抓单 1-是 0-否
			String deliveryTime = (String) jsonTagData.get("deliveryTime"); //发货时间（强制抓单是，发货时间不能为空）
			String shopCode = (String)jsonTagData.get("shopCode");


			/*查询所有启用的店铺*/
			List<MerchantShopRelMongo> merchantShopRelList=getMerchantShopRel(orderNo,shopCode);
			/*封装抓取实体*/
			SmurfsOrderCollectionRequest requestModelTag=getSmurfsOrderCollectionRequest(tag,orderNo,startDate,endDate);

			//订单状态，1:待付款; 2:已付款; 3:已发货; 4:已关闭; 5:已完成; 6:部分发货; 7:支付宝交易;
			String statusStr = "3,5,4";
			//102接口只支持一种订单状态查询
			List<String> statusList = Arrays.asList(statusStr.split(","));
			for (String status : statusList) {
				// 店铺循环查询
				for (MerchantShopRelMongo mongo : merchantShopRelList) {
					requestModelTag.setStoreCode(mongo.getShopCode());
					requestModelTag.setStatus(status);
					JSONObject requestJSONObjectTag = JSONObject.fromObject(requestModelTag);
					String remark="(请关闭监控从新抓取)抓取蓝精灵已经发货和已完成订单总量报文"; //抓取日志描述
					//抓取蓝精灵已经发货和已完成订单 第1次报文
					/*循环抓取蓝精灵*/
					JSONObject jsonOrder=getSmurfsUtilsSend(requestJSONObjectTag,mongo,remark);
					if (jsonOrder==null)continue;
					String totalNum = jsonOrder.getString("total");
					Double ceil = Math.ceil(Integer.valueOf(totalNum)* 0.01);
					for (int j = 0; j < ceil; j++) {// 循环请求
						requestJSONObjectTag.put("pageNo", j + 1);
						remark="(请关闭监控从新抓取)抓取蓝精灵已经发货和已完成订单第"+j+"次报文";
						// 循环抓取
						jsonOrder=getSmurfsUtilsSend(requestJSONObjectTag,mongo,remark);
						if (jsonOrder==null)continue;
						JSONArray jsonOrderArray = jsonOrder.getJSONArray("list");
						for (Object orderObject: jsonOrderArray) {
							JSONObject jSONOrderObject = (JSONObject) orderObject;
							//orderNo空校验
							if (jSONOrderObject.get("orderNo")==null||StringUtils.isBlank(jSONOrderObject.getString("orderNo"))) {
								sendOneOrderError("orderNo is null",jSONOrderObject,null);
								continue;
							}
							orderNo = jSONOrderObject.getString("orderNo");
							//订单商品空校验
							JSONArray jsonGoodArray = jSONOrderObject.getJSONArray("orderGoodsList");
							if (jsonGoodArray==null||jsonGoodArray.size()==0) {
								sendOneOrderError("orderGoodsList is null",jSONOrderObject,orderNo);
								continue;
							}
							Object goodsjsonGood0 = jsonGoodArray.get(0);
							JSONObject jsonGood0JSONObject=(JSONObject)goodsjsonGood0;
							// 如果是强制抓单，则不判断运单号是否为空// 不发日志
							if (StringUtils.isEmpty(isEnforce) || !"1".equals(isEnforce)) {
								// 如果状态是4 已关闭 并且运单号为空的订单 不进行出库
								if ("4".equals(status)&&(jsonGood0JSONObject.get("invoiceNo")==null||StringUtils.isBlank(jsonGood0JSONObject.getString("invoiceNo"))||"null".equals(jsonGood0JSONObject.getString("invoiceNo")))) {
									continue;
								}
							}

							String deliveryDate=null;
							if (jSONOrderObject.get("deliveryDate")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("deliveryDate"))) {
								deliveryDate=jSONOrderObject.getString("deliveryDate");// 订单100发货时间
							}
							//如果强制抓单，当报文中发货时间为空时，取导入的发货时间，若都为空则报错  // 如果发货时间为空 取 强制抓单的时间  如果发货时间不为空 取发货时间
							if ("1".equals(isEnforce)&&StringUtils.isEmpty(deliveryDate))deliveryDate=deliveryTime;
							if (StringUtils.isBlank(deliveryDate)) {
								sendOneOrderError("deliveryDate发货时间为空",jSONOrderObject,orderNo);
								continue;
							}


							// 不接发货时间小于2019-04-01 00:00:00的单// 不发日志
							Timestamp deliverTimestamp = TimeUtils.parseFullTime(deliveryDate);
							Timestamp deliverTimestamp2 = Timestamp.valueOf("2019-03-01 00:00:00");
							if (deliverTimestamp.getTime()<deliverTimestamp2.getTime()) {
								LOGGER.error("发货日期小于2019-03-01,订单号:"+ orderNo);
								continue;
							}


							jSONOrderObject.put("deliveryDate", deliveryDate);// 存放发货时间
							jSONOrderObject.put("derpDeliveryTime", deliveryTime);////发货时间（强制抓单是，发货时间不能为空）
							jSONOrderObject.put("derpShopCode", mongo.getShopCode());
							jSONOrderObject.put("derpIsEnforce", isEnforce);//发货时间（强制抓单是，发货时间不能为空）
							jSONOrderObject.put("derpStatus", status);
							jSONOrderObject.put("merchantName",mongo.getMerchantName());
							String json=jSONOrderObject.toString();// 得到Service json

							try {// 单个数据过滤 并插入
								// 判读订单是否已经存在// 不发送日志
								if (grabSmurfsOrderCollectionService.isExistOrderNo(orderNo)) {
									LOGGER.error("外部单号数据库已经存在,订单号:"+ orderNo);
									continue;
								}
								//LOGGER.error("推送单个消费端json:"+ json);
								rocketMQProducer.send(json,MQOrderEnum.GETONE_SMURFS_ORDER_COLLECTION_ORDER.getTopic(),MQOrderEnum.GETONE_SMURFS_ORDER_COLLECTION_ORDER.getTags());
							    //推送一单后睡眠40毫秒
								Thread.sleep(40);
							} catch (Exception e) {
								e.printStackTrace();
								JSONObject jsonError= new  JSONObject();
								jsonError.put("expMsg","推送单个蓝精灵消费异常");
								grabSmurfsOrderCollectionService.getOneSmurfsOrderCollectionError(jsonError,jSONOrderObject,orderNo);
								LOGGER.error("推送单个蓝精灵消费异常:json"+jSONOrderObject.toString());
							}
						}
					}

				}
			}
			// 定时器来的才推送 消费电商订单金额更新
			if (StringUtils.isBlank(tag)&&StringUtils.isBlank(shopCode)) {
				//分摊税费运费
				//Thread.sleep(60000);// 睡眠60秒
				JSONObject json = new JSONObject();
				json.put("remark", "电商订单金额更新(订单100)");
				rocketMQProducer.send(json.toString(),MQOrderEnum.TIMER_ORDER_APPORTION_TAX_FREIGHT.getTopic(),MQOrderEnum.TIMER_ORDER_APPORTION_TAX_FREIGHT.getTags());

			}
				
		} catch (Exception e) {
			JSONObject jsonError= new  JSONObject();
			jsonError.put("expMsg","抓取蓝精灵消费异常");
			grabSmurfsOrderCollectionService.grabSmurfsOrderCollectionError(jsonError,null,null);
			LOGGER.error("抓取蓝精灵消费异常");

		}

		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	/**
	 * 循环抓取蓝精灵
	 * @param requestJSONObjectTag
	 * @return
	 */
	private JSONObject getSmurfsUtilsSend(JSONObject requestJSONObjectTag,MerchantShopRelMongo mongo,String remark) {
		LOGGER.info("抓取蓝精灵订单请求报文"+requestJSONObjectTag);
		JSONObject jsonData=null;
		try {
			String findResult = SmurfsUtils.send(requestJSONObjectTag, SmurfsAPIEnum.SMURFS_ORDER_COLLECTION_REPORT2);
	        jsonData = JSONObject.fromObject(findResult);
			LOGGER.info(remark+":"+findResult);
			JSONObject jsonOrder = jsonData.getJSONObject("orderList");
			String totalNum = jsonOrder.getString("total");	 // 校验total 格式
			Double ceil = Math.ceil(Integer.valueOf(totalNum)* 0.01);
			JSONArray jsonOrderArray = jsonOrder.getJSONArray("list");
			return jsonOrder;
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject jsonError= new  JSONObject();
			jsonError.put("expMsg",remark+"异常 店铺名称:"+mongo.getShopName());
			// 监控抓取第e仓数据是否失败
			grabSmurfsOrderCollectionService.grabSmurfsOrderCollectionError(jsonError,jsonError,null);
			LOGGER.error(remark+"异常,店铺名称:"+mongo.getShopName()+jsonData);
		}
		return null;
	}



	/**封装抓取实体*/
	private SmurfsOrderCollectionRequest getSmurfsOrderCollectionRequest(String tag, String orderNo,String startDate, String endDate) {
		SmurfsOrderCollectionRequest requestModelTag = new SmurfsOrderCollectionRequest();
		requestModelTag.setPageNo(1);//页数
		requestModelTag.setPageSize(100);//每页显示的记录数
		if ("2".equals(tag)) {// 手工抓取
			requestModelTag.setOrderNo(orderNo);//订单号
			requestModelTag.setStartDate(startDate);//订单创建时间开始 格式：yyyy-MM-dd HH:mm:ss
			requestModelTag.setEndDate(endDate);//订单创建时间结束 格式：yyyy-MM-dd HH:mm:ss

		}else {// 定时器
	        startDate = TimeUtils.getFiveHoursAgoDate();// 开始时间 当时时间前5小时
	        endDate=TimeUtils.formatFullTime();	// 结束时间
			requestModelTag.setStartDate(startDate);//订单创建时间开始 格式：yyyy-MM-dd HH:mm:ss
			requestModelTag.setEndDate(endDate);//订单创建时间结束 格式：yyyy-MM-dd HH:mm:ss
		}
		return requestModelTag;
	}



	/**
	 * 获取商家店铺信息
	 * @param shopCode
	 * @return
	 */
	private List<MerchantShopRelMongo> getMerchantShopRel(String orderNo,String shopCode) {
		Map<String, Object> merchantShopRelMap = new HashMap<>();
		if (StringUtils.isNotBlank(shopCode))merchantShopRelMap.put("shopCode",shopCode );
		merchantShopRelMap.put("status", "1");//'状态(1使用中,0已禁用)'
		merchantShopRelMap.put("dataSourceCode", DERP.DATASOURCE_4);	// 数据来源：订单100
		List<MerchantShopRelMongo> merchantShopRelList = merchantShopRelMongoDao.findAll(merchantShopRelMap);
		if (merchantShopRelList.size()==0) {
			JSONObject jsonError= new  JSONObject();
			jsonError.put("expMsg","(不能重推请关闭)抓取蓝精灵已经发货和已完成订单没有查询到店铺编码,订单号："+orderNo+",店铺编码:"+shopCode);
			// 监控抓取第e仓数据是否失败
			grabSmurfsOrderCollectionService.grabSmurfsOrderCollectionError(jsonError,jsonError,null);
			LOGGER.error("expMsg","(不能重推请关闭)抓取蓝精灵已经发货和已完成订单没有查询到店铺编码,订单号："+orderNo+",店铺编码:"+shopCode);
		}
		return merchantShopRelList;
	}

	/**
	 * 发送错误日志
	 * @param jsonTagData
	 */
	private void sendOneOrderError(String expMsg, JSONObject jsonTagData,String orderNo) {
		JSONObject jsonError= new  JSONObject();
		jsonError.put("expMsg",expMsg);
		LOGGER.error(expMsg);
		// 监控抓取第e仓数据是否失败
		grabSmurfsOrderCollectionService.getOneSmurfsOrderCollectionError(jsonError,jsonTagData,orderNo);

	}

}
