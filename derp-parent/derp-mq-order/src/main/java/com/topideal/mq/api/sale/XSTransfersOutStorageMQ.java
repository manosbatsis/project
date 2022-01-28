/*package com.topideal.mq.api.sale;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.XSTransfersOutStorageService;

*//**
 * 调拨出库MQ
 *//*
@Component
public class XSTransfersOutStorageMQ extends ConsumerMQAbstract {
	*//**
	 * 打印日志
	 *//*
	public static final Logger  LOGGER= LoggerFactory.getLogger(XSTransfersOutStorageMQ.class);
	@Autowired
	private XSTransfersOutStorageService transfersOutStorageService;//调拨出库
	
	public XSTransfersOutStorageMQ() {
		super.setTags(MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_2_1.getTags());
		super.setTopics(MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_2_1.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		
		LOGGER.info("=================调拨出库接口==================");
		try {
			//业务处理
			Map<String,List<String>> inventoryMap = transfersOutStorageService.saveTransfersOutStorageInfo(json,keys,topics,tags);
			//推送库存MQ
			transfersOutStorageService.pushInventoryMQ(inventoryMap);
		} catch (Exception e) {
			LOGGER.error("调拨出库接口异常", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
*/