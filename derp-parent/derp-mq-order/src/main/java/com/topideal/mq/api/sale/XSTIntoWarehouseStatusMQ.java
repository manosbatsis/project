package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.XSTIntoWarehouseStatusService;
/**
 * 进仓单状态回推
 */
@Component
public class XSTIntoWarehouseStatusMQ extends ConsumerMQAbstract {
	/**
	 * 打印日志
	 */
	private Logger LOGGER=LoggerFactory.getLogger(XSTIntoWarehouseStatusMQ.class);
	@Autowired
	private XSTIntoWarehouseStatusService intoWarehouseStatusService;//进仓单状态回推service
	
	public XSTIntoWarehouseStatusMQ(){
		super.setTopics(MQOrderEnum.EPASS_WAREHOUSE_STATUS_2.getTopic());
		super.setTags(MQOrderEnum.EPASS_WAREHOUSE_STATUS_2.getTags());
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("==========进仓单状态回推========");
		try {
			intoWarehouseStatusService.saveIntoWarehouseStatusInfo(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("异常,{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
