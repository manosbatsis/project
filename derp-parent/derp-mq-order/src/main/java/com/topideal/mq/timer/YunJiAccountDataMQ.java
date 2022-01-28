package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.YunJiAccountDataService;
/**
 * 同步云集结算爬虫账单MQ
 */
@Component
public class YunJiAccountDataMQ extends ConsumerMQAbstract{
	
	@Autowired
	private YunJiAccountDataService yunJiAccountDataService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(YunJiAccountDataMQ.class);
	
	public YunJiAccountDataMQ(){
		super.setTags(MQOrderEnum.TIMER_YUNJI_ACCOUNT_DATA.getTags());
		super.setTopics(MQOrderEnum.TIMER_YUNJI_ACCOUNT_DATA.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================同步云集结算爬虫账单开始==================");
		synchronized(this){
			try {
				//生成爬虫账单
				yunJiAccountDataService.saveAccountData(json,keys,topics,tags);
				LOGGER.info("===================同步云集结算爬虫账单结束==================");
			} catch (Exception e) {
				LOGGER.error("同步云集结算爬虫账单消费异常",e);
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
