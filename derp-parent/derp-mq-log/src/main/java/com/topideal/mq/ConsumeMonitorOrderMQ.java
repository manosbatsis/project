package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.mq.tools.order.ConsumerMQAbstractOrder;
import com.topideal.service.ConsumeMonitorService;

/**
 * 日志监控MQ-业务通道(order/仓储/报表/推送外部api)
 * @author zhanghx
 * 2018/8/17
 */
@Component
public class ConsumeMonitorOrderMQ extends ConsumerMQAbstractOrder {

    @Autowired
    private ConsumeMonitorService consumeMonitorService;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumeMonitorOrderMQ.class);

    public ConsumeMonitorOrderMQ(){
        super.setTopics(MQLogEnum.LOG_CONSUME_MONITOR.getTopic());
        super.setTags(MQLogEnum.LOG_CONSUME_MONITOR.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String topics,String tags) {
        LOGGER.info("=============业务日志监控===========");
        try{
        	consumeMonitorService.save(json, topics, tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
