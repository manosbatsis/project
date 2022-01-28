package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.XSTIntoWarehouseStatusPushBackService;

/**
 * 进仓单状态回推库存加减成功回推 mq
 * 2019/02/27
 * 杨创
 */
@Component
public class XSTIntoWarehouseStatusPushBackMQ extends ConsumerMQAbstract {
	/**
	 * 打印日志
	 */
	private Logger logger=LoggerFactory.getLogger(XSTIntoWarehouseStatusPushBackMQ.class);
	@Autowired
	private XSTIntoWarehouseStatusPushBackService xSTIntoWarehouseStatusPushBackService;//进仓单状态回推service
	
	public XSTIntoWarehouseStatusPushBackMQ(){
		super.setTopics(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK_2.getTopic());
		super.setTags(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK_2.getTags());
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		logger.info("==========进仓单状态回推库存加减成功回推========");
		try {
			xSTIntoWarehouseStatusPushBackService.updatexSTIntoWarehouseStatusPushBackInfo(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("进仓单状态回推库存加减成功回推,{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
