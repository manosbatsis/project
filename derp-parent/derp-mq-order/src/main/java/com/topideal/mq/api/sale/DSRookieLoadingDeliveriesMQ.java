package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.DSRookieLoadingDeliveriesService;


/**
 * 电商菜鸟装载交运回推MQ
 */
@Component
public class DSRookieLoadingDeliveriesMQ extends ConsumerMQAbstract{
	@Autowired
	private DSRookieLoadingDeliveriesService dSRookieLoadingDeliveriesService;
	
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER =LoggerFactory.getLogger(DSRookieLoadingDeliveriesMQ.class);
	
	public DSRookieLoadingDeliveriesMQ(){
		super.setTags(MQOrderEnum.EPASS_ROOKIE_LOADINF_DELIVRIES_2_1.getTags());
		super.setTopics(MQOrderEnum.EPASS_ROOKIE_LOADINF_DELIVRIES_2_1.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		
		LOGGER.info("================电商菜鸟装载交运回推接口==================");
		
		try {
			dSRookieLoadingDeliveriesService.saveRookieLoadingDeliveriesInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("装载交运回推接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
