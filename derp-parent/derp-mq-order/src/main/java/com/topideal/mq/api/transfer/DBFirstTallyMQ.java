package com.topideal.mq.api.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.transfer.DBFirstTallyService;

/**
 * 调拨-初理货 MQ
 * Created by yucaifu 
 */
@Component
public class DBFirstTallyMQ extends ConsumerMQAbstract {

    @Autowired
    private DBFirstTallyService dBFirstTallyService;


    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(DBFirstTallyMQ.class);

    public DBFirstTallyMQ(){
        super.setTopics(MQOrderEnum.EPASS_FIRST_TALLY_3.getTopic());
        super.setTags(MQOrderEnum.EPASS_FIRST_TALLY_3.getTags());
    }


    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============初理货接口--调拨===========");
        try{
        	dBFirstTallyService.saveTallyInfo(json,keys,topics,tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }



}
