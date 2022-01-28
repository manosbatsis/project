package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SyncGoodsInfoToFinanceService;

/**
 * 销售订单商品同步到金融系统
 */
@Component
public class SyncGoodsInfoToFinanceMQ extends ConsumerMQAbstract{
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(SyncGoodsInfoToFinanceMQ.class);
	@Autowired 
	private SyncGoodsInfoToFinanceService syncGoodsInfoToFinanceService;
	
	public SyncGoodsInfoToFinanceMQ(){
		super.setTags(MQOrderEnum.TIMER_SYN_GOODSINFO_TO_FINANCE.getTags());
		super.setTopics(MQOrderEnum.TIMER_SYN_GOODSINFO_TO_FINANCE.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================销售订单商品同步到金融系统开始==================");
		synchronized(this){
			try {
				
				syncGoodsInfoToFinanceService.syncGoodsInfoToFinance(json, keys, topics, tags);
				LOGGER.info("===================销售订单商品同步到金融系统结束==================");
				
			} catch (Exception e) {
				LOGGER.error("销售订单商品同步到金融系统异常："+e,e);
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
