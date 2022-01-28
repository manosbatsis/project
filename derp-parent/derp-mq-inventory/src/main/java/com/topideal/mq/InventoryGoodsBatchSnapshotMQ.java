package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryBatchValidityCheckService;
import com.topideal.service.InventoryGoodsBatchSnapshotService;

/**
 *  库存商品批次快照   MQ
 * Created by baols on 2018/6/11.
 */
@Component
public class InventoryGoodsBatchSnapshotMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryGoodsBatchSnapshotMQ.class);

    @Autowired
    private InventoryGoodsBatchSnapshotService inventoryGoodsBatchSnapshotService;
    @Autowired
    private InventoryBatchValidityCheckService inventoryBatchValidityCheckService;
    
    public InventoryGoodsBatchSnapshotMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_GOODS_BATCH_SNAPSHOT.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_GOODS_BATCH_SNAPSHOT.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("=============库存商品批次快照=================>");
        try{
        	inventoryBatchValidityCheckService.synsInventoryBatchValidityCheck(json, keys,topics,tags);
        	
        	inventoryGoodsBatchSnapshotService.synsInventoryGoodsBatchSnapshot(json, keys, topics, tags);
        }catch(Exception e){
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}
