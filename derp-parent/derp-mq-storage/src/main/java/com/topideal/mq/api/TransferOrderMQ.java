package com.topideal.mq.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.TransferOrderService;
/**
 * 调拨单回推
 */
@Component
public class TransferOrderMQ extends ConsumerMQAbstract{
	
	@Autowired
	private TransferOrderService transferOrderService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(TransferOrderMQ.class);
	
	public TransferOrderMQ(){
		super.setTags(MQStorageEnum.EPASS_STORAGE_TRANSFER_ORDER.getTags());
		super.setTopics(MQStorageEnum.EPASS_STORAGE_TRANSFER_ORDER.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================单据调整类型-调拨单回推==================");
		try {
			boolean flag = transferOrderService.saveAdjustmentType(json,keys,topics,tags);
			
		} catch (Exception e) {
			LOGGER.error("单据调整类型-调拨单回推接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
