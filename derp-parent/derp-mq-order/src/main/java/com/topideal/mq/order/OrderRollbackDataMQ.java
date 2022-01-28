package com.topideal.mq.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.order.OrderRollbackDataService;

/**
 * 业务库存回滚数据
 * @author 
 */
@Component
public class OrderRollbackDataMQ extends ConsumerMQAbstract {
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(OrderRollbackDataMQ.class);

	@Autowired
	private OrderRollbackDataService orderRollbackDataService;

	public OrderRollbackDataMQ() {
		super.setTags(MQOrderEnum.ORDERDATA_ROLLBACK.getTags());
		super.setTopics(MQOrderEnum.ORDERDATA_ROLLBACK.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		synchronized(this) {
			LOGGER.info("业务单据回滚==========开始");
			try {
				orderRollbackDataService.synsOrderRollbackData(json, keys, topics, tags);
			} catch (Exception e) {
				LOGGER.info("业务单据回滚==========失败");
				e.printStackTrace();
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
			LOGGER.info("业务单据回滚==========结束");
		}

		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
