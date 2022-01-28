package com.topideal.mq.order;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.order.SaveToCReceiveBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/1 16:36
 * @Description: 定时读取爬虫库的平台结算数据，生成Toc应收账单
 */
@Component
public class SaveToCReceiveBillByCrawlerMQ extends ConsumerMQAbstract {

    public static final Logger LOGGER = LoggerFactory.getLogger(SaveToCReceiveBillByCrawlerMQ.class);

    @Autowired
    private SaveToCReceiveBillService saveToCReceiveBillService;

    public SaveToCReceiveBillByCrawlerMQ(){
        super.setTags(MQOrderEnum.TOC_RECEIVE_BILL_BY_CRAWLER_GENERATE.getTags());
        super.setTopics(MQOrderEnum.TOC_RECEIVE_BILL_BY_CRAWLER_GENERATE.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        try {
            LOGGER.info("读取爬虫库的平台结算数据生成Toc应收账单：json ======================" + json);
            saveToCReceiveBillService.SaveToCReceiveBillByCrawler(json, keys, topics, tags);
        } catch (Exception e) {
            LOGGER.error("读取爬虫库的平台结算数据生成Toc应收账单失败", e);
            e.printStackTrace();
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        LOGGER.info("读取爬虫库的平台结算数据生成Toc应收账单结束");
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
