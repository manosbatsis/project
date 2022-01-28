package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryFreezeMoveService;

/**
 * 移动已完结冻结解冻数据到历史表 MQ 
 * @author zhanghx
 */
@Component
public class InventoryFreezeMoveMQ extends ConsumerMQAbstract {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryFreezeMoveMQ.class);

	@Autowired
	private InventoryFreezeMoveService service;

	public InventoryFreezeMoveMQ() {
		super.setTopics(MQInventoryEnum.INVENTORY_FREEZE_MOVE.getTopic());
		super.setTags(MQInventoryEnum.INVENTORY_FREEZE_MOVE.getTags());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("===============移动已完结冻结解冻数据到历史表 MQ被调用===========");
		try {
			service.synsMove(json, keys, topics, tags);
		} catch (Exception e) {
			e.printStackTrace();
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

	}
}
