package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.PurchaseSdOrderTimerService;
/**
 * 采购SD单自动生成定时器MQ
 */
@Component
public class PurchaseSdOrderTimerMQ extends ConsumerMQAbstract{
	
	@Autowired
	private PurchaseSdOrderTimerService purchaseSdOrderTimerService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(PurchaseSdOrderTimerMQ.class);
	
	public PurchaseSdOrderTimerMQ(){
		super.setTags(MQOrderEnum.TIMER_PURCHASE_SD_ORDER.getTags());
		super.setTopics(MQOrderEnum.TIMER_PURCHASE_SD_ORDER.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================采购SD单自动生成=================");
		synchronized(this){
			try {
				
				// 采购SD单自动生成
				purchaseSdOrderTimerService.savePurchaseSdOrder(json,keys,topics,tags);
				
				LOGGER.info("===================采购SD单自动生成结束==================");
				
			} catch (Exception e) {
				LOGGER.error("采购SD单自动生成异常",e.getMessage());
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
