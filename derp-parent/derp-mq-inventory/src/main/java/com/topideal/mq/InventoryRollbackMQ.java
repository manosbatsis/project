package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryRollbackService;

/**
 * 库存回滚 
 * @author 杨创
 *2019-07-09
 */
@Component
public class InventoryRollbackMQ extends ConsumerMQAbstract {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryRollbackMQ.class);

	@Autowired
	private InventoryRollbackService inventoryRollbackService;

	public InventoryRollbackMQ() {
		super.setTopics(MQInventoryEnum.INVENTORY_DATA_ROLL_BACK.getTopic());
		super.setTags(MQInventoryEnum.INVENTORY_DATA_ROLL_BACK.getTags());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("===============库存回滚 MQ被调用===========");
		try {
			inventoryRollbackService.saveinventoryRollbackInfo(json, keys, topics, tags);
		} catch (Exception e) {
			e.printStackTrace();
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

	}
}
