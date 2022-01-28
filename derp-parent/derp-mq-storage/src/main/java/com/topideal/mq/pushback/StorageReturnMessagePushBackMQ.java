package com.topideal.mq.pushback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.storageReturnMessagePushBackService;

/**
 * 仓储退运信息库存加减成功回推 mq
 * @author 杨创 
 * 2019/02/26
 */
@Component
public class StorageReturnMessagePushBackMQ extends ConsumerMQAbstract {

	@Autowired
	private storageReturnMessagePushBackService storageReturnMessagePushBackService;// 仓储退运信息库存加减成功回推

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StorageReturnMessagePushBackMQ.class);

	public StorageReturnMessagePushBackMQ() {
		super.setTags(MQPushBackStorageEnum.STORAGE_RETURN_MESSAGE_PUSH_BACK.getTags());
		super.setTopics(MQPushBackStorageEnum.STORAGE_RETURN_MESSAGE_PUSH_BACK.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("========================仓储退运信息库存加减成功回推MQ=========================");
		try {
			storageReturnMessagePushBackService.updatestorageReturnMessagePushBackInfo(json, keys, topics, tags);
		} catch (Exception e) {
			LOGGER.error("仓储退运信息库存加减成功回推异常,{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
