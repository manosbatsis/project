package com.topideal.mq.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.OfcInventoryResultsPushService;

/**
 * ofc盘点结果
 * 
 * @author 杨创 2018/12/3
 */
@Component
public class OfcInventoryResultsPushMQ extends ConsumerMQAbstract {

	@Autowired
	private OfcInventoryResultsPushService ofcInventoryResultsPushService;// ofc盘点结果

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OfcInventoryResultsPushMQ.class);

	public OfcInventoryResultsPushMQ() {
		super.setTags(MQStorageEnum.EPASS_OFCSTORAGE_RESULTS_PUSH.getTags());
		super.setTopics(MQStorageEnum.EPASS_OFCSTORAGE_RESULTS_PUSH.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("========================Ofc仓储盘点结果MQ=========================");
		try {
			ofcInventoryResultsPushService.saveOfcInventoryResultsPushInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("Ofc仓储盘点结果接口异常,{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
