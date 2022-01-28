package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.ReceiveBillNcVerifyBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时器循环调NC4.12核销记录查询接口获取核销明细
 *
 * @author gy
 */
@Component
public class ReceiveBillNcVerifyBackMQ extends ConsumerMQAbstract {

	public static final Logger LOGGER = LoggerFactory.getLogger(ReceiveBillNcVerifyBackMQ.class);

	@Autowired
	private ReceiveBillNcVerifyBackService receiveBillNcVerifyBackService;

	public ReceiveBillNcVerifyBackMQ() {
		super.setTags(MQOrderEnum.TIMER_RECEICE_BILL_NC_VERIFY_BACK.getTags());
		super.setTopics(MQOrderEnum.TIMER_RECEICE_BILL_NC_VERIFY_BACK.getTopic());
	}


	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("===================NC核销明细查询==================");
		try {
			synchronized (this) {
				receiveBillNcVerifyBackService.saveReceiveVerifyItem(json, keys, topics, tags);
				LOGGER.info("===================NC核销明细查询结束==================");
			}


		} catch (Exception e) {
			LOGGER.error("NC核销明细查询异常", e.getMessage());
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
