package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.XSLoadingDeliveriesService;

/**
 * 装载交运回推MQ
 */
@Component
public class XSLoadingDeliveriesMQ extends ConsumerMQAbstract{
	@Autowired
	private XSLoadingDeliveriesService loadingDeliveriesService;
	
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER =LoggerFactory.getLogger(XSLoadingDeliveriesMQ.class);
	
	public XSLoadingDeliveriesMQ(){
		super.setTags(MQOrderEnum.EPASS_LOADINF_DELIVRIES_2_2.getTags());
		super.setTopics(MQOrderEnum.EPASS_LOADINF_DELIVRIES_2_2.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("================装载交运回推接口==================");
		try {
			loadingDeliveriesService.saveLoadingDeliveriesInfo(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("装载交运回推接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
