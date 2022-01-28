package com.topideal.mq.api.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.transfer.DBTransfersOutStorageService;

/**
 * 调拨出库MQ
 * @author yucaifu
 */
@Component
public class DBTransfersOutStorageMQ extends ConsumerMQAbstract {
	/**
	 * 打印日志
	 */
	public static final Logger  LOGGER= LoggerFactory.getLogger(DBTransfersOutStorageMQ.class);
	@Autowired
	private DBTransfersOutStorageService transfersOutStorageService;//调拨出库
	
	public DBTransfersOutStorageMQ() {
		super.setTags(MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_3.getTags());
		super.setTopics(MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_3.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		
		LOGGER.info("=================调拨出库接口==================");
		try {
			transfersOutStorageService.saveTransfersOutStorageInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("调拨出库接口异常", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
