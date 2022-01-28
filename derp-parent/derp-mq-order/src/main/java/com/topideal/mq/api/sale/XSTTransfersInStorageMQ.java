/*package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.XSTTransfersInStorageService;
*//**
 * 调拨入库MQ
 *//*
@Component
public class XSTTransfersInStorageMQ extends ConsumerMQAbstract {
	打印日志
    private static final Logger LOGGER = LoggerFactory.getLogger(XSTTransfersInStorageMQ.class);
    @Autowired
    private XSTTransfersInStorageService transfersInStorageService;// 调拨入库service
    
    public  XSTTransfersInStorageMQ() {
		super.setTags(MQOrderEnum.EPASS_TRANSFER_IN_STIRAGE_2.getTags());
		super.setTopics(MQOrderEnum.EPASS_TRANSFER_IN_STIRAGE_2.getTopic());
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("====================调拨入库===================");
		try {
			transfersInStorageService.saveTransfersInStorage(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("调拨入库异常", e.getMessage());
			return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
*/