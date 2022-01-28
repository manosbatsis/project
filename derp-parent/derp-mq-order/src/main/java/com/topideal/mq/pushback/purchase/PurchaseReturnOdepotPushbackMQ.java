package com.topideal.mq.pushback.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.purchase.PurchaseReturnOdepotPushbackService;

/**
 * 采购退货出库  回推
 * @author gy
 */
@Component
public class PurchaseReturnOdepotPushbackMQ extends ConsumerMQAbstract {

    @Autowired
    private PurchaseReturnOdepotPushbackService service; 

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseReturnOdepotPushbackMQ.class);

    public PurchaseReturnOdepotPushbackMQ(){
        super.setTopics(MQPushBackOrderEnum.PURCHASE_RETURN_ODEPOT_PUSH_BACK.getTopic());
        super.setTags(MQPushBackOrderEnum.PURCHASE_RETURN_ODEPOT_PUSH_BACK.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============采购 退货出库 回推===========");
        try{
        	service.modifyStatus(json, keys, topics, tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
