package com.topideal.mq.pushback;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.CrawlerOutDepotInventoryLowerPushBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 爬虫出库单库存扣减回调mq
 **/
@Component
public class CrawlerOutDepotInventoryLowerPushBackMQ extends ConsumerMQAbstract {

    /* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerOutDepotInventoryLowerPushBackMQ.class);

    @Autowired
    private CrawlerOutDepotInventoryLowerPushBackService crawlerOutDepotInventoryLowerPushBackService;

    public CrawlerOutDepotInventoryLowerPushBackMQ(){
        super.setTags(MQInventoryEnum.CRAWLER_OUT_DEPOT_INVENTORY_LOWER_PUSH_BACK.getTags());
        super.setTopics(MQInventoryEnum.CRAWLER_OUT_DEPOT_INVENTORY_LOWER_PUSH_BACK.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("========================爬虫出库单库存扣减成功抓取出库单收发明细信息回调MQ=========================");
        try {
            crawlerOutDepotInventoryLowerPushBackService.FetchCrawlerOutDepotInfo(json, keys, topics, tags);
        } catch (Exception e) {
            LOGGER.error("爬虫出库单库存扣减成功抓取出库单收发明细信息回推异常,{}", e.getMessage());
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
