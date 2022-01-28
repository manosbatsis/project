package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.XSTTransfersOutStoragePushBackService;

/**
 * 调拨出库库存加减成功回推MQ
 * 2019/02/27
 * 杨创
 */
@Component
public class XSTTransfersOutStoragePushBackMQ extends ConsumerMQAbstract {
	/**
	 * 打印日志
	 */
	public static final Logger  LOGGER= LoggerFactory.getLogger(XSTTransfersOutStoragePushBackMQ.class);
	@Autowired
	private XSTTransfersOutStoragePushBackService xSTTransfersOutStoragePushBackService;//调拨出库
	
	public XSTTransfersOutStoragePushBackMQ() {
		super.setTags(MQPushBackOrderEnum.TRANSFER_OUT_STIRAGE_PUSH_BACK_2_2.getTags());
		super.setTopics(MQPushBackOrderEnum.TRANSFER_OUT_STIRAGE_PUSH_BACK_2_2.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		
		LOGGER.info("=================调拨出库库存加减成功回推MQ==================");
		try {
			xSTTransfersOutStoragePushBackService.updateXSTTransfersOutStoragePushBackInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("调拨出库库存加减成功回推异常", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
