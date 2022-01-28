package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.FailLogRePushService;

/**
 * 锁记录不存在MQ
 */
@Component
public class FailType03MQ extends ConsumerMQAbstract {

   @Autowired
   private FailLogRePushService failLogRePushService ;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(FailType03MQ.class);

    public FailType03MQ(){
        super.setTopics(MQLogEnum.MQ_FAILTYPE_03.getTopic());
        super.setTags(MQLogEnum.MQ_FAILTYPE_03.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String topics,String tags) {
        LOGGER.info("=============mq_failtype_03自动重推开始===========");
        try{
        	failLogRePushService.rePushFailLog( json, tags) ;
        }catch(Exception e){
            LOGGER.error("mq_failtype_03自动重推 MQ 异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        LOGGER.info("=============mq_failtype_03自动重推结束===========");
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
