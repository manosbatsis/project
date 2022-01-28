package com.topideal.mq.api.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.transfer.DBTransfersInStorageService;
/**
 * 调拨入库MQ
 * @author yucaifu
 */
@Component
public class DBTransfersInStorageMQ extends ConsumerMQAbstract {
	/*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(DBTransfersInStorageMQ.class);
    @Autowired
    private DBTransfersInStorageService dBtransfersInStorageService;// 调拨入库service
    
    public  DBTransfersInStorageMQ() {
		super.setTags(MQOrderEnum.EPASS_TRANSFER_IN_STIRAGE_3.getTags());
		super.setTopics(MQOrderEnum.EPASS_TRANSFER_IN_STIRAGE_3.getTopic());
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("====================调拨入库-调拨===================");
		try {
			dBtransfersInStorageService.saveTransfersInStorage(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("调拨入库-调拨，异常", e.getMessage());
			return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
