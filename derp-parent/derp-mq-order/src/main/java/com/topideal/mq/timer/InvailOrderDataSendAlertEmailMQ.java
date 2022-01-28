package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.InvailOrderDataSendAlertEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Wilson Lau
 * @Date: 2021/10/8 17:52
 * @Description: 定时器检查昨天新增、修改的数据，发邮件预警：
 * 定时器检查昨天新增、修改的数据，发邮件预警：
 *  a、查询调拨出库单、调拨入库单的调入仓和调出仓与调拨单的调入仓和调出仓不一致的数据
 *  b、查询电商订单的发货时间比交易时间早的单据。
 */
@Component
public class InvailOrderDataSendAlertEmailMQ extends ConsumerMQAbstract {

    @Autowired
    private InvailOrderDataSendAlertEmailService invailOrderDataSendAlertEmailService;

    /**
     * 打印日志
     */
    public static final Logger LOGGER= LoggerFactory.getLogger(InvailOrderDataSendAlertEmailMQ.class);

    public InvailOrderDataSendAlertEmailMQ(){
        super.setTags(MQOrderEnum.TIMER_ALERT_MEAIL_ORDER_DATA.getTags());
        super.setTopics(MQOrderEnum.TIMER_ALERT_MEAIL_ORDER_DATA.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================定时器检查电商订单以及调拨单数据=================="+json);
        long  startTime = System.currentTimeMillis();
        try {
            invailOrderDataSendAlertEmailService.checkDataAndSendAlertEmail(json, keys, topics, tags);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("定时器检查电商订单以及调拨单数据MQ异常",e);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("===================定时器检查电商订单以及调拨单数据MQ 结束, 耗费了:" +  (endTime - startTime) + " ms" + "==================");
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

    }
}
