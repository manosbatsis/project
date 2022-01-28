package com.topideal.mq.syn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.common.ReportSysMongoDataService;

/**
 * 同步报表数据到mongdb
 * @author 杨创
 */
@Component
public class ReportSysMongoDataMQ extends ConsumerMQAbstract {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportSysMongoDataMQ.class);

	@Autowired
	private ReportSysMongoDataService reportSysMongoDataService;//同步报表数据到mongdb
	

	public ReportSysMongoDataMQ() {
		super.setTopics(MQReportEnum.SYS_MONGO_DATA.getTopic());
		super.setTags(MQReportEnum.SYS_MONGO_DATA.getTags());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============同步数据到mongdb  =================>");
		//锁住代码块
		try {
			reportSysMongoDataService.sysMongoData(json, keys, topics, tags);
		LOGGER.info("=============同步数据到mongdb  =================>");	
		} catch (Exception e) {
			LOGGER.error("同步数据到mongdb异常:"+e.getMessage(), e);	
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
}
