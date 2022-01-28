package com.topideal.mq.pushback.sale;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.InventoryLocationAdjustPushBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 库位调整单回调修改状态
 **/
@Component
public class InventoryLocationAdjustPushBackMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger LOGGER= LoggerFactory.getLogger(InventoryLocationAdjustPushBackMQ.class);

    @Autowired
    private InventoryLocationAdjustPushBackService inventoryLocationAdjustPushBackService;

    public InventoryLocationAdjustPushBackMQ(){
        super.setTags(MQPushBackOrderEnum.INVENTORY_LOCATION_ADJUSTMENT_DETAIL_PUSH_BACK.getTags());
        super.setTopics(MQPushBackOrderEnum.INVENTORY_LOCATION_ADJUSTMENT_DETAIL_PUSH_BACK.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================库位调整单审核-商品收发明细生成成功回推==================");
        try {
            inventoryLocationAdjustPushBackService.updateAdjsutOrderStatus(json,keys,topics,tags);
        } catch (Exception e) {
            LOGGER.error("库位调整单审核-商品收发明细生成成功回推异常",e.getMessage());
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
