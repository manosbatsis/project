/*package com.topideal.mq.api.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.transfer.DBReturnMessagePushService;

*//**
 * 退运信息接口MQ
 * @author YUCAIFU
 *//*
@Component
public class DBReturnMessagePushMQ extends ConsumerMQAbstract{

	
	   @Autowired
	    private DBReturnMessagePushService dBReturnMessagePushService;


	    打印日志
	    private static final Logger LOGGER = LoggerFactory.getLogger(DBReturnMessagePushMQ.class);

	
	public DBReturnMessagePushMQ(){
		super.setTags(MQOrderEnum.EPASS_RETURN_MESSAGE_PUSH_3.getTags());
		super.setTopics(MQOrderEnum.EPASS_RETURN_MESSAGE_PUSH_3.getTopic());
	}
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("========================退运信息接口-调拨=========================");
		try {
			dBReturnMessagePushService.saveReturnMessagePushInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("退运信息接口-调拨，异常,{}",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
*/