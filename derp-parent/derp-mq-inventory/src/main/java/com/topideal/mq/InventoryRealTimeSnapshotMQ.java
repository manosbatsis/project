package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryRealTimeSnapshotService;

/**
 *  实时库存快照   MQ
 * Created by baols on 2018/6/11.
 */
@Component
public class InventoryRealTimeSnapshotMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryRealTimeSnapshotMQ.class);

    @Autowired
    private InventoryRealTimeSnapshotService inventoryRealTimeSnapshotService;

    public InventoryRealTimeSnapshotMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_REAL_TIME_SNAPSHOT.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_REAL_TIME_SNAPSHOT.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("=============实时库存快照   =================>");
        try{
        	inventoryRealTimeSnapshotService.synsInventoryRealTimeInterface(json, keys, topics, tags);
        }catch(Exception e){
        	e.printStackTrace();
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}
