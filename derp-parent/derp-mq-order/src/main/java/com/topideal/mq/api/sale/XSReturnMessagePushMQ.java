/*package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.XSReturnMessagePushService;

*//**
 * 退运信息接口MQ
 *//*
@Component
public class XSReturnMessagePushMQ extends ConsumerMQAbstract{

	
	   @Autowired
	    private XSReturnMessagePushService returnMessagePushService;


	    打印日志
	    private static final Logger LOGGER = LoggerFactory.getLogger(XSReturnMessagePushMQ.class);

	
	public XSReturnMessagePushMQ(){
		super.setTags(MQOrderEnum.EPASS_RETURN_MESSAGE_PUSH_2.getTags());
		super.setTopics(MQOrderEnum.EPASS_RETURN_MESSAGE_PUSH_2.getTopic());
	}
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("========================退运信息接口=========================");
		try {
			returnMessagePushService.saveReturnMessagePushInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("退运信息接口异常,{}",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
*/