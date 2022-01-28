package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.AdvanceBillNcBackfillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时器循环调NC4.11账单状态查询接口获取预收账单状态更新
 *
 */
@Component
public class AdvanceBillNcBackfillMQ extends ConsumerMQAbstract {

    public static final Logger LOGGER= LoggerFactory.getLogger(AdvanceBillNcBackfillMQ.class);

    @Autowired
    private AdvanceBillNcBackfillService advanceBillNcBackfillService;

    public AdvanceBillNcBackfillMQ(){
        super.setTags(MQOrderEnum.TIMER_ADVANCE_BILL_BACKFILL.getTags());
        super.setTopics(MQOrderEnum.TIMER_ADVANCE_BILL_BACKFILL.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================NC结算预收账单回填==================");
        try {
            // 采购本位币金额回填
            advanceBillNcBackfillService.saveReceiveBillNcBackfill(json,keys,topics,tags);

            LOGGER.info("===================NC结算预收账单回填结束==================");

        } catch (Exception e) {
            LOGGER.error("NC结算账单回填异常",e.getMessage());
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
