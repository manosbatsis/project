package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.ReceiveAgingReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiveAgingReportMQ  extends ConsumerMQAbstract {
    @Autowired
    private ReceiveAgingReportService receiveAgingReportService;
    /**
     * 打印日志
     */
    public static final Logger LOGGER= LoggerFactory.getLogger(ReceiveAgingReportMQ.class);

    public ReceiveAgingReportMQ(){
        super.setTags(MQOrderEnum.TIMER_RECEIVEAGING_REPORT_ORDER.getTags());
        super.setTopics(MQOrderEnum.TIMER_RECEIVEAGING_REPORT_ORDER.getTopic());
    }
    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================生成应收账龄报告MQ=================="+json);
        try {
            receiveAgingReportService.saveReceiveAgingReport(json, keys, topics, tags);
        } catch (Exception e) {
            LOGGER.error("生成应收账龄报告MQ",e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
