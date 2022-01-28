package com.topideal.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryYwmsTimeSnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  众邦云实时库存快照   MQ
 */
@Component
public class InventoryYwmsRealTimeSnapshotMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryYwmsRealTimeSnapshotMQ.class);

    @Autowired
    private InventoryYwmsTimeSnapshotService inventoryYwmsTimeSnapshotService;

    public InventoryYwmsRealTimeSnapshotMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_YWMS_REAL_TIME_SNAPSHOT.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_YWMS_REAL_TIME_SNAPSHOT.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("=============众邦云实时库存快照   =================>");
        try{
            inventoryYwmsTimeSnapshotService.synsInventoryRealTimeYwms(json, keys, topics, tags);
        }catch(Exception e){
        	LOGGER.error(e.getMessage(), e);
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}
