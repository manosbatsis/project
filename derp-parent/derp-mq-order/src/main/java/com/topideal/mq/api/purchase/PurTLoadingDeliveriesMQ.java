package com.topideal.mq.api.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.purchase.PurTLoadingDeliveriesService;

/**
 * 装载交运-采购退货回推MQ
 */
@Component
public class PurTLoadingDeliveriesMQ extends ConsumerMQAbstract{
	@Autowired
	private PurTLoadingDeliveriesService purTLoadingDeliveriesService;
	
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER =LoggerFactory.getLogger(PurTLoadingDeliveriesMQ.class);
	
	public PurTLoadingDeliveriesMQ(){
		super.setTags(MQOrderEnum.EPASS_LOADINF_DELIVRIES_1.getTags());
		super.setTopics(MQOrderEnum.EPASS_LOADINF_DELIVRIES_1.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		
		LOGGER.info("================装载交运-采购退货回推接口==================");
		
		try {
			purTLoadingDeliveriesService.saveLoadingDeliveriesInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("装载交运-采购退货回推接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
