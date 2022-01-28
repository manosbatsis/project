package com.topideal.mq.pushback;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackStorageEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.InventoryResultsConfirmPushBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 仓储确认手工导入盘点结果单库存加减成功回推 mq
 * 2020/02/11
 */
@Component
public class InventoryResultsConfirmPushBackMQ extends ConsumerMQAbstract{
	@Autowired
	private InventoryResultsConfirmPushBackService inventoryResultsConfirmPushBackService;

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(InventoryResultsConfirmPushBackMQ.class);

	public InventoryResultsConfirmPushBackMQ(){
		super.setTags(MQPushBackStorageEnum.STORAGE_RESULTS_CONFIRM_PUSH_BACK.getTags());
		super.setTopics(MQPushBackStorageEnum.STORAGE_RESULTS_CONFIRM_PUSH_BACK.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags ) {
		LOGGER.info("===================确认仓储手工导入盘点结果库存加减成功回推 ==================");
		try {
			inventoryResultsConfirmPushBackService.updateinventoryResultsPushBackInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("确认仓储手工导入盘点结果库存加减成功回推异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
