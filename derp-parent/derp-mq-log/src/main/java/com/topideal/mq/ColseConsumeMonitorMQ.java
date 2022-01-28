package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.mq.tools.other.ConsumerMQAbstract;
import com.topideal.service.ColseConsumeMonitorService;

/**
 * 回滚关闭日志监控MQ-(order/仓储/报表/推送外部api)
 * @author 杨创
 * 2020/10/27
 */
@Component
public class ColseConsumeMonitorMQ extends ConsumerMQAbstract {

    @Autowired
    private ColseConsumeMonitorService colseConsumeMonitorService;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(ColseConsumeMonitorMQ.class);

    public ColseConsumeMonitorMQ(){
        super.setTopics(MQLogEnum.LOG_CONSUME_COLSE.getTopic());
        super.setTags(MQLogEnum.LOG_CONSUME_COLSE.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String topics,String tags) {
        LOGGER.info("=============业务日志监控===========");
        try{
        	colseConsumeMonitorService.saveClose(json, topics, tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
