package com.topideal.mq.api.sale;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.YWmsDSOrderOutDepotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 众邦云仓电商出库回推
 */
@Component
public class YWmsDSOrderOutDepotMQ extends ConsumerMQAbstract {

    @Autowired
    private YWmsDSOrderOutDepotService service;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(YWmsDSOrderOutDepotMQ.class);

    public YWmsDSOrderOutDepotMQ(){
        super.setTopics(MQOrderEnum.YWMS_DSORDER_OUTDEPOT.getTopic());
        super.setTags(MQOrderEnum.YWMS_DSORDER_OUTDEPOT.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============众邦云仓电商出库发货单接口回推===========");
        try{
        	service.saveStatus(json, keys, topics, tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
