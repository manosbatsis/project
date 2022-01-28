/*
package com.topideal.mq.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.order.GetBusinessDataPushInventoryService;
*/
/**
 * 获取业务端数据推送库存
 *//*

@Component
public class GetBusinessDataPushInventoryMQ extends ConsumerMQAbstract{
	
	@Autowired
	private GetBusinessDataPushInventoryService getBusinessDataPushInventoryService;
	*/
/**
	 * 打印日志 
	 *//*

	public static final Logger LOGGER= LoggerFactory.getLogger(GetBusinessDataPushInventoryMQ.class);
	
	public GetBusinessDataPushInventoryMQ(){
		super.setTags(MQOrderEnum.GET_BUSINESS_DATA_PUSH_INVENTORY.getTags());
		super.setTopics(MQOrderEnum.GET_BUSINESS_DATA_PUSH_INVENTORY.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags ) {
		LOGGER.info("===================获取业务端数据推送库存==================");
		try {
			getBusinessDataPushInventoryService.getInfo(json,keys,topics,tags);
			
			
		} catch (Exception e) {
			LOGGER.error("获取业务端数据推送库存异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
*/
