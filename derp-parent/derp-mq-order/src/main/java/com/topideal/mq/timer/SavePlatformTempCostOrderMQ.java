package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SavePlatformTempCostOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生成平台暂估费用单
 */
@Component
public class SavePlatformTempCostOrderMQ extends ConsumerMQAbstract{

	@Autowired
	private SavePlatformTempCostOrderService savePlatformTempCostOrderService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(SavePlatformTempCostOrderMQ.class);

	public SavePlatformTempCostOrderMQ(){
		super.setTags(MQOrderEnum.TIMER_PLATFORM_TEMPORARY_COST_ORDER.getTags());
		super.setTopics(MQOrderEnum.TIMER_PLATFORM_TEMPORARY_COST_ORDER.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================生成平台暂估费用单MQ=================="+json);
		long  startTime = System.currentTimeMillis();
		synchronized (this) {
			try {
				savePlatformTempCostOrderService.savePlatformTempCostOrder(json, keys, topics, tags);

			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("生成平台暂估费用单MQ异常",e);
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("===================生成平台暂估费用单MQ 结束, 耗费了:" +  (endTime - startTime) + " ms" + "==================");
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

	}

}
