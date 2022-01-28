package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.CrawlerBillService;
import com.topideal.service.timer.CrawlerJDGoodsAndStocksaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 同步京东爬虫商品-每日销量库存MQ
 */
@Component
public class CrawlerJDGoodsAndStocksaleMQ extends ConsumerMQAbstract{

	@Autowired
	private CrawlerJDGoodsAndStocksaleService crawlerJDGoodsAndStocksaleService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(CrawlerJDGoodsAndStocksaleMQ.class);

	public CrawlerJDGoodsAndStocksaleMQ(){
		super.setTags(MQOrderEnum.TIMER_CRAWLER_JD_GOODSANDSTOCKSALE.getTags());
		super.setTopics(MQOrderEnum.TIMER_CRAWLER_JD_GOODSANDSTOCKSALE.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================同步京东爬虫商品-每日销量==================");
		synchronized(this){
			try {
				crawlerJDGoodsAndStocksaleService.synsJDGoodsAndStocksale(json,keys,topics,tags);
			} catch (Exception e) {
				LOGGER.error("同步京东爬虫商品-每日销量消费异常",e.getMessage());
				e.printStackTrace();
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
