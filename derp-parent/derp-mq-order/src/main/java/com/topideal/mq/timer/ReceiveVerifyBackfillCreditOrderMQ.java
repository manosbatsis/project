package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.ReceiveCloseAccountsService;
import com.topideal.service.timer.ReceiveVerifyBackfillCreditOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiveVerifyBackfillCreditOrderMQ extends ConsumerMQAbstract {

    public static final Logger LOGGER= LoggerFactory.getLogger(ReceiveVerifyBackfillCreditOrderMQ.class);

    @Autowired
    private ReceiveVerifyBackfillCreditOrderService receiveVerifyBackfillCreditOrderService;

    public ReceiveVerifyBackfillCreditOrderMQ(){
        super.setTopics(MQOrderEnum.TIMER_RECEIVE_BACKFILL_CREDIT.getTopic());
        super.setTags(MQOrderEnum.TIMER_RECEIVE_BACKFILL_CREDIT.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================赊销单/赊销收款单创建的应收单收款核销回填MQ 开始=================="+json);
        synchronized (this) {
            try {
                receiveVerifyBackfillCreditOrderService.saveBackfillCredit(json, keys, topics, tags);
                LOGGER.info("===================赊销单/赊销收款单创建的应收单收款核销回填MQ 结束==================");
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("赊销单/赊销收款单创建的应收单收款核销回填数据异常",e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
