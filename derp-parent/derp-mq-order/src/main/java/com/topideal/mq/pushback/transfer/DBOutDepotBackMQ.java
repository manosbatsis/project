package com.topideal.mq.pushback.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.transfer.DBBackService;
/**
 * 调拨库存回调通知出库
 * */
@Component
public class DBOutDepotBackMQ extends ConsumerMQAbstract {

    @Autowired
    private DBBackService service;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(DBOutDepotBackMQ.class);

    public DBOutDepotBackMQ(){
        super.setTopics(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTopic());
        super.setTags(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("==============调拨库存回调通知--出库===========");
        try{
        	service.synsOutDepotBack(json, keys, topics, tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
