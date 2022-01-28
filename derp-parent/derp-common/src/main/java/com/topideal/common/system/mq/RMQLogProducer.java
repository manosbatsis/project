package com.topideal.common.system.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

/**
 * 日志消息生产者
 */
public class RMQLogProducer {

    private final Logger logger = LoggerFactory.getLogger(RMQLogProducer.class);

    private DefaultMQProducer defaultMQProducer;
    @Value("${derplogrocketmq.producergroup}")
    public String producerGroup;
    @Value("${derplogrocketmq.namesrvaddr}")
    public String namesrvAddr;
    
    /**
     * 初始化生产者
     */
    public void init() throws MQClientException {
        // 初始化
    	logger.info("初始化日志MQ生产者....");
        defaultMQProducer = new DefaultMQProducer(producerGroup);
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        defaultMQProducer.setInstanceName(String.valueOf(System.currentTimeMillis()));
        defaultMQProducer.start();
        logger.info("初始化日志MQ生产者完成");
    }

    /**
     * 销毁
     */
    public void destroy() {
    	logger.info("销毁日志MQ生产者");
        defaultMQProducer.shutdown();
    }

   public SendResult send(String json, String topic, String tags)throws Exception{
        Message message = new Message(topic, tags,String.valueOf(System.currentTimeMillis()),json.getBytes("utf-8"));
        return defaultMQProducer.send(message);
    }
 
 
}
