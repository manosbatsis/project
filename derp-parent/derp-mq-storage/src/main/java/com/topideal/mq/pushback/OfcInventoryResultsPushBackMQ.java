package com.topideal.mq.pushback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackStorageEnum;
import com.topideal.common.enums.MQStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.OfcInventoryResultsPushBackService;

/**
 * ofc盘点结果库存加减成功回推 mq
 * @author 杨创 
 * 2019/02/26
 */
@Component
public class OfcInventoryResultsPushBackMQ extends ConsumerMQAbstract {

	@Autowired
	private OfcInventoryResultsPushBackService ofcInventoryResultsPushBackService;// 库存回推ofc 盘点结果

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OfcInventoryResultsPushBackMQ.class);

	public OfcInventoryResultsPushBackMQ() {
		super.setTags(MQPushBackStorageEnum.OFCSTORAGE_RESULTS_PUSH_BACK.getTags());
		super.setTopics(MQPushBackStorageEnum.OFCSTORAGE_RESULTS_PUSH_BACK.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("========================ofc盘点结果库存加减成功回推 MQ=========================");
		try {
			ofcInventoryResultsPushBackService.updateOfcInventoryResultsPushBackInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("ofc盘点结果库存加减成功回推 mq异常,{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
