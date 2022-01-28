package com.topideal.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.ReminderEmailSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 发送邮件提醒类型
 */
@Component
public class ReminderEmailSendMQ extends ConsumerMQAbstract {
    /**
     * 打印日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(ReminderEmailSendMQ.class);

    @Autowired
    private ReminderEmailSendService reminderEmailSendService;

    public ReminderEmailSendMQ() {
        super.setTags(MQErpEnum.SEND_REMINDER_MAIL.getTags());
        super.setTopics(MQErpEnum.SEND_REMINDER_MAIL.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("=============发送邮件提醒开始json: --==========="+json);
        try {
            //发送邮件提醒
            reminderEmailSendService.saveSendMail(json,keys,tags,tags);
        } catch (Exception e) {
            LOGGER.error("发送应收邮件异常:{}", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
