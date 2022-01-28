package com.topideal.mq.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.BigCargoTallyService;
/**
 * 大货理货回推
 * 杨创 
 * 2019.04.01
 */
@Component
public class BigCargoTallyMQ extends ConsumerMQAbstract{
	
	@Autowired
	private BigCargoTallyService bigCargoTallyService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(BigCargoTallyMQ.class);
	
	public BigCargoTallyMQ(){
		super.setTags(MQStorageEnum.EPASS_BIG_CARGO_TALLY_PUSH.getTags());
		super.setTopics(MQStorageEnum.EPASS_BIG_CARGO_TALLY_PUSH.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================单据调整类型-大货理货接口==================");
		try {
			boolean flag = bigCargoTallyService.saveBigCargoTallyInfo(json,keys,topics,tags);
			
		} catch (Exception e) {
			LOGGER.error("单据调整类型-大货理货接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
