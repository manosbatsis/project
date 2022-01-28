package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryQuantityAddOrLowerService;

/**
 *  （调增、调减）库存量明细   MQ
 * Created by baols on 2018/6/11.
 */
@Component
public class InventoryQuantityAddOrLowerMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryQuantityAddOrLowerMQ.class);

    @Autowired
    private InventoryQuantityAddOrLowerService inventoryQuantityAddOrLowerService;

    public InventoryQuantityAddOrLowerMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============（调整、调减）接口MQ被调用===========");
        System.out.println("------------------json："+json);
        try{
        	inventoryQuantityAddOrLowerService.synsAddInventoryQuantity(json,keys,topics,tags);
        }catch(Exception e){
        	e.printStackTrace();
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}
