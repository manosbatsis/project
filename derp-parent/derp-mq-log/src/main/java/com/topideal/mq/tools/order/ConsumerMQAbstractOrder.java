package com.topideal.mq.tools.order;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;

/**
 * 业务监控 MQ业务实现抽象类（订单/推送外部API/仓储/报表）
 * 
 */
public abstract class ConsumerMQAbstractOrder {
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
