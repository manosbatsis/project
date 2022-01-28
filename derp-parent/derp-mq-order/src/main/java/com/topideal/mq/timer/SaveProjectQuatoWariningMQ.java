package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SaveProjectQuatoWariningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生成项目额度预警
 */
@Component
public class SaveProjectQuatoWariningMQ extends ConsumerMQAbstract{

	@Autowired
	private SaveProjectQuatoWariningService service;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(SaveProjectQuatoWariningMQ.class);

	public SaveProjectQuatoWariningMQ(){
		super.setTags(MQOrderEnum.TIMER_PROJUCET_QUATO_WARNING.getTags());
		super.setTopics(MQOrderEnum.TIMER_PROJUCET_QUATO_WARNING.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================生成项目额度预警MQ=================="+json);
		try {
			synchronized(this){
				//生成项目额度预警
				service.saveProjectQuatoWariningService(json, keys, topics, tags);
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("生成项目额度预警异常",e.getMessage());
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
