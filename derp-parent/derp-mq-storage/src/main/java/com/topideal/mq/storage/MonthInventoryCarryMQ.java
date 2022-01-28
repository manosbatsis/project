package com.topideal.mq.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.StorageReturnMessagePushService;
import com.topideal.service.storage.MonthInventoryCarryService;

/**
 * 月库存结转MQ
 * 
 * @author 杨创 2018/7/16
 */
@Component
public class MonthInventoryCarryMQ extends ConsumerMQAbstract {

	@Autowired
	private MonthInventoryCarryService monthInventoryCarryService;// 仓储退信息

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MonthInventoryCarryMQ.class);

	public MonthInventoryCarryMQ() {
		super.setTags(MQStorageEnum.DERP_MONTH_INVENTORY_CARRY.getTags());
		super.setTopics(MQStorageEnum.DERP_MONTH_INVENTORY_CARRY.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("========================仓储退运信息接口MQ=========================");
		try {
			monthInventoryCarryService.saveMonthInventoryCarryInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("仓储退运信息接口异常,{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
