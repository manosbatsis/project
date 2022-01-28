package com.topideal.mq.pushback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.StorageadJustmentInventoryAuditService;
/**
 * 库存调整单页面审核 库存加减成功回推 mq
 * 2019/02/27
 * 杨创
 */
@Component
public class StorageadJustmentInventoryAuditMQ extends ConsumerMQAbstract{
	
	@Autowired
	private StorageadJustmentInventoryAuditService storageadJustmentInventoryAuditService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(StorageadJustmentInventoryAuditMQ.class);
	
	public StorageadJustmentInventoryAuditMQ(){
		super.setTags(MQPushBackStorageEnum.STORAGE_ADJUSTMENT_INVENTORY_AUDIT_PUSH_BACK.getTags());
		super.setTopics(MQPushBackStorageEnum.STORAGE_ADJUSTMENT_INVENTORY_AUDIT_PUSH_BACK.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================库存调整单页面审核 库存加减成功回推MQ==================");
		try {
			boolean flag = storageadJustmentInventoryAuditService.updateAdjustmentType(json,keys,topics,tags);
			
			
		} catch (Exception e) {
			LOGGER.error("库存调整单页面审核 库存加减成功回推异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
