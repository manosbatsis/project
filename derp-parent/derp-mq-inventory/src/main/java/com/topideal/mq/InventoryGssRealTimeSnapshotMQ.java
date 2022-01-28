package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryRealTimeSnapshotService;

/**--此类已废弃
 *  Gss实时库存快照   MQ
 * Created by baols on 2018/6/11.
 */
@Component
public class InventoryGssRealTimeSnapshotMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryGssRealTimeSnapshotMQ.class);

    @Autowired
    private InventoryRealTimeSnapshotService inventoryRealTimeSnapshotService;

    public InventoryGssRealTimeSnapshotMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_GSS_REAL_TIME_SNAPSHOT.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_GSS_REAL_TIME_SNAPSHOT.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("=============Gss实时库存快照   =================>");
        try{
        	//inventoryRealTimeSnapshotService.synsInventoryRealTimeGss(json, keys, topics, tags);
        }catch(Exception e){
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}
