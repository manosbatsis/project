package com.topideal.mq.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.SelfInventoryUpdateService;
/**
 * 自营库存更新
 */
@Component
public class SelfInventoryUpdateMQ extends ConsumerMQAbstract{
	
	@Autowired
	private SelfInventoryUpdateService selfInventoryUpdateService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(SelfInventoryUpdateMQ.class);
	
	public SelfInventoryUpdateMQ(){
		super.setTags(MQStorageEnum.EPASS_STORAGE_SELF_INVENTORY_PUSH.getTags());
		super.setTopics(MQStorageEnum.EPASS_STORAGE_SELF_INVENTORY_PUSH.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================单据调整类型-自营库存更新==================");
		try {
			boolean flag = selfInventoryUpdateService.saveAdjustmentType(json,keys,topics,tags);
			
		} catch (Exception e) {
			LOGGER.error("单据调整类型-自营库存更新接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
