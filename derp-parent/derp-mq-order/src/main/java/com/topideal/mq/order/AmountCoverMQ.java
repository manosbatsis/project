package com.topideal.mq.order;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.order.AmountCoverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 电商订单金额更新导入覆盖
 * @author tsh
 */
@Component
public class AmountCoverMQ extends ConsumerMQAbstract {
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(AmountCoverMQ.class);

	@Autowired
	private AmountCoverService amountCoverService;

	public AmountCoverMQ() {
		super.setTags(MQOrderEnum.AMOUNT_COVER.getTags());
		super.setTopics(MQOrderEnum.AMOUNT_COVER.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("电商订单金额更新==========开始");
		try {
			amountCoverService.synsAmountCover(json, keys, topics, tags);
		} catch (Exception e) {
			LOGGER.info("电商订单金额更新==========失败");
			e.printStackTrace();
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		LOGGER.info("电商订单金额更新==========结束");
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
