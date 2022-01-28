package com.topideal.mq.tools;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * MQ 消费者监听器
 *
 */
@Component
public class MQListener implements MessageListenerConcurrently {
    //业务实现类集合
	@Autowired
    private List<ConsumerMQAbstract> list;


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            for (ConsumerMQAbstract mq : list) {
                if(msg.getTopic().equals(mq.getTopics())&&(msg.getTags()).equals(mq.getTags())){
                      //调用接口，处理数据
                    return  mq.execute(new String(msg.getBody()),msg.getKeys(),msg.getTopic(),msg.getTags());
                }
            }
        }
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    public void setList(List<ConsumerMQAbstract> list) {
        this.list = list;
    }
}
