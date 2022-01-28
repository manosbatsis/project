package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SaveCutomerQuotaWarningService;

/**
 * 生成/更新客户额度预警 定时器
 */
@Component
public class SaveCutomerQuotaWarningMQ extends ConsumerMQAbstract {
    @Autowired
    private SaveCutomerQuotaWarningService service;
    /**
     * 打印日志
     */
    public static final Logger LOGGER= LoggerFactory.getLogger(SaveProjectQuatoWariningMQ.class);

    public SaveCutomerQuotaWarningMQ(){
        super.setTags(MQOrderEnum.TIMER_CUSTOMER_QUATO_WARNING.getTags());
        super.setTopics(MQOrderEnum.TIMER_CUSTOMER_QUATO_WARNING.getTopic());
    }


    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================生成/更新客户额度预警MQ=================="+json);
        try {
            service.saveCutomerQuotaWarning(json, keys, topics, tags);
        } catch (Exception e) {
            LOGGER.error("生成/更新客户额度预警MQ",e.getMessage());
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
