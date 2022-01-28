package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryDeleteDataForZeroService;

/**
 * 删除批次库存余量为0的数据 MQ
 * Created by baols on 2018/6/11.
 */
@Component
public class InventoryDeleteDataForZeroMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryDeleteDataForZeroMQ.class);

    @Autowired
    private InventoryDeleteDataForZeroService inventoryDeleteDataForZeroService;

    public InventoryDeleteDataForZeroMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_DELETE_DATA_FOR_ZERO.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_DELETE_DATA_FOR_ZERO.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("============= 删除批次库存余量为0的数据=================>");
        try{
        	inventoryDeleteDataForZeroService.deleteInventoryDeleteDataForZero(json, keys, topics, tags);
        }catch(Exception e){
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}
