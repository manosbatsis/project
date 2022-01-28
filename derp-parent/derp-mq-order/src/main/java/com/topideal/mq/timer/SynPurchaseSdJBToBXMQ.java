package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SynPurchaseSdJBToBXService;

/**
 * 嘉宝的采购SD单同步到宝信
 * @author qiancheng.chen
 * @date 2020-12-23
 *
 */
@Component
public class SynPurchaseSdJBToBXMQ extends ConsumerMQAbstract{
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(SynPurchaseSdJBToBXMQ.class);
	@Autowired 
	private SynPurchaseSdJBToBXService synPurchaseSdJBToBXService;
	
	public SynPurchaseSdJBToBXMQ(){
		super.setTags(MQOrderEnum.TIMER_SYN_PURCHASE_SD_JB_TO_BX.getTags());
		super.setTopics(MQOrderEnum.TIMER_SYN_PURCHASE_SD_JB_TO_BX.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================嘉宝的采购SD单复制到宝信开始==================");
		synchronized(this){
			try {
				//生成爬虫账单
				synPurchaseSdJBToBXService.synsPurchaseSdJBToBX(json, keys, topics, tags);
				LOGGER.info("===================嘉宝的采购SD单复制到宝信结束==================");
			} catch (Exception e) {
				LOGGER.error("嘉宝的采购SD单同步到宝信异常："+e,e);
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
