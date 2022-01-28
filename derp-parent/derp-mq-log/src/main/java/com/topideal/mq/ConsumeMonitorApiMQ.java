package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.mq.tools.api.ConsumerMQAbstractApi;
import com.topideal.service.ConsumeMonitorService;

/**
 * 日志监控MQ-API通道
 * @author zhanghx
 * 2018/8/17
 */
@Component
public class ConsumeMonitorApiMQ extends ConsumerMQAbstractApi {

	@Autowired
    private ConsumeMonitorService consumeMonitorService;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumeMonitorApiMQ.class);

    public ConsumeMonitorApiMQ(){
        super.setTopics(MQLogEnum.LOG_API.getTopic());
        super.setTags(MQLogEnum.LOG_API.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String topics,String tags) {
        LOGGER.info("=============api日志监控===========");
        try{
        	consumeMonitorService.save(json, topics, tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}