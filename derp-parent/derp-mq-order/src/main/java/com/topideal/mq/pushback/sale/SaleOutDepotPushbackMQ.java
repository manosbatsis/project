package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.SaleOutDepotPushbackService;

/**
 * 销售 出库回推
 */
@Component
public class SaleOutDepotPushbackMQ extends ConsumerMQAbstract {

    @Autowired
    private SaleOutDepotPushbackService service;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(SaleOutDepotPushbackMQ.class);

    public SaleOutDepotPushbackMQ(){
        super.setTopics(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTopic()); 
        super.setTags(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============销售出库 回推===========");
        synchronized (this) {
            try {
                service.modifyStatus(json, keys, topics, tags);
            } catch (Exception e) {
                LOGGER.error("异常:{}", e.getMessage());
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
