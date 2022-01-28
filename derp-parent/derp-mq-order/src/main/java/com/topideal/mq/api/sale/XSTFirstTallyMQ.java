package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.XSTFirstTallyService;

/**
 * 销售退货--初理货 MQ
 * @author 杨创
 * 2018/7/26
 */
@Component
public class XSTFirstTallyMQ extends ConsumerMQAbstract {

    @Autowired
    private XSTFirstTallyService xSTFirstTallyService;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(XSTFirstTallyMQ.class);

    public XSTFirstTallyMQ(){
        super.setTopics(MQOrderEnum.EPASS_FIRST_TALLY_2.getTopic());
        super.setTags(MQOrderEnum.EPASS_FIRST_TALLY_2.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============初理货接口===========");
        try{
        	xSTFirstTallyService.saveTallyInfo(json,keys,topics,tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
