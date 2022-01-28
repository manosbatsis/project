package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.PlatformPurNotShalfMessageService;
/**
 * 平台采购单发送消息MQ
 */
@Component
public class PlatformPurNotShalfMessageMQ extends ConsumerMQAbstract{
	
	@Autowired
	private PlatformPurNotShalfMessageService platformPurNotShalfMessageService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(PlatformPurNotShalfMessageMQ.class);
	
	public PlatformPurNotShalfMessageMQ(){
		super.setTags(MQOrderEnum.TIMER_PLATFORM_PUR_NOTSHALF_MESSAGE.getTags());
		super.setTopics(MQOrderEnum.TIMER_PLATFORM_PUR_NOTSHALF_MESSAGE.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================平台采购单未上架邮件提醒开始==================");
		synchronized(this){
			try {
				platformPurNotShalfMessageService.sendPurNotShalfMessage(json,keys,topics,tags);
				LOGGER.info("===================平台采购单未上架邮件提醒结束==================");
			} catch (Exception e) {
				LOGGER.error("平台采购单未上架邮件提醒异常",e);
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
