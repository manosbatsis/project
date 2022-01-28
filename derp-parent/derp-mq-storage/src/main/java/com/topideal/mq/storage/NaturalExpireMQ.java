package com.topideal.mq.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.storage.NaturalExpireService;

/**
 * 自然过期MQ
 * 
 * @author 杨创 2019/10/22
 */
@Component
public class NaturalExpireMQ extends ConsumerMQAbstract {

	@Autowired
	private NaturalExpireService naturalExpireService;// 自然过期

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(NaturalExpireMQ.class);

	public NaturalExpireMQ() {
		super.setTags(MQStorageEnum.DERP_STORAGE_NATURAL_EXPIRE.getTags());
		super.setTopics(MQStorageEnum.DERP_STORAGE_NATURAL_EXPIRE.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("========================自然过期MQ=========================");
		try {
			naturalExpireService.savenaturalExpire(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("自然过期MQ异常,{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
