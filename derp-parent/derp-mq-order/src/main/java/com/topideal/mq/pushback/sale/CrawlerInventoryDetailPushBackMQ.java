package com.topideal.mq.pushback.sale;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.CrawlerInventoryDetailPushBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 爬虫出库单库存回调通知接口保存批次信息 mq
 **/
@Component
public class CrawlerInventoryDetailPushBackMQ extends ConsumerMQAbstract {

    /* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerInventoryDetailPushBackMQ.class);

    @Autowired
    private CrawlerInventoryDetailPushBackService crawlerInventoryDetailPushBackService;

    public CrawlerInventoryDetailPushBackMQ(){
        super.setTags(MQPushBackOrderEnum.CRAWLER_INVENTORY_DETAIL_PUSH_BACK.getTags());
        super.setTopics(MQPushBackOrderEnum.CRAWLER_INVENTORY_DETAIL_PUSH_BACK.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("========================爬虫出库单库存扣减成功回推批次信息MQ=========================");
        try {
            crawlerInventoryDetailPushBackService.saveBillBatch(json, keys, topics, tags);
        } catch (Exception e) {
            LOGGER.error("爬虫出库单库存扣减成功回推批次信息异常,{}", e.getMessage());
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
