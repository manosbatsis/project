package com.topideal.mq.api.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.transfer.DBInWarehouseStatusService;
/**
 * 进仓单状态回推
 * @author yucaifu
 *2018/5/25
 */
@Component
public class DBInWarehouseStatusMQ extends ConsumerMQAbstract {
	/**
	 * 打印日志
	 */
	private Logger LOGGER=LoggerFactory.getLogger(DBInWarehouseStatusMQ.class);
	@Autowired
	private DBInWarehouseStatusService dBInWarehouseStatusService;//进仓单状态回推service
	
	public DBInWarehouseStatusMQ(){
		super.setTopics(MQOrderEnum.EPASS_WAREHOUSE_STATUS_3.getTopic());
		super.setTags(MQOrderEnum.EPASS_WAREHOUSE_STATUS_3.getTags());
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("==========进仓单状态回推========");
		try {
			dBInWarehouseStatusService.saveIntoWarehouseStatusInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("异常,{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
