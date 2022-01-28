package com.topideal.mq.pushback.purchase;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.purchase.PurchaseWriteOffPushbackService;
import com.topideal.service.pushback.purchase.impl.PurchaseReturnOdepotPushbackServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PurchaseWriteOffPushbackMQ extends ConsumerMQAbstract {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseWriteOffPushbackMQ.class);

    @Autowired
    private PurchaseWriteOffPushbackService purchaseWriteOffPushbackService;

    public PurchaseWriteOffPushbackMQ(){
        super.setTopics(MQPushBackOrderEnum.PURCHASE_WRITE_OFF_PUSH_BACK.getTopic());
        super.setTags(MQPushBackOrderEnum.PURCHASE_WRITE_OFF_PUSH_BACK.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===============采购红冲扣减库存 回推===========");
        synchronized(this){
            try{
                purchaseWriteOffPushbackService.modifyStatus(json, keys, topics, tags);
            }catch(Exception e){
                LOGGER.error("异常:{}",e.getMessage());
                return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
