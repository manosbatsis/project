package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.mq.tools.other.ConsumerMQAbstract;
import com.topideal.service.LowerFailService;

/**
 * 库存扣减失败重推 MQ
 */
@Component
public class LowerFailMQ extends ConsumerMQAbstract {

    @Autowired
    private LowerFailService lowerFailService;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(LowerFailMQ.class);

    public LowerFailMQ(){
        super.setTopics(MQLogEnum.TIMER_LOWER_FAIL.getTopic());
        super.setTags(MQLogEnum.TIMER_LOWER_FAIL.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String topics,String tags) {
        LOGGER.info("=============库存扣减失败重推 MQ===========");
        try{
        	lowerFailService.resendLowerFailRecode();
        }catch(Exception e){
            LOGGER.error("库存扣减失败重推 MQ 异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
