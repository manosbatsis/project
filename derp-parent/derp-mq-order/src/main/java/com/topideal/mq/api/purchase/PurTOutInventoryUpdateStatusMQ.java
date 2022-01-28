package com.topideal.mq.api.purchase;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.purchase.PurTOutInventoryUpdateStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 出库明细更新出库状态MQ
 */
@Component
public class PurTOutInventoryUpdateStatusMQ extends ConsumerMQAbstract{
	@Autowired
	private PurTOutInventoryUpdateStatusService service ;

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER =LoggerFactory.getLogger(PurTOutInventoryUpdateStatusMQ.class);

	public PurTOutInventoryUpdateStatusMQ(){
		super.setTags(MQOrderEnum.EPASS_OUT_INVENTORY_1_UPDATE_STATUS.getTags());
		super.setTopics(MQOrderEnum.EPASS_OUT_INVENTORY_1_UPDATE_STATUS.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		
		LOGGER.info("================出库明细更新出库状态接口==================");
		
		try {
			service.updatePurTOutInventoryStatus(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("出库明细更新出库状态接口异常",e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
