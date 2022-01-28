package com.topideal.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryDetailsMoveToHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 迁移数据到历史表
 */
@Component
public class InventoryDetailsMoveToHistoryMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryDetailsMoveToHistoryMQ.class);

    @Autowired
    private InventoryDetailsMoveToHistoryService inventoryDetailsMoveToHistoryService;

    public InventoryDetailsMoveToHistoryMQ() {
        super.setTopics(MQInventoryEnum.INVENTORY_INVENTORYDETAILS_MOVETOHISTORY.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_INVENTORYDETAILS_MOVETOHISTORY.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        synchronized(this) {
            LOGGER.info("迁移数据到历史表===i_inventory_details=======开始");
            try {
                inventoryDetailsMoveToHistoryService.synsMoveToHistory(json, keys, topics, tags);
            } catch (Exception e) {
                LOGGER.info("迁移数据到历史表===i_inventory_details=======失败");
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            LOGGER.info("迁移数据到历史表===i_inventory_details=======结束");
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}