package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.FetchingYunJiInventoryService;

@Component
public class FetchingYunJiInventoryDataMQ extends ConsumerMQAbstract {

	@Autowired
	private FetchingYunJiInventoryService fetchingYunJiInventoryService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(FetchingYunJiInventoryDataMQ.class);

	public FetchingYunJiInventoryDataMQ() {
		super.setTags(MQOrderEnum.TIMER_YUNJI_INVENTORY_DATA.getTags());
		super.setTopics(MQOrderEnum.TIMER_YUNJI_INVENTORY_DATA.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============生成云集爬虫数据开始===========");
		synchronized(this){
			try {
				fetchingYunJiInventoryService.fetchingData(json, keys, topics, tags);
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("异常:{}", e.getMessage());
				return ConsumeConcurrentlyStatus.RECONSUME_LATER;
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
