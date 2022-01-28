package com.topideal.mq.syn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.common.DelOrSyncService;

/**
 * 同步数据 MQ
 * @author zhanghx
 */
@Component
public class SysDataMQ extends ConsumerMQAbstract {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SysDataMQ.class);

	@Autowired
	private DelOrSyncService delOrSyncService;// 删除/同步数据分发接口
	

	public SysDataMQ() {
		super.setTopics(MQReportEnum.SYS_DATA.getTopic());
		super.setTags(MQReportEnum.SYS_DATA.getTags());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============同步数据  =================>");
		//锁住代码块
		try {
			delOrSyncService.getDelOrSync(json, keys, topics, tags);
		LOGGER.info("=============同步数据结束  =================>");	
		} catch (Exception e) {
			LOGGER.error("同步数据异常:"+e.getMessage(), e);	
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
}
