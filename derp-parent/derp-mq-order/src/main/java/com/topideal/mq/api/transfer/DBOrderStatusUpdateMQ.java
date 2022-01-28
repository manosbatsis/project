package com.topideal.mq.api.transfer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.transfer.DBOrderStatusUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 单据状态更新mq
 **/
@Component
public class DBOrderStatusUpdateMQ extends ConsumerMQAbstract {

    public static final Logger LOGGER= LoggerFactory.getLogger(DBOrderStatusUpdateMQ.class);

    @Autowired
    private DBOrderStatusUpdateService dbOrderStatusUpdateService;

    public DBOrderStatusUpdateMQ() {
        super.setTags(MQOrderEnum.EPASS_ORDER_STATUS_UPDATE_3.getTags());
        super.setTopics(MQOrderEnum.EPASS_ORDER_STATUS_UPDATE_3.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("=================单据状态更新调拨模块==================");
        try {
            dbOrderStatusUpdateService.updateDBOrderStatusInfo(json,keys,topics,tags);
        } catch (Exception e) {
            LOGGER.error("单据状态更新调拨模块接口异常", e.getMessage());
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
