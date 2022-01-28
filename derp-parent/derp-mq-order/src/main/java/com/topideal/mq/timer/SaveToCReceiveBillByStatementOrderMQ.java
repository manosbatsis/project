package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SaveToCReceiveBillByStatementOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 根据平台结算单生成Toc应收单MQ
 * @Author: Chen Yiluan
 * @Date: 2021/03/09 13:43
 **/
@Component
public class SaveToCReceiveBillByStatementOrderMQ extends ConsumerMQAbstract {

    /* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveToCReceiveBillByStatementOrderMQ.class);


    public SaveToCReceiveBillByStatementOrderMQ() {
        super.setTopics(MQOrderEnum.TOC_RECEIVE_BILL_GENERATE_BY_STATEMENT.getTopic());
        super.setTags(MQOrderEnum.TOC_RECEIVE_BILL_GENERATE_BY_STATEMENT.getTags());
    }

    @Autowired
    private SaveToCReceiveBillByStatementOrderService saveToCReceiveBillByStatementOrderService;

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================根据平台结算单生成To C应收数据MQ 开始=================="+json);
        synchronized (this) {
            try {
                saveToCReceiveBillByStatementOrderService.saveToCReceiveBill(json, keys, topics, tags);
                LOGGER.info("===================根据平台结算单生成To C应收数据MQ 结束==================");
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("根据平台结算单生成To C应收数据异常",e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
