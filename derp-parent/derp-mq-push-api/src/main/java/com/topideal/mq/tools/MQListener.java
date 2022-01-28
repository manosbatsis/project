package com.topideal.mq.tools;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MQ 消费者监听器
 * 
 */
@Component
public class MQListener implements MessageListenerConcurrently {
    /**
     * 打印日志
     */
    public static final Logger LOGGER= LoggerFactory.getLogger(MQListener.class);
    //业务实现类集合
    @Autowired
    private List<ConsumerMQAbstract> list;


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            for (ConsumerMQAbstract mq : list) {
                if(msg.getTopic().equals(mq.getTopics())&&(msg.getTags()).equals(mq.getTags())){
                    try{
                        mq.execute(new String(msg.getBody()),msg.getKeys(),msg.getTopic(),msg.getTags());
                    }catch(Exception e){
                        LOGGER.error(e.getMessage());
                    }

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
        }
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    public void setList(List<ConsumerMQAbstract> list) {
        this.list = list;
    }
}
