package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.DSB2COrderService;
/**
 * B2C订单接MQ
 */
@Component
public class DSB2COrderMQ extends ConsumerMQAbstract{
	
	@Autowired
	private DSB2COrderService b2COrderService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(DSB2COrderMQ.class);
	
	public DSB2COrderMQ(){
		super.setTags(MQOrderEnum.EPASS_B2C_ORDER_2.getTags());
		super.setTopics(MQOrderEnum.EPASS_B2C_ORDER_2.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags ) {
		LOGGER.info("===================B2C订单接==================");
		try {
			boolean saveB2COrderInfo = b2COrderService.saveB2COrderInfo(json,keys,topics,tags);
			System.out.println();
			
		} catch (Exception e) {
			LOGGER.error("B2C订单接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
