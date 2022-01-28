package com.topideal.mq.pushback.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.purchase.ConfirmDepotPushbackService;
import com.topideal.service.pushback.purchase.DeclareDeliveryPushbackService;

/**
 * 预申报 打托入库 回推
 * @author zhanghx
 * 2018/7/16
 */
@Component
public class DeclareDeliveryPushbackMQ extends ConsumerMQAbstract {

    @Autowired
    private DeclareDeliveryPushbackService service; 

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(DeclareDeliveryPushbackMQ.class);

    public DeclareDeliveryPushbackMQ(){
        super.setTopics(MQPushBackOrderEnum.DECLARE_DELIVERY_PUSH_BACK.getTopic());
        super.setTags(MQPushBackOrderEnum.DECLARE_DELIVERY_PUSH_BACK.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============采购 预申报打托入库 回推===========");
        try{
        	service.modifyStatus(json, keys, topics, tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e);
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
