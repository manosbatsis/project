package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.mq.tools.other.ConsumerMQAbstract;
import com.topideal.service.MoveLogService;

/**
 * 日志搬迁
 * @author zhanghx
 */
@Component
public class MoveLogMQ extends ConsumerMQAbstract {

    @Autowired
    private MoveLogService service;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(MoveLogMQ.class);

    public MoveLogMQ(){
        super.setTopics(MQLogEnum.LOG_MOVE.getTopic());
        super.setTags(MQLogEnum.LOG_MOVE.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String topics,String tags) {
        LOGGER.info("=============日志搬迁开始===========");
        try{
        	synchronized (this) {
        		service.synsMoveLog(json, topics, tags);
			}
        }catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
