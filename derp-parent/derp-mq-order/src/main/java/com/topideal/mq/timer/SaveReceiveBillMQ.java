package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SaveReceiveBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 生成应收账单
 * @Author: CYL
 * @Date: 2020/09/21 15:16
 **/
@Component
public class SaveReceiveBillMQ extends ConsumerMQAbstract {

    /* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveReceiveBillMQ.class);

    @Autowired
    private SaveReceiveBillService saveReceiveBillService;

    public SaveReceiveBillMQ() {
        super.setTopics(MQOrderEnum.RECEIVE_BILL_GENERATE.getTopic());
        super.setTags(MQOrderEnum.RECEIVE_BILL_GENERATE.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("========================生成应收账单MQ=========================");
        try {
            saveReceiveBillService.saveReceiveBill(json, keys, topics, tags);
        } catch (Exception e) {
            LOGGER.error("生成应收账单MQ异常,{}", e.getMessage());
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
