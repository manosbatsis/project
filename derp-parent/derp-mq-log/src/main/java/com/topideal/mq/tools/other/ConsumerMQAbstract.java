package com.topideal.mq.tools.other;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;

/**
 * 其他日志MQ业务实现抽象类
 * 
 */
public abstract class ConsumerMQAbstract {
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
