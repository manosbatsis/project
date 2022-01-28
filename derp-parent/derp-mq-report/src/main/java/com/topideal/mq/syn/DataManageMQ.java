package com.topideal.mq.syn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.DataManageService;

/**
 * 数据处理
 */
@Component
public class DataManageMQ extends ConsumerMQAbstract {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DataManageMQ.class);

	@Autowired
	private DataManageService dataManageService;
	

	public DataManageMQ() {
		super.setTopics(MQReportEnum.BU_DARA_MANAGE.getTopic());
		super.setTags(MQReportEnum.BU_DARA_MANAGE.getTags());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("============= 生成事业部利息经销存数据 =================>");
		try {
			//（维护利息经销数据 已经用完可以注释）
			//dataManageService.saveDataManageReport(json, keys, topics, tags);
		LOGGER.info("=============生成事业部利息经销存数据  =================>");	
		} catch (Exception e) {
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
}
