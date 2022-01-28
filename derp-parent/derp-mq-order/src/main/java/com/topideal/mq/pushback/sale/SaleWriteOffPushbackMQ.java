package com.topideal.mq.pushback.sale;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.SaleWriteOffPushBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 销售 出库回推
 */
@Component
public class SaleWriteOffPushbackMQ extends ConsumerMQAbstract {

    @Autowired
    private SaleWriteOffPushBackService service;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(SaleWriteOffPushbackMQ.class);

    public SaleWriteOffPushbackMQ(){
        super.setTopics(MQPushBackOrderEnum.SALE_WRITE_OFF_PUSH_BACK.getTopic());
        super.setTags(MQPushBackOrderEnum.SALE_WRITE_OFF_PUSH_BACK.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {

        synchronized (this) {
            LOGGER.info("===============销售单红冲 回推 开始===========");
            try {
                service.modifyStatus(json, keys, topics, tags);
            } catch (Exception e) {
                LOGGER.error("异常:{}", e.getMessage());
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            LOGGER.info("===============销售单红冲 回推 结束===========");
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
