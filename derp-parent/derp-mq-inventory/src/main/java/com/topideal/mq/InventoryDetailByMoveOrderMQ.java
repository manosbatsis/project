package com.topideal.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryDetailByMoveOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 移库单商品收发明细（不扣减库存）
 **/
@Component
public class InventoryDetailByMoveOrderMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryDetailByMoveOrderMQ.class);

    @Autowired
    private InventoryDetailByMoveOrderService inventoryDetailByMoveOrderService;

    public InventoryDetailByMoveOrderMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_DETAIL_BY_MOVEORDER.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_DETAIL_BY_MOVEORDER.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===============移库单审核生成商品收发明细开始===========");
        try{
            inventoryDetailByMoveOrderService.saveInventoryDetail(json,keys,topics,tags);
        }catch(Exception e){
            e.printStackTrace();
            return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
