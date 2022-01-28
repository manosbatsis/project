package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryWriteOffService;

/**
 *  库存单据红冲
 *  杨创  2021/01/10
 */
@Component
public class InventoryWriteOffMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryWriteOffMQ.class);

    @Autowired
    private InventoryWriteOffService inventoryWriteOffService;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ

    public InventoryWriteOffMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_WRITE_OFF.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_WRITE_OFF.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============库存单据红冲接口==========="+json);

        try{
        	inventoryWriteOffService.synsInventoryWriteOff(json,keys,topics,tags);
        }catch(Exception e){
        	e.printStackTrace();
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
