package com.topideal.mq.pushback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.TransferOrderPushBackService;
/**
 * 调拨单回推库存加减成功回推 mq
 */
@Component
public class TransferOrderPushBackMQ extends ConsumerMQAbstract{
	
	@Autowired
	private TransferOrderPushBackService transferOrderPushBackService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(TransferOrderPushBackMQ.class);
	
	public TransferOrderPushBackMQ(){
		super.setTags(MQPushBackStorageEnum.STORAGE_TRANSFER_ORDER_PUSH_BACK.getTags());
		super.setTopics(MQPushBackStorageEnum.STORAGE_TRANSFER_ORDER_PUSH_BACK.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================调拨单回推库存加减成功回推 MQ==================");
		try {
			boolean flag = transferOrderPushBackService.updateAdjustmentType(json,keys,topics,tags);
			System.out.println();
			
		} catch (Exception e) {
			LOGGER.error("调拨单回推库存加减成功回推异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
