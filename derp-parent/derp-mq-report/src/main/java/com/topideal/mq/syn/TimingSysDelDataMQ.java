package com.topideal.mq.syn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.SysDelDataService;

/**
 * 定时器删除数据 MQ
 * @author 
 */
@Component
public class TimingSysDelDataMQ extends ConsumerMQAbstract {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TimingSysDelDataMQ.class);

	@Autowired
	private SysDelDataService sysDelDataService;
	

	public TimingSysDelDataMQ() {
		super.setTopics(MQReportEnum.TIMING_SYS_DEL_DATA.getTopic());
		super.setTags(MQReportEnum.TIMING_SYS_DEL_DATA.getTags());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============删除历史快照数据-报表  =================>");
		try {
			sysDelDataService.delData(json, keys, topics, tags);
		LOGGER.info("=============删除历史快照数据-报表  =================>");	
		} catch (Exception e) {
			e.getMessage();
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
}
