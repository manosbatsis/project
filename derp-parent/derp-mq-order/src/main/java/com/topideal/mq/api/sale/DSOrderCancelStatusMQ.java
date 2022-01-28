package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.DSOrderCancelStatusService;

/**
 * 订单取消状态回推MQ
 */
@Component
public class DSOrderCancelStatusMQ extends ConsumerMQAbstract {

	@Autowired
	private DSOrderCancelStatusService orderCancelStatusService;

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DSOrderCancelStatusMQ.class);

	public DSOrderCancelStatusMQ() {
		super.setTopics(MQOrderEnum.EPASS_OEDER_CANCE_STATUS_2.getTopic());
		super.setTags(MQOrderEnum.EPASS_OEDER_CANCE_STATUS_2.getTags());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		  LOGGER.info("==============订单取消状态回推===========");
	        try{
	        	orderCancelStatusService.saveOrderCancelInfo(json,keys,topics,tags);
	        }catch(Exception e){
	            LOGGER.error("异常:{}",e.getMessage());
	            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
	        }
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
