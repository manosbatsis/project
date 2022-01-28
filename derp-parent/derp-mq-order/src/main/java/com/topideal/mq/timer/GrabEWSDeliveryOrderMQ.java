package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.api.ews.EwsUtils;
import com.topideal.api.ews.w01.GrabEWSDeliveryOrderRequest;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.GrabEWSDeliveryOrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 抓取寄售商e仓发货订单MQ
 */
@Component
public class GrabEWSDeliveryOrderMQ extends ConsumerMQAbstract{

	@Autowired
	private GrabEWSDeliveryOrderService grabEWSDeliveryOrderService;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(GrabEWSDeliveryOrderMQ.class);

	public GrabEWSDeliveryOrderMQ(){
		super.setTags(MQOrderEnum.TIMER_EWS_DELIVERY_ORDER.getTags());
		super.setTopics(MQOrderEnum.TIMER_EWS_DELIVERY_ORDER.getTopic());
	}


	@Override
	public ConsumeConcurrentlyStatus execute(String jsonStr,String keys,String topics,String tags) {
		LOGGER.info("===================抓取寄售商e仓发货订单==================");
		LOGGER.info("抓取寄售商e仓发货订单order_mq 接收定时器开始"+jsonStr);
		try {
			JSONObject jsonTagData = JSONObject.fromObject(jsonStr);
			String tag = (String)jsonTagData.get("tag");// "1" 定时器动触发 ,"2"手动触发
			String startTime = (String)jsonTagData.get("startTime");// 查询开始时间
			String endTime = (String)jsonTagData.get("endTime");// 查询结束时间
			String orderId = (String)jsonTagData.get("orderId");// 订单号
			String merchantCodeListStr = (String)jsonTagData.get("merchantCode");
			GrabEWSDeliveryOrderRequest request=getGrabEWSDeliveryRequest(tag,startTime,endTime,orderId);
			if ("2".equals(tag)&&StringUtils.isBlank(merchantCodeListStr)) {// 手动抓取
				JSONObject jsonError= new  JSONObject();
				jsonError.put("expMsg","手动抓取卓志商家编码不能为空");
				grabEWSDeliveryOrderService.grabEWSDeliveryOrderError(jsonError,null,null);
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
			if (StringUtils.isBlank(merchantCodeListStr))merchantCodeListStr = ApolloUtil.ewsTopidealcode;
			List<String> merchantCodeList = Arrays.asList(merchantCodeListStr.split(","));
			// 获取卓志编码对应的商家名称
			Map<String, Object> merchantMap=new HashMap<String, Object>();
			merchantMap.put("isProxy", "0");
			List<MerchantInfoMongo> merchantList = merchantInfoMongoDao.findAll(merchantMap);
			Map<String, String> merchantNameMap=new HashMap<>();
			for (MerchantInfoMongo merchantInfoMongo : merchantList) {
				merchantNameMap.put(merchantInfoMongo.getTopidealCode(), merchantInfoMongo.getName());
			}

			//商家循环
			for (String merchantCode : merchantCodeList) {
				request.setMerchant_code(merchantCode);//商家编码（卓志编码）//"1000000204,1000000626,0000138"
				Object jsonRequest = com.alibaba.fastjson.JSONObject.toJSON(request);
				com.alibaba.fastjson.JSONObject jSONObject=(com.alibaba.fastjson.JSONObject)jsonRequest;
				// 抓取第e仓订单
				String remark="(请关闭监控从新抓取)抓取寄售商e仓发货订单总量报文:商家编码:"+merchantCode; //抓取日志描述
				// 抓取e仓
				JSONObject jsonData=getGrabEWSDeliveryOrder(jSONObject,remark);
				Thread.sleep(500);// 抓取睡眠500毫秒 防止抓取对方响应为空
				if (jsonData==null)continue;
				String orderNum = jsonData.getString("order_num");// 订单总数
				Double ceil = Math.ceil(Integer.valueOf(orderNum) * 0.01);// 循环请求
				for (int j = 0; j < ceil; j++) {
					jSONObject.put("page_no", j + 1);// 从新设置起始页
					remark="(请关闭监控从新抓取)抓取蓝精灵已经发货和已完成订单第"+(j+1)+"次报文";
					// 抓取e仓
					jsonData=getGrabEWSDeliveryOrder(jSONObject,remark);
					if (jsonData==null)continue;
					JSONArray jsonOrderArray = jsonData.getJSONArray("order_list");
					for (Object orderObject : jsonOrderArray) {
						JSONObject jSONOrderObject = (JSONObject) orderObject;
						jSONOrderObject.put("merchantName", merchantNameMap.get(merchantCode));
						String orderNo="";
						try {
							orderNo = jSONOrderObject.getString("order_id");// 外部单号
							LOGGER.error("推送单个消费端json:"+ jSONOrderObject.toString());
							rocketMQProducer.send(jSONOrderObject.toString(),MQOrderEnum.GETONE_EWS_DELIVERY_ORDER.getTopic(),MQOrderEnum.GETONE_EWS_DELIVERY_ORDER.getTags());
						    //推送一单后睡眠40毫秒
							Thread.sleep(40);
						} catch (Exception e) {
							e.printStackTrace();
							JSONObject jsonError= new  JSONObject();
							jsonError.put("expMsg","推送单个第e仓消费异常");
							grabEWSDeliveryOrderService.getOneEWSDeliveryOrderError(jsonError,jSONOrderObject,orderNo);
							LOGGER.error("推送单个第e仓消费异常:json"+jSONOrderObject.toString());
						}
					}
				}
			}
			//分摊税费运费
			//Thread.sleep(60000);// 睡眠60秒
			//定时器才推送电商订单金额更新
			if (StringUtils.isBlank(tag)&&StringUtils.isBlank((String)jsonTagData.get("merchantCode"))) {
				JSONObject json = new JSONObject();
				json.put("remark", "电商订单金额更新(第e仓)");
				rocketMQProducer.send(json.toString(),MQOrderEnum.TIMER_ORDER_APPORTION_TAX_FREIGHT.getTopic(),MQOrderEnum.TIMER_ORDER_APPORTION_TAX_FREIGHT.getTags());

			}
			
		} catch (Exception e) {
			JSONObject jsonError= new  JSONObject();
			jsonError.put("expMsg","第e仓异常");
			grabEWSDeliveryOrderService.grabEWSDeliveryOrderError(jsonError,null,null);
			LOGGER.error("第e仓异常:json",e);

		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}




	private JSONObject getGrabEWSDeliveryOrder(com.alibaba.fastjson.JSONObject jSONObject,String remark) throws InterruptedException {
		JSONObject jsonData=null;
		try {
			// 查询第e仓订单
			String findResult = EwsUtils.getGrabEWSDeliveryOrder(jSONObject);
			jsonData = JSONObject.fromObject(findResult);
			LOGGER.info(remark+findResult);
			String orderNum = jsonData.getString("order_num");// 订单总数
			Double ceil = Math.ceil(Integer.valueOf(orderNum) * 0.01);
			String status = (String)jsonData.get("status");// 1代表成功，2代表失败
			String note = (String)jsonData.get("note");// 回执说明
			JSONArray jsonOrderArray = jsonData.getJSONArray("order_list");
			return jsonData;
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject jsonError= new  JSONObject();
			jsonError.put("expMsg",remark+"报文异常");
			// 监控抓取第e仓数据是否失败
			grabEWSDeliveryOrderService.grabEWSDeliveryOrderError(jsonError,null,null);
			LOGGER.error(remark+"报文异常"+jsonData);
		}
		return null;
	}


	/**
	 * 获取请求实体
	 * @param tag
	 * @param startTime
	 * @param endTime
	 * @param orderId
	 * @return
	 */
	private GrabEWSDeliveryOrderRequest getGrabEWSDeliveryRequest(String tag, String startTime, String endTime,
			String orderId) {
		GrabEWSDeliveryOrderRequest request= new GrabEWSDeliveryOrderRequest();
		request.setRoot_in("JFX");
		request.setPage_no(1);//页码。取值范围:大于零的整数。默认值为1,即默认返回第一页数据。
		request.setPage_size(100);//每页条数。取值范围:大于零的整数; 默认值：100。
		request.setDatatype(3);//1按发货时间 2按支付时间 3更新时间
		request.setStatus("016");//已发货：016
		if ("2".equals(tag)) {// 手动
			request.setStart_time(startTime);//查询交易创建时间开始。格式:yyyy-MM-dd HH:mm:ss。默认返回3天数据
			request.setEnd_time(endTime);//查询交易创建时间结束。格式:yyyy-MM-dd HH:mm:ss。
			request.setOrder_id(orderId);// 订单号

		}else{//定时器
			startTime = TimeUtils.getFiveHoursAgoDate();// 开始时间 当时时间前5小时
			endTime=TimeUtils.formatFullTime();	// 结束时间
			request.setEnd_time(endTime);//查询交易创建时间结束。格式:yyyy-MM-dd HH:mm:ss。
			request.setStart_time(startTime);//查询交易创建时间开始。格式:yyyy-MM-dd HH:mm:ss。默认返回3天数据
		}
		return request;
	}

}
