package com.topideal.mq.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.GenerateSalePurchaseService;

@Component
public class GenerateSalePurchaseMQ extends ConsumerMQAbstract {

	@Autowired
	private GenerateSalePurchaseService generateSalePurchaseService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(GenerateSalePurchaseMQ.class);

	public GenerateSalePurchaseMQ() {
		super.setTags(MQReportEnum.GENERATE_SALE_PURCHASE.getTags());
		super.setTopics(MQReportEnum.GENERATE_SALE_PURCHASE.getTopic());
	}
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============调用生成购销采销日报数据===========");
		try {
			generateSalePurchaseService.generateSalePurchaseInterface(json, keys, topics, tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("生成购销采销日报数据异常:{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
