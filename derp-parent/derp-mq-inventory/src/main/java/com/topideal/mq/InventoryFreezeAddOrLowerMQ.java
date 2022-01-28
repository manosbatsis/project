package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryFreezeAddOrLowerService;

/**
 * 冻结和释放冻结  MQ
 * Created by baols on 2018/6/11.
 */
@Component
public class InventoryFreezeAddOrLowerMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryFreezeAddOrLowerMQ.class);

    @Autowired
    private InventoryFreezeAddOrLowerService inventoryfreezeAddOrLowerService;

    public InventoryFreezeAddOrLowerMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============冻结和释放冻结  MQ===========");
        try{
        	inventoryfreezeAddOrLowerService.synsAddOrLowerInventoryfreeze(json,keys,topics,tags);
        }catch(Exception e){
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}
