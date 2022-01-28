/*package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.XSTransfersOutStoragePushBackService;

*//**
 *销售调拨出库库存加减成功回推 mq
 * 2019/02/27
 * 杨创
 *//*
@Component
public class XSTransfersOutStoragePushBackMQ extends ConsumerMQAbstract {
	*//**
	 * 打印日志
	 *//*
	public static final Logger  LOGGER= LoggerFactory.getLogger(XSTransfersOutStoragePushBackMQ.class);
	@Autowired
	private XSTransfersOutStoragePushBackService xSTransfersOutStoragePushBackService;//调拨出库
	
	public XSTransfersOutStoragePushBackMQ() {
		super.setTags(MQPushBackOrderEnum.TRANSFER_OUT_STIRAGE_PUSH_BACK_2_1.getTags());
		super.setTopics(MQPushBackOrderEnum.TRANSFER_OUT_STIRAGE_PUSH_BACK_2_1.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		
		LOGGER.info("=================销售调拨出库库存加减成功回推MQ==================");
		try {
			xSTransfersOutStoragePushBackService.updatexSTransfersOutStoragePushBackInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("销售调拨出库库存加减成功回推异常", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
*/