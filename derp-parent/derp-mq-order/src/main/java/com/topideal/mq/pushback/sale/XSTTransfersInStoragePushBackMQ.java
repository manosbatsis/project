package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.XSTTransfersInStoragePushBackService;
/**
 *销售调拨入库库存加减成功回推
 * 2019/02/27
 * 杨创
 */
@Component
public class XSTTransfersInStoragePushBackMQ extends ConsumerMQAbstract {
	/*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(XSTTransfersInStoragePushBackMQ.class);
    @Autowired
    private XSTTransfersInStoragePushBackService xSTTransfersInStoragePushBackService;// 调拨入库service
    
    public  XSTTransfersInStoragePushBackMQ() {
		super.setTags(MQPushBackOrderEnum.TRANSFER_IN_STIRAGE_PUSH_BACK_2.getTags());
		super.setTopics(MQPushBackOrderEnum.TRANSFER_IN_STIRAGE_PUSH_BACK_2.getTopic());
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("====================销售调拨出库库存加减成功回推===================");
		try {
			xSTTransfersInStoragePushBackService.updateXSTTransfersInStoragePushBack(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("销售调拨出库库存加减成功回推异常", e.getMessage());
			return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
