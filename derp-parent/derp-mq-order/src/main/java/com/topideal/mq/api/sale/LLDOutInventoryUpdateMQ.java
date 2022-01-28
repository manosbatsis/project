package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.LLDOutInventoryUpdateService;

/**
 * 出库明细回推MQ —— 领料单 
 */
@Component
public class LLDOutInventoryUpdateMQ extends ConsumerMQAbstract{
	@Autowired
	private LLDOutInventoryUpdateService lldOutInventoryService;
	
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER =LoggerFactory.getLogger(LLDOutInventoryUpdateMQ.class);
	
	public LLDOutInventoryUpdateMQ(){
		super.setTags(MQOrderEnum.EPASS_OUT_INVENTORY_4_1.getTags());
		super.setTopics(MQOrderEnum.EPASS_OUT_INVENTORY_4_1.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		
		LOGGER.info("================领料单 扣减库存—— 出库明细回推接口==================");
		
		try {
			lldOutInventoryService.saveOutInventoryInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("领料单  扣减库存—— 出库明细回推接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
