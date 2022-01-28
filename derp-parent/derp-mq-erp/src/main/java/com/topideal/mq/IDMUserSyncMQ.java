package com.topideal.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 从IDM同步用户-定时器
 * @author 
 */
@Component
public class IDMUserSyncMQ extends ConsumerMQAbstract {

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(IDMUserSyncMQ.class);

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private RMQProducer rocketMQProducer;

	public IDMUserSyncMQ() {
		super.setTags(MQErpEnum.USER_SYN_IDM.getTags());
		super.setTopics(MQErpEnum.USER_SYN_IDM.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============从IDM同步用户开始json: ==========="+json);
		try{
			userInfoService.synsUserInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("从IDM同步用户异常:{}", e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		LOGGER.info("=============从IDM同步用户结束: ===========");
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}


}
