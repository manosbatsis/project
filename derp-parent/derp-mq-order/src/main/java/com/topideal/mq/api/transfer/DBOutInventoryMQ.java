package com.topideal.mq.api.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.transfer.DBOutInventoryService;

/**
 * 出库明细(调拨模块)
 */
@Component
public class DBOutInventoryMQ extends ConsumerMQAbstract {
	/**
	 * 打印日志
	 */
	public static final Logger  LOGGER= LoggerFactory.getLogger(DBOutInventoryMQ.class);
	@Autowired
	private DBOutInventoryService dBOutInventoryService;//库存明细调拨
	
	public DBOutInventoryMQ() {
		super.setTags(MQOrderEnum.EPASS_OUT_INVENTORY_3.getTags());
		super.setTopics(MQOrderEnum.EPASS_OUT_INVENTORY_3.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		
		LOGGER.info("=================出库明细调拨模块==================");
		try {
			dBOutInventoryService.saveDBOutInventoryInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("出库明细调拨模块接口异常", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
