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
 * 删除月结明细 MQ
 */
@Component
public class DelMonthlyAccountItemMQ extends ConsumerMQAbstract {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DelMonthlyAccountItemMQ.class);

	@Autowired
	private DelOrSyncService delOrSyncService;// 删除/同步数据分发接口
	

	public DelMonthlyAccountItemMQ() {
		super.setTopics(MQReportEnum.DEL_MONTHLY_ACCOUNT_ITEM.getTopic());
		super.setTags(MQReportEnum.DEL_MONTHLY_ACCOUNT_ITEM.getTags());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("============= 删除月结明细  =================>");
		try {
			delOrSyncService.getDelOrSync(json, keys, topics, tags);
		LOGGER.info("=============删除月结明细结束  =================>");	
		} catch (Exception e) {
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
}
