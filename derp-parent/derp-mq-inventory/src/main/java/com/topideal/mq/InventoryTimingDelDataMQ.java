package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryTimingDelDataService;

/**
 * 定时器删除数据 MQ
 * @author 
 */
@Component
public class InventoryTimingDelDataMQ extends ConsumerMQAbstract {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryTimingDelDataMQ.class);

	@Autowired
	private InventoryTimingDelDataService inventoryTimingDelDataService;
	

	public InventoryTimingDelDataMQ() {
		super.setTopics(MQInventoryEnum.INVENTORY_TIMING_DELETE_DATA.getTopic());
		super.setTags(MQInventoryEnum.INVENTORY_TIMING_DELETE_DATA.getTags());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============删除历史快照数据-库存  =================>");
		try {
			inventoryTimingDelDataService.delData(json, keys, topics, tags);
		LOGGER.info("=============删除历史快照数据-库存  =================>");	
		} catch (Exception e) {
			e.getMessage();
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
}
