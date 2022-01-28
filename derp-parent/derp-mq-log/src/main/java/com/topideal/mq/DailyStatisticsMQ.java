package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.mq.tools.other.ConsumerMQAbstract;
import com.topideal.service.DailyStatisticsService;

/**
 * 统计MQ消费情况-发送邮件 MQ
 */
@Component
public class DailyStatisticsMQ extends ConsumerMQAbstract {

    @Autowired
    private DailyStatisticsService dailyStatisticsService;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyStatisticsMQ.class);

    public DailyStatisticsMQ(){
        super.setTopics(MQLogEnum.TIMER_DAILY_STATISTICS.getTopic());
        super.setTags(MQLogEnum.TIMER_DAILY_STATISTICS.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String topics,String tags) {
        LOGGER.info("=============统计MQ消费情况-发送邮件 MQ===========");
        try{
        	dailyStatisticsService.sendMail(json, topics, tags);
        }catch(Exception e){
        	e.printStackTrace();
            LOGGER.error("统计MQ消费情况-发送邮件 MQ 异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
