package com.topideal.mq.tools.order;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 业务监控 MQ 消费者监听器（订单/推送外部API/仓储/报表）
 *
 */
@Component
public class MQListenerOrder implements MessageListenerConcurrently {
    //业务实现类集合
	@Autowired
    private List<ConsumerMQAbstractOrder> list;


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            for (ConsumerMQAbstractOrder mq : list) {
                if(msg.getTopic().equals(mq.getTopics())&&(msg.getTags()).equals(mq.getTags())){
                      //调用接口，处理数据
                	System.out.println("业务通道");
                    return  mq.execute(new String(msg.getBody()),msg.getTopic(),msg.getTags());
                }
            }
        }
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    public void setList(List<ConsumerMQAbstractOrder> list) {
        this.list = list;
    }
}
