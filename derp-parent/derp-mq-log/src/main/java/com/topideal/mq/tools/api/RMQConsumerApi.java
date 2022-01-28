package com.topideal.mq.tools.api;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;

/**
 *  API MQ 消费方
 * 
 */
@Component
@Scope(scopeName="singleton")
public class RMQConsumerApi {
	private final Logger logger = LoggerFactory.getLogger(RMQConsumerApi.class);
    //mq 工具类
    private DefaultMQPushConsumer consumer;
    //MQ 连接地址
    @Value("${derplogrocketmq.namesrvaddr}")
    private String namesrvAddr;
    //消费者组名
    @Value("${derplogrocketmq.apilog.consumergroup}")
    private String consumerGroup;
    //消息主题
    @Value("${derplogrocketmq.apilog.topics}")
    private String topics;
    //监听器
    @Autowired
    private MQListenerApi listener;


    /**初始化消费者
     * */
    @PostConstruct
    public void init() {
    	logger.info("初始化消费者开始-API通道...");
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(namesrvAddr);
        try {
            consumer.subscribe(topics, "*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        consumer.setInstanceName(UUID.randomUUID().toString());
        /** 注意新主题从来没有消费过,producer如果先启动,consumer后启动，间隔时间内producer发出的消息默认是接不到的,需要如下设置 */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.registerMessageListener((MessageListenerConcurrently)this.listener);
        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        logger.info("初始化消费者完成-API...group="+consumer.getConsumerGroup()+" instance="+consumer.getInstanceName());
    }
    
    /**关闭消费者
     * */
    @PreDestroy
    public void destroy() {
        consumer.shutdown();
    }

}
