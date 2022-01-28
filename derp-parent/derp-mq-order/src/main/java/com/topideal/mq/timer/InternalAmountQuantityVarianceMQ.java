package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.InternalAmountQuantityVarianceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huangrenya
 * 内部交易金额数量差异提醒定时任务
 **/
@Component
public class InternalAmountQuantityVarianceMQ extends ConsumerMQAbstract {
    /**
     * 打印日志
     */
    public static final Logger LOGGER= LoggerFactory.getLogger(InternalAmountQuantityVarianceMQ.class);

    @Autowired
    private InternalAmountQuantityVarianceService internalAmountQuantityVarianceService;

    public InternalAmountQuantityVarianceMQ() {
        super.setTags(MQOrderEnum.TIME_ITERNAL_QUANTITY_VARIANCE_ORDER_ENUM.getTags());
        super.setTopics(MQOrderEnum.TIME_ITERNAL_QUANTITY_VARIANCE_ORDER_ENUM.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("==================内部交易金额数量差异提醒开始==================");
        try {

            internalAmountQuantityVarianceService.getInternalAmountQuantityOrder(json,keys,topics,tags);

            LOGGER.info("==================内部交易金额数量差异提醒结束==================");
        }catch (Exception e){
            LOGGER.error("内部交易金额数量差异提醒结束报错信息",e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }


}
