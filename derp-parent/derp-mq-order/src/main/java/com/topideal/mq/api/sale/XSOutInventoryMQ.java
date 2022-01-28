package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.XSOutInventoryService;

/**
 * 出库明细回推MQ
 */
@Component
public class XSOutInventoryMQ extends ConsumerMQAbstract{
	@Autowired
	private XSOutInventoryService xSOutDepotService;
	
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER =LoggerFactory.getLogger(XSOutInventoryMQ.class);
	
	public XSOutInventoryMQ(){
		super.setTags(MQOrderEnum.EPASS_OUT_INVENTORY_2.getTags());
		super.setTopics(MQOrderEnum.EPASS_OUT_INVENTORY_2.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		
		LOGGER.info("================ 生成出库单 出库明细回推接口==================");
		
		try {
			xSOutDepotService.saveOutDepotInfo(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(" 生成出库单 出库明细回推接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
