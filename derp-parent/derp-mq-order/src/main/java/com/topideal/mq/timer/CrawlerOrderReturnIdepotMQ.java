package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.CrawlerOrderReturnIdepotService;
/**
 * 爬虫生成电商售后退款单
 */
@Component
public class CrawlerOrderReturnIdepotMQ extends ConsumerMQAbstract{
	
	@Autowired
	private CrawlerOrderReturnIdepotService crawlerOrderReturnIdepotService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(CrawlerOrderReturnIdepotMQ.class);
	
	public CrawlerOrderReturnIdepotMQ(){
		super.setTags(MQOrderEnum.TIMER_CRAWLER_ORDER_RETURN_IDEPOT.getTags());
		super.setTopics(MQOrderEnum.TIMER_CRAWLER_ORDER_RETURN_IDEPOT.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================爬虫生成电商售后退款单==================");
		synchronized(this){
			try {				
				// 获取电商售后退款订单数据
				crawlerOrderReturnIdepotService.saveOrderReturnIdepot(json, keys, topics, tags);

			} catch (Exception e) {
				LOGGER.error("爬虫生成电商售后退款单异常", e);
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
