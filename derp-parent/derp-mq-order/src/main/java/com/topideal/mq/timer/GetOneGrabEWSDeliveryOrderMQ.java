package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.GrabEWSDeliveryOrderService;
/**
 * 抓取寄售商e仓发货订单MQ
 */
@Component
public class GetOneGrabEWSDeliveryOrderMQ extends ConsumerMQAbstract{
	
	@Autowired
	private GrabEWSDeliveryOrderService grabEWSDeliveryOrderService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(GetOneGrabEWSDeliveryOrderMQ.class);
	
	public GetOneGrabEWSDeliveryOrderMQ(){
		super.setTags(MQOrderEnum.GETONE_EWS_DELIVERY_ORDER.getTags());
		super.setTopics(MQOrderEnum.GETONE_EWS_DELIVERY_ORDER.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================消费单个e仓订单==================");
		LOGGER.info("消费单个e仓订单开始"+json);		
		try {
			grabEWSDeliveryOrderService.insertDatafilterGrabEWSDeliveryOrder(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
