package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.GrabDerpPurchaseOrderService;
/**
 * 抓取经分销收到发票没有付款的采购订单MQ
 */
@Component
public class GrabDerpPurchaseOrderMQ extends ConsumerMQAbstract{
	
	@Autowired
	private GrabDerpPurchaseOrderService grabDerpPurchaseOrderService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(GrabDerpPurchaseOrderMQ.class);
	
	public GrabDerpPurchaseOrderMQ(){
		super.setTags(MQOrderEnum.TIMER_DERP_PURCHASE_ORDER.getTags());
		super.setTopics(MQOrderEnum.TIMER_DERP_PURCHASE_ORDER.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================抓取经分销收到发票没有付款的采购订单==================");
		try {
			//抓取经分销收到发票没有付款的采购订单
			grabDerpPurchaseOrderService.saveGrabDerpPurchaseOrder(json, keys, topics, tags);
			
			
		} catch (Exception e) {
			LOGGER.error("抓取经分销收到发票没有付款的采购订单 异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
