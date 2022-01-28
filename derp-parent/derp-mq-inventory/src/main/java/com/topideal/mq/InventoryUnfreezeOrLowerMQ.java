package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryUnfreezeOrLowerService;

/**
 *  库存解冻和扣减接口（主要用于事物一致性）  MQ
 * Created by baols on 2018/6/11.
 */
@Component
public class InventoryUnfreezeOrLowerMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryUnfreezeOrLowerMQ.class);

    @Autowired
    private InventoryUnfreezeOrLowerService inventoryUnfreezeOrLowerService;

    public InventoryUnfreezeOrLowerMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_UNFREEZE_OR_LOWER.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_UNFREEZE_OR_LOWER.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============（库存解冻和扣减接口）接口MQ被调用===========");
        try{
        	inventoryUnfreezeOrLowerService.synsInventoryUnfreezeOrLower(json,keys,topics,tags);
        }catch(Exception e){
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}
