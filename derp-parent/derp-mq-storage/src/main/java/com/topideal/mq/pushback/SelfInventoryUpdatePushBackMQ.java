package com.topideal.mq.pushback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.SelfInventoryUpdatePushBackService;
/**
 * 自营库存更新库存加减成功回推 mq
 * 2019/02/26
 * 杨创
 */
@Component
public class SelfInventoryUpdatePushBackMQ extends ConsumerMQAbstract{
	
	@Autowired
	private SelfInventoryUpdatePushBackService selfInventoryUpdatePushBackService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(SelfInventoryUpdatePushBackMQ.class);
	
	public SelfInventoryUpdatePushBackMQ(){
		super.setTags(MQPushBackStorageEnum.STORAGE_SELF_INVENTORY_PUSH_BACK.getTags());
		super.setTopics(MQPushBackStorageEnum.STORAGE_SELF_INVENTORY_PUSH_BACK.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================自营库存更新库存加减成功回推 MQ==================");
		try {
			boolean flag = selfInventoryUpdatePushBackService.updateAdjustmentType(json,keys,topics,tags);			
			
		} catch (Exception e) {
			LOGGER.error("自营库存更新库存加减成功回推异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
