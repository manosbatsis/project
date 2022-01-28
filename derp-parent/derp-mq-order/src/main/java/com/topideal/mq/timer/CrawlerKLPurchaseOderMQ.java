package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.CrawlerKLPurchaseOderService;
/**
 * 生成考拉平台采购订单
 * @author Guobs
 */
@Component
public class CrawlerKLPurchaseOderMQ extends ConsumerMQAbstract{
	
	@Autowired
	private CrawlerKLPurchaseOderService crawlerKLPurchaseOderService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(CrawlerKLPurchaseOderMQ.class);
	
	public CrawlerKLPurchaseOderMQ(){
		super.setTags(MQOrderEnum.TIMER_CRAWLER_KL_PURCHASE_ORDER.getTags());
		super.setTopics(MQOrderEnum.TIMER_CRAWLER_KL_PURCHASE_ORDER.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================同步考拉平台采购订单==================");
		synchronized(this){
		   try {
				//生成爬虫账单
			   crawlerKLPurchaseOderService.insertKLPurchaseOder(json,keys,topics,tags);

			} catch (Exception e) {
				LOGGER.error("同步考拉平台采购订单消费异常", e);
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
