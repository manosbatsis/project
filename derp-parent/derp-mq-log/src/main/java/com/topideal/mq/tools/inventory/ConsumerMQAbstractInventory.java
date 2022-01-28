package com.topideal.mq.tools.inventory;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;

/**
 * 库存监控  MQ业务实现抽象类
 * 
 */
public abstract class ConsumerMQAbstractInventory {
    //消息主题
    private String topics;
    //消息标签
    private String tags;

    public abstract ConsumeConcurrentlyStatus execute(String json,String topics,String tags);


    public String getTopics() {
        return topics;
    }

    public String getTags() {
        return tags;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
