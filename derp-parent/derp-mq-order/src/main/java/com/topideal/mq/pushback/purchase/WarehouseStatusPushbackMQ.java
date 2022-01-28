package com.topideal.mq.pushback.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.purchase.WarehouseStatusPushbackService;

/**
 * 采购 进仓状态 回推
 * @author zhanghx
 */
@Component
public class WarehouseStatusPushbackMQ extends ConsumerMQAbstract {

    @Autowired 
    private WarehouseStatusPushbackService service;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseStatusPushbackMQ.class);

    public WarehouseStatusPushbackMQ(){
        super.setTopics(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK.getTopic());
        super.setTags(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============采购 确认入仓 回推===========");
        synchronized(this){
            try{
                service.modifyStatus(json, keys, topics, tags);
            }catch(Exception e){
                LOGGER.error("异常:{}",e.getMessage());
                return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
