package com.topideal.mq.api.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.transfer.DBLoadingDeliveriesService;

/**
 * 装载交运回推MQ
 * @author yucaifu
 */
@Component
public class DBLoadingDeliveriesMQ extends ConsumerMQAbstract{
	@Autowired
	private DBLoadingDeliveriesService dBLoadingDeliveriesService;
	
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER =LoggerFactory.getLogger(DBLoadingDeliveriesMQ.class);
	
	public DBLoadingDeliveriesMQ(){
		super.setTags(MQOrderEnum.EPASS_LOADINF_DELIVRIES_3.getTags());
		super.setTopics(MQOrderEnum.EPASS_LOADINF_DELIVRIES_3.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		
		LOGGER.info("================装载交运回推接口-调拨==================");
		
		try {
			dBLoadingDeliveriesService.saveLoadingDeliveriesInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("装载交运回推-调拨接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
