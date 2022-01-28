package com.topideal.mq.timer;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.CrawlerVipExtraDataDao;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SavePlatformCostOrderService;

import net.sf.json.JSONObject;
/**
 * 生成平台费用订单
 */
@Component
public class SavePlatformCostOrderMQ extends ConsumerMQAbstract{
	
	@Autowired
	private SavePlatformCostOrderService savePlatformCostOrderService;
	@Autowired	
	private CrawlerVipExtraDataDao crawlerVipExtraDataDao;//唯品账单活动折扣明细
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(SavePlatformCostOrderMQ.class);
	
	public SavePlatformCostOrderMQ(){
		super.setTags(MQOrderEnum.TIMER_PLATFORM_COST_ORDER.getTags());
		super.setTopics(MQOrderEnum.TIMER_PLATFORM_COST_ORDER.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================生成平台费用订单MQ=================="+json);
		synchronized(this){
			try {
				JSONObject jsonData = JSONObject.fromObject(json);
				String userCode = (String) jsonData.get("userCode");// 用户编码
				String billCode = (String) jsonData.get("bill_code");// 账单号
				Map<String, Object>parmaMap=new HashMap<String, Object>();
				if (StringUtils.isNotBlank(userCode))parmaMap.put("userCode", userCode);
				if (StringUtils.isNotBlank(billCode))parmaMap.put("billCode", billCode);
				// 分组获取账单
				List<Map<String, Object>> groupCrawlerVipExtraList = crawlerVipExtraDataDao.getGroupCrawlerVipExtra(parmaMap);
				for (Map<String, Object> map : groupCrawlerVipExtraList) {
					JSONObject jsonObject = new JSONObject();
	
					Long merchantIdMap = (Long) map.get("merchant_id");
					Long customerIdMap = (Long) map.get("customer_id");
					String billCodeMap = (String) map.get("bill_code");
					String userCodeMap = (String) map.get("user_code");
					String processingTypeMap = (String) map.get("processing_type");
					String currencyCodeMap = (String) map.get("currency_code");
					long time = TimeUtils.getNow().getTime();
					jsonObject.put("billCode", billCodeMap);
					jsonObject.put("billCodeKeyWord", billCodeMap+","+time);
					jsonObject.put("merchantId", merchantIdMap);
					jsonObject.put("customerId", customerIdMap);
					jsonObject.put("userCode", userCodeMap);
					jsonObject.put("processingType", processingTypeMap);
					jsonObject.put("currencyCode", currencyCodeMap);
					try {
						//生成平台费用订单
						savePlatformCostOrderService.savePlatformCostOrder(jsonObject.toString(), keys, topics, tags,map);
					} catch (Exception e) {
						e.printStackTrace();
						LOGGER.error("生成平台费用订单异常billCode:"+(String)map.get("bill_code"),e.getMessage());
					}
					
				}
				
				
		
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("生成平台费用订单异常",e.getMessage());
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
