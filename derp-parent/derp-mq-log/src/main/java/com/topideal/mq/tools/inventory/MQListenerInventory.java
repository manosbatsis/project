package com.topideal.mq.tools.inventory;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 库存监控 MQ 消费者监听器
 *
 */
@Component
public class MQListenerInventory implements MessageListenerConcurrently {
    //业务实现类集合
	@Autowired
    private List<ConsumerMQAbstractInventory> list;


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            for (ConsumerMQAbstractInventory mq : list) {
                if(msg.getTopic().equals(mq.getTopics())&&(msg.getTags()).equals(mq.getTags())){
                      //调用接口，处理数据
                	System.out.println("库存通道");
                    mq.execute(new String(msg.getBody()),msg.getTopic(),msg.getTags());
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
        }
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    public void setList(List<ConsumerMQAbstractInventory> list) {
        this.list = list;
    }
}
