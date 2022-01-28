package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryMonthlyBillService;

/**
 *  月结账单   MQ
 * Created by baols on 2018/6/11.
 */
@Component
public class InventoryMonthlyBillMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryMonthlyBillMQ.class);

    @Autowired
    private InventoryMonthlyBillService inventoryMonthlyBillService;

    public InventoryMonthlyBillMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_MONTHLY_BILL.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_MONTHLY_BILL.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============月结账单MQ被调用===========");
        try{
        	inventoryMonthlyBillService.synsInventoryMonthlyBill(json,keys,topics,tags);
        }catch(Exception e){
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}
