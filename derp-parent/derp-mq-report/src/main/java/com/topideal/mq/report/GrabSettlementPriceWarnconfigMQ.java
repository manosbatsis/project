package com.topideal.mq.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.GrabSettlementPriceWarnconfigService;
/**
 * 标准成本单价波动预警MQ
 */
@Component
public class GrabSettlementPriceWarnconfigMQ extends ConsumerMQAbstract{
	
	@Autowired
	private GrabSettlementPriceWarnconfigService settlementPriceWarnconfigService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(GrabSettlementPriceWarnconfigMQ.class);
	
	public GrabSettlementPriceWarnconfigMQ(){
		super.setTags(MQReportEnum.TIMER_DERP_PURCHASE_IDEPOT_ORDER.getTags());
		super.setTopics(MQReportEnum.TIMER_DERP_PURCHASE_IDEPOT_ORDER.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================抓取经分销采购入库单入库仓库为保税仓、海外仓的入库数据==================");
		try {
			//抓取经分销采购入库单入库仓库为保税仓、海外仓的入库数据
			settlementPriceWarnconfigService.saveGrabDerpPurchaseIdepotOrder(json, keys, topics, tags);
			
			
		} catch (Exception e) {
			LOGGER.error("抓取经分销采购入库单入库仓库为保税仓、海外仓的入库数据, 异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
