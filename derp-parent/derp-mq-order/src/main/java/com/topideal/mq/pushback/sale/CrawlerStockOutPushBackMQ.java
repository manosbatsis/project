/*
package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.CrawlerStockOutPushBackService;

*/
/**
 * 爬虫生成出库单库存扣减成功回推
 *//*

@Component
public class CrawlerStockOutPushBackMQ extends ConsumerMQAbstract {

	@Autowired
	private CrawlerStockOutPushBackService crawlerStockOutPushBackService;

	*/
/* 打印日志 *//*

	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerStockOutPushBackMQ.class);

	public CrawlerStockOutPushBackMQ(){
		super.setTags(MQPushBackOrderEnum.CRAWLER_STOCK_OUT_PUSH_BACK.getTags());
		super.setTopics(MQPushBackOrderEnum.CRAWLER_STOCK_OUT_PUSH_BACK.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("========================爬虫生成出库单库存扣减成功回推MQ=========================");
		try {
			crawlerStockOutPushBackService.modifyStatus(json, keys, topics, tags);
		} catch (Exception e) {
			LOGGER.error("爬虫生成出库单库存扣减成功回推异常,{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
*/
