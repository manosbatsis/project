package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.LLDLoadingDeliveriesService;

/**
 * 装载交运回推MQ —— 领料单 
 */
@Component
public class LLDLoadingDeliveriesMQ extends ConsumerMQAbstract{
	@Autowired
	private LLDLoadingDeliveriesService loadingDeliveriesService;
	
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER =LoggerFactory.getLogger(LLDLoadingDeliveriesMQ.class);
	
	public LLDLoadingDeliveriesMQ(){
		super.setTags(MQOrderEnum.EPASS_LOADINF_DELIVRIES_4.getTags());
		super.setTopics(MQOrderEnum.EPASS_LOADINF_DELIVRIES_4.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		
		LOGGER.info("================领料单—— 装载交运回推接口==================");
		
		try {
			loadingDeliveriesService.saveLoadingDeliveriesInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("领料单—— 装载交运回推接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
