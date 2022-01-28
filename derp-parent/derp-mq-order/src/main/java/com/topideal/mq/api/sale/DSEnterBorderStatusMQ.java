package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.DSEnterBorderStatusService;

/**
 * 进境状态回推MQ
 */
@Component
public class DSEnterBorderStatusMQ extends ConsumerMQAbstract {
	@Autowired
	public DSEnterBorderStatusService enterBorderStatusService;
	/**
	 * 打印日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DSEnterBorderStatusMQ.class);
	
	public DSEnterBorderStatusMQ() {
		super.setTags(MQOrderEnum.EPASS_ENTER_BORDER_STATUS_2.getTags());
		super.setTopics(MQOrderEnum.EPASS_ENTER_BORDER_STATUS_2.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("==================进境状态回推接口====================");
		try {
			enterBorderStatusService.saveEnterBorderStatusInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("进境状态回推接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
