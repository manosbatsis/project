package com.topideal.mq.pushback.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.purchase.YwmsPurchaseWarehousePushbackService;

/**
 * 众邦云仓采购入库库存回推
 * @author gy
 */
@Component
public class YWmsPurchaseWarehousePushbackMQ extends ConsumerMQAbstract {

    @Autowired 
    private YwmsPurchaseWarehousePushbackService service;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(YWmsPurchaseWarehousePushbackMQ.class);

    public YWmsPurchaseWarehousePushbackMQ(){
        super.setTopics(MQPushBackOrderEnum.YWMS_PURCHASE_WAREHOUSE_PUSH_BACK.getTopic());
        super.setTags(MQPushBackOrderEnum.YWMS_PURCHASE_WAREHOUSE_PUSH_BACK.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============众邦云仓采购入库库存回推===========");
        try{
        	service.modifyStatus(json, keys, topics, tags);
        }catch(Exception e){
        	e.printStackTrace();
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
