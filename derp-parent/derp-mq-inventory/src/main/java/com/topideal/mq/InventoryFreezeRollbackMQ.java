package com.topideal.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryFreezeRollBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 库存回滚
 * @author 杨创
 *2019-07-09
 */
@Component
public class InventoryFreezeRollbackMQ extends ConsumerMQAbstract {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryFreezeRollbackMQ.class);

	@Autowired
	private InventoryFreezeRollBackService inventoryFreezeRollBackService;

	public InventoryFreezeRollbackMQ() {
		super.setTopics(MQInventoryEnum.INVENTORY_FREEZE_ROLL_BACK.getTopic());
		super.setTags(MQInventoryEnum.INVENTORY_FREEZE_ROLL_BACK.getTags());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		synchronized(this) {
			LOGGER.info("===============单据回滚重新冻结库存-开始===========");
			try {

				inventoryFreezeRollBackService.saveInventoryfreezeRollBack(json, keys, topics, tags);

			} catch (Exception e) {
				LOGGER.info("===============单据回滚重新冻结库存-异常===========");
				e.printStackTrace();
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
			LOGGER.info("===============单据回滚重新冻结库存-结束===========");
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

	}
}
