package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.AdvanceBillNcVoucherBackfillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时器循环调NC4.14凭证查询接口获取预收账单凭证
 *
 */
@Component
public class AdvanceBillNcVoucherBackfillMQ extends ConsumerMQAbstract {

    public static final Logger LOGGER= LoggerFactory.getLogger(AdvanceBillNcVoucherBackfillMQ.class);

    @Autowired
    private AdvanceBillNcVoucherBackfillService advanceBillNcVoucherBackfillService;


    public AdvanceBillNcVoucherBackfillMQ(){
        super.setTags(MQOrderEnum.TIMER_ADVANCE_BILL_VOUCHER_BACKFILL.getTags());
        super.setTopics(MQOrderEnum.TIMER_ADVANCE_BILL_VOUCHER_BACKFILL.getTopic());
    }
    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================NC预收凭证查询==================");
        try {
            synchronized (this) {
                // 获取应收账单凭证
                advanceBillNcVoucherBackfillService.saveReceiveBillNcVoucherBackfill(json,keys,topics,tags);
                LOGGER.info("===================NC预收凭证查询结束==================");
            }
        } catch (Exception e) {
            LOGGER.error("NC凭证查询异常",e.getMessage());
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
