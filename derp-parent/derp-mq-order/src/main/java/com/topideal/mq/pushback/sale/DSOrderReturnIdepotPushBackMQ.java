package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.DSOrderReturnIdepotService;


/**
 *  电商订单退货-成功回推 mq
 */
@Component
public class DSOrderReturnIdepotPushBackMQ  extends ConsumerMQAbstract{
	@Autowired
	private DSOrderReturnIdepotService dSOrderReturnIdepotService;

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(DSOrderReturnIdepotPushBackMQ.class);
	
	public DSOrderReturnIdepotPushBackMQ(){
		super.setTags(MQPushBackOrderEnum.ORDER_RETURN_IDEPOT_PUSH_BACK.getTags());
		super.setTopics(MQPushBackOrderEnum.ORDER_RETURN_IDEPOT_PUSH_BACK.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags ) {
		LOGGER.info("===================电商订单退货-库存增加成功回推==================");
		try {
			dSOrderReturnIdepotService.updateDSOrderReturnIdepotPushBackInfo(json,keys,topics,tags);
			
			
		} catch (Exception e) {
			LOGGER.error("电商订单退货--库存增加成功回推异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
