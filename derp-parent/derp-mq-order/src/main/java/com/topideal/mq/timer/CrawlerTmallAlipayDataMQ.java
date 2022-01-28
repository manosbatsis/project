package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.CrawlerTmallAlipayDataService;
import com.topideal.service.timer.CrawlerVIPExtraDataService;
/**
 * 定时器同步天猫支付数据
 */
@Component
public class CrawlerTmallAlipayDataMQ extends ConsumerMQAbstract{
	
	@Autowired
	private CrawlerTmallAlipayDataService crawlerTmallAlipayDataService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(CrawlerTmallAlipayDataMQ.class);
	
	public CrawlerTmallAlipayDataMQ(){
		super.setTags(MQOrderEnum.TIMER_CRAWLER_TMALL_ALIPAY_DATA.getTags());
		super.setTopics(MQOrderEnum.TIMER_CRAWLER_TMALL_ALIPAY_DATA.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================定时器同步天猫支付数据明细==================");
		synchronized(this){
			try {
				
				// 同步每日结算数据汇总
				crawlerTmallAlipayDataService.insertDailySettleBatch(json,keys,topics,tags);
				//同步每日结算明细数据
				crawlerTmallAlipayDataService.insertDailySettle(json,keys,topics,tags);
				//同步每日结算明细数据
				crawlerTmallAlipayDataService.insertDailyFee(json,keys,topics,tags);
				//每月结算费用
				crawlerTmallAlipayDataService.insertMonthlyFee(json,keys,topics,tags);

				
			} catch (Exception e) {
				LOGGER.error("生成唯品账单活动折扣明细消费异常", e);
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
