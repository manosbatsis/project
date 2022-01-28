package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryBatchValidityCheckService;

/** 此定时器移到保存批次快照前执行  此mq消费端暂时注释弃用
 *  校验批次库存明细商品效期是否已过期
 * @author baols
 *
 */
@Component
public class InventoryBatchValidityCheckMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryBatchValidityCheckMQ.class);

    @Autowired
    private InventoryBatchValidityCheckService inventoryBatchValidityCheckService;

    public InventoryBatchValidityCheckMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_BATCH_VALIDITY_CHECK.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_BATCH_VALIDITY_CHECK.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============校验批次库存明细商品效期是否已过期MQ被调用===========");
        try{
        	inventoryBatchValidityCheckService.synsInventoryBatchValidityCheck(json, keys,topics,tags);
        }catch(Exception e){
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}
