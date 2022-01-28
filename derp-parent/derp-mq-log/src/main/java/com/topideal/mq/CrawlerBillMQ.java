package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.mq.tools.other.ConsumerMQAbstract;
import com.topideal.service.CrawlerBillService;

/**
 * 爬虫日志 MQ
 */
@Component
public class CrawlerBillMQ extends ConsumerMQAbstract {

    @Autowired
    private CrawlerBillService crawlerBillService;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerBillMQ.class);

    public CrawlerBillMQ(){
        super.setTopics(MQLogEnum.LOG_CRAWLER_BILL.getTopic());
        super.setTags(MQLogEnum.LOG_CRAWLER_BILL.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String topics,String tags) {
        LOGGER.info("=============爬虫日志 MQ===========");
        try{
        	crawlerBillService.save(json, topics, tags);
        }catch(Exception e){
            LOGGER.error("爬虫日志 MQ 异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
