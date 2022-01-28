/*package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.XSReturnMessagePushBackService;

*//**
 * 销售退运信息库存扣减成功回推 mq
 * 2019/02/27
 * 杨创
 *//*
@Component
public class XSReturnMessagePushBackMQ extends ConsumerMQAbstract {

	@Autowired
	private XSReturnMessagePushBackService xSReturnMessagePushBackService;

	 打印日志 
	private static final Logger LOGGER = LoggerFactory.getLogger(XSReturnMessagePushBackMQ.class);

	public XSReturnMessagePushBackMQ(){
		super.setTags(MQPushBackOrderEnum.RETURN_MESSAGE_PUSH_BACK_2.getTags());
		super.setTopics(MQPushBackOrderEnum.RETURN_MESSAGE_PUSH_BACK_2.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("========================退运信息库存扣减成功回推MQ=========================");
		try {
			xSReturnMessagePushBackService.updateReturnMessagePushBackInfo(json, keys, topics, tags);
		} catch (Exception e) {
			LOGGER.error("退运信息库存扣减成功回推异常,{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
*/