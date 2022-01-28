package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.CrawlerTmallPurchaseOderService;
/**
 * 同步天猫平台采购订单
 */
@Component
public class CrawlerTmallPurchaseOderMQ extends ConsumerMQAbstract{
	
	@Autowired
	private CrawlerTmallPurchaseOderService crawlerTmallPurchaseOderService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(CrawlerTmallPurchaseOderMQ.class);
	
	public CrawlerTmallPurchaseOderMQ(){
		super.setTags(MQOrderEnum.TIMER_CRAWLER_TMALL_PURCHASE_ORDER.getTags());
		super.setTopics(MQOrderEnum.TIMER_CRAWLER_TMALL_PURCHASE_ORDER.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================同步天猫平台采购订单==================");
		synchronized(this){
		   try {
				//生成爬虫账单
			   crawlerTmallPurchaseOderService.insertTmallPurchaseOder(json,keys,topics,tags);
				Thread.sleep(2000);
			} catch (Exception e) {
				LOGGER.error("同步天猫平台采购订单消费异常",e.getMessage());
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
