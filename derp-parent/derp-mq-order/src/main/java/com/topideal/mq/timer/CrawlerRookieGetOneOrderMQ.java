package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.CrawlerRookieGetOneOrderService;
/**
 *消费菜鸟电商订单单个数据
 */
@Component
public class CrawlerRookieGetOneOrderMQ extends ConsumerMQAbstract{
	
	@Autowired
	private CrawlerRookieGetOneOrderService crawlerRookieGetOneOrderService;

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(CrawlerRookieGetOneOrderMQ.class);
	
	public CrawlerRookieGetOneOrderMQ(){
		super.setTags(MQOrderEnum.TIMER_CRAWLER_ROOKIE_GET_ONE_ORDER.getTags());
		super.setTopics(MQOrderEnum.TIMER_CRAWLER_ROOKIE_GET_ONE_ORDER.getTopic());
	}
	
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================消费单个菜鸟电商订单==================");

		try {			
			crawlerRookieGetOneOrderService.insertOrder(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		
	}




}
