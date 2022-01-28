package com.topideal.mq.order;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.order.MoveDataToHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 迁移数据到历史表
 * @author 
 */
@Component
public class MoveDataToHistoryMQ extends ConsumerMQAbstract {
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(MoveDataToHistoryMQ.class);

	@Autowired
	private MoveDataToHistoryService moveDataToHistoryService;

	public MoveDataToHistoryMQ() {
		super.setTags(MQOrderEnum.ORDERDATA_MOVETOHISTORY.getTags());
		super.setTopics(MQOrderEnum.ORDERDATA_MOVETOHISTORY.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		synchronized(this) {
			LOGGER.info("迁移数据到历史表==========开始");
			try {
				moveDataToHistoryService.synsMoveToHistory(json, keys, topics, tags);
			} catch (Exception e) {
				LOGGER.info("迁移数据到历史表==========失败");
				e.printStackTrace();
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
			LOGGER.info("迁移数据到历史表==========结束");
		}

		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
