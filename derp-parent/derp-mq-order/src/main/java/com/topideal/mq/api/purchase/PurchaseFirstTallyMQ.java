package com.topideal.mq.api.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.purchase.PurchaseFirstTallyService;

/**
 * 初理货 MQ
 * @author zhanghx
 * 2018/7/16
 */
@Component
public class PurchaseFirstTallyMQ extends ConsumerMQAbstract {

    @Autowired
    private PurchaseFirstTallyService firstTallyService;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseFirstTallyMQ.class);

    public PurchaseFirstTallyMQ(){
        super.setTopics(MQOrderEnum.EPASS_FIRST_TALLY_1.getTopic());
        super.setTags(MQOrderEnum.EPASS_FIRST_TALLY_1.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============初理货接口===========");
        try{
            firstTallyService.saveTallyInfo(json,keys,topics,tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
