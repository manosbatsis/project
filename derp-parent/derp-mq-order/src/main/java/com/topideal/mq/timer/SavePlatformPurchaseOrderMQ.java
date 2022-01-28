package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SavePlatformPurchaseOrderService;
/**
 * 生成/更新平台采购订单
 */
@Component
public class SavePlatformPurchaseOrderMQ extends ConsumerMQAbstract{
	
	@Autowired
	private SavePlatformPurchaseOrderService savePlatformPurchaseOrderService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(SavePlatformPurchaseOrderMQ.class);
	
	public SavePlatformPurchaseOrderMQ(){
		super.setTags(MQOrderEnum.TIMER_PLATFORM_PURCHASE_ORDER.getTags());
		super.setTopics(MQOrderEnum.TIMER_PLATFORM_PURCHASE_ORDER.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================生成/更新平台采购订单MQ=================="+json);
		try {
			//生成平台采购订单(京东)
			//savePlatformPurchaseOrderService.saveJDPlatformPurchaseOrder(json, keys, topics, tags);
			//生成平台采购订单(天猫)
			//savePlatformPurchaseOrderService.saveTmallPlatformPurchaseOrder(json, keys, topics, tags);
			
			
		} catch (Exception e) {
			LOGGER.error("生成/更新平台采购订单异常",e.getMessage());
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
