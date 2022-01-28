package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.mq.tools.other.ConsumerMQAbstract;
import com.topideal.service.MqConsumeFailService;

/**
 * 发送MQ消费失败预警邮件 MQ
 */
@Component
public class MqConsumeFailMQ extends ConsumerMQAbstract {

    @Autowired
    private MqConsumeFailService mqConsumeFailService;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(MqConsumeFailMQ.class);

    public MqConsumeFailMQ(){
        super.setTopics(MQLogEnum.TIMER_CONSUME_FAIL.getTopic());
        super.setTags(MQLogEnum.TIMER_CONSUME_FAIL.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String topics,String tags) {
        LOGGER.info("=============MQ消费失败预警-发送邮件 MQ===========");
        try{
        	mqConsumeFailService.sendMail(json, topics, tags);
        }catch(Exception e){
            LOGGER.error("MQ消费失败预警-发送邮件 MQ 异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
