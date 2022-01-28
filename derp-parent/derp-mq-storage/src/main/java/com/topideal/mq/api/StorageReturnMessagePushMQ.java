package com.topideal.mq.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.StorageReturnMessagePushService;

/**
 * 仓储退运信息接口MQ
 * 
 * @author 杨创 2018/7/14
 */
@Component
public class StorageReturnMessagePushMQ extends ConsumerMQAbstract {

	@Autowired
	private StorageReturnMessagePushService storageReturnMessagePushService;// 仓储退信息

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StorageReturnMessagePushMQ.class);

	public StorageReturnMessagePushMQ() {
		super.setTags(MQStorageEnum.EPASS_STORAGE_RETURN_MESSAGE_PUSH.getTags());
		super.setTopics(MQStorageEnum.EPASS_STORAGE_RETURN_MESSAGE_PUSH.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("========================仓储退运信息接口MQ=========================");
		try {
			storageReturnMessagePushService.saveStorageReturnMessagePushInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("仓储退运信息接口异常,{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
