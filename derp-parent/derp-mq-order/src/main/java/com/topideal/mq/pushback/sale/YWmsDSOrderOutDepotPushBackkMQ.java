package com.topideal.mq.pushback.sale;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.YWmsDSOrderOutDepotPushbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 众邦云仓电商出库库存回推
 */
@Component
public class YWmsDSOrderOutDepotPushBackkMQ extends ConsumerMQAbstract {

    @Autowired
    private YWmsDSOrderOutDepotPushbackService service;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(YWmsDSOrderOutDepotPushBackkMQ.class);

    public YWmsDSOrderOutDepotPushBackkMQ(){
        super.setTopics(MQPushBackOrderEnum.YWMS_DSORDER_OUTDEPOT_PUSH_BACK.getTopic());
        super.setTags(MQPushBackOrderEnum.YWMS_DSORDER_OUTDEPOT_PUSH_BACK.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============众邦云仓电商出库库存回推===========");
        try{
        	service.modifyStatus(json, keys, topics, tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
