package com.topideal.mq.dstp;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.DSTPSynGoodsService;
import com.topideal.service.DSTPSynOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 同步宝信、健太、润佰、卓烨的订单到DSTP
 * @author 
 */
@Component
public class DSTPSynOrderMQ extends ConsumerMQAbstract {

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(DSTPSynOrderMQ.class);

	@Autowired
	private DSTPSynOrderService dsTPSynOrderService;

	public DSTPSynOrderMQ() {
		super.setTags(MQReportEnum.DSTP_SYNORDER.getTags());
		super.setTopics(MQReportEnum.DSTP_SYNORDER.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============同步订单到DSTP开始===========");
		try {

			dsTPSynOrderService.synOrderDSTP(json, keys, topics, tags);
			LOGGER.info("=============同步订单到DSTP结束===========");
		} catch (Exception e) {
			LOGGER.error("异常:{}", e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
