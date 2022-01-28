package com.topideal.mq.pushback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.BigCargoTallyPushBackService;
/**
 * 大货理货加减成功回推 mq 
 */
@Component
public class BigCargoTallyPushBackMQ extends ConsumerMQAbstract{
	
	@Autowired
	private BigCargoTallyPushBackService bigCargoTallyPushBackService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(BigCargoTallyPushBackMQ.class);
	
	public BigCargoTallyPushBackMQ(){
		super.setTags(MQPushBackStorageEnum.STORAGE_BIG_CARGO_TALLY_PUSH_BACK.getTags());
		super.setTopics(MQPushBackStorageEnum.STORAGE_BIG_CARGO_TALLY_PUSH_BACK.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================大货理货接口库存加减成功回推 MQ==================");
		try {
			boolean flag = bigCargoTallyPushBackService.updateBigCargoTally(json,keys,topics,tags);
			System.out.println();
			
		} catch (Exception e) {
			LOGGER.error("大货理货接口库存加减成功回推异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
