package com.topideal.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryALiRealTimeSnapshotService;
import com.topideal.service.InventoryRealTimeSnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 实时库存快照   MQ
 */
@Component
public class InventoryALiRealTimeSnapshotMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryALiRealTimeSnapshotMQ.class);

    @Autowired
    private InventoryALiRealTimeSnapshotService inventoryALiRealTimeSnapshotService;

    public InventoryALiRealTimeSnapshotMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_ALI_ROOKIE_REAL_TIME_SNAPSHOT.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_ALI_ROOKIE_REAL_TIME_SNAPSHOT.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("=============阿里实时库存快照   =================>");
        try{
            inventoryALiRealTimeSnapshotService.synsInventoryRealTimeALi(json, keys, topics, tags);
        }catch(Exception e){
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}
