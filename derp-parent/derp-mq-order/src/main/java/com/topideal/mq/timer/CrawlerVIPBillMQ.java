package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.CrawlerBillService;
/**
 * 生成唯品爬虫账单MQ
 */
@Component
public class CrawlerVIPBillMQ extends ConsumerMQAbstract{
	
	@Autowired
	private CrawlerBillService crawlerBillService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(CrawlerVIPBillMQ.class);
	
	public CrawlerVIPBillMQ(){
		super.setTags(MQOrderEnum.TIMER_CRAWLER_VIP_BILL.getTags());
		super.setTopics(MQOrderEnum.TIMER_CRAWLER_VIP_BILL.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================生成唯品爬虫账单==================");
		synchronized(this){
			try {
				
				//生成爬虫账单
				crawlerBillService.insertVIPCrawlerBill(json,keys,topics,tags);
				
			} catch (Exception e) {
				LOGGER.error("生成唯品爬虫账单消费异常",e.getMessage());
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
