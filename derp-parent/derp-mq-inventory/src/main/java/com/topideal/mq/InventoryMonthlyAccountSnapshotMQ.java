package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryMonthlyAccountSnapshotService;

/**
 *  生成已经月结的月结账单快照
 * Created by yangchuang on 2019/08/12
 */
@Component
public class InventoryMonthlyAccountSnapshotMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryMonthlyAccountSnapshotMQ.class);

    @Autowired
    private InventoryMonthlyAccountSnapshotService inventoryMonthlyAccountSnapshotService;

    public InventoryMonthlyAccountSnapshotMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_MONTHLY_ACCOUNT_SNAPSHOT.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_MONTHLY_ACCOUNT_SNAPSHOT.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============（生成已经月结的月结账单快照）接口MQ被调用===========");
        try{
        	inventoryMonthlyAccountSnapshotService.synsMonthlyAccountSnapshot(json,keys,topics,tags);
        }catch(Exception e){
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}
