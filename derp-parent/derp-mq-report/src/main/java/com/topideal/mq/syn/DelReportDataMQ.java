package com.topideal.mq.syn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.DelReportDataService;
import com.topideal.service.api.common.DelOrSyncService;

/**
 * 删除报表数据 
 */
@Component
public class DelReportDataMQ extends ConsumerMQAbstract {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DelReportDataMQ.class);

	@Autowired
	private DelOrSyncService delOrSyncService;// 删除/同步数据分发接口
	

	public DelReportDataMQ() {
		super.setTopics(MQReportEnum.DEL_REPORT_DATAS.getTopic());
		super.setTags(MQReportEnum.DEL_REPORT_DATAS.getTags());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("============= 删除报表数据开始  =================>");
		try {
			delOrSyncService.getDelOrSync(json, keys, topics, tags);
		LOGGER.info("=============删除报表数据结束  =================>");	
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
}
