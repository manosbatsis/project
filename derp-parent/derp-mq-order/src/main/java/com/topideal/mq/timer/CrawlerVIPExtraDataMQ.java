package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.CrawlerVIPExtraDataService;
/**
 * 生成唯品账单活动折扣明细MQ
 */
@Component
public class CrawlerVIPExtraDataMQ extends ConsumerMQAbstract{
	
	@Autowired
	private CrawlerVIPExtraDataService crawlerVIPExtraDataService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(CrawlerVIPExtraDataMQ.class);
	
	public CrawlerVIPExtraDataMQ(){
		super.setTags(MQOrderEnum.TIMER_CRAWLER_VIP_EXTRA_DATA.getTags());
		super.setTopics(MQOrderEnum.TIMER_CRAWLER_VIP_EXTRA_DATA.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================生成唯品账单活动折扣明细==================");
		synchronized(this){
			try {
				
				// 生成爬虫账单
				crawlerVIPExtraDataService.insertVIPCrawlerExtraData(json,keys,topics,tags);
				// 生成爬虫文件
				crawlerVIPExtraDataService.insertVIPCrawlerFileData(json,keys,topics,tags);
				
			} catch (Exception e) {
				LOGGER.error("生成唯品账单活动折扣明细消费异常", e);
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
