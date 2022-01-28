package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.XSTConsumerInStoragePushBackService;
/**
 * 消费者销售退入库库存加成功回推
 * 2019/07/25
 * 文艳
 */
@Component
public class XSTConsumerInStoragePushBackMQ extends ConsumerMQAbstract {
	/*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(XSTConsumerInStoragePushBackMQ.class);
    @Autowired
    private XSTConsumerInStoragePushBackService xSTConsumerInStoragePushBackService;// 销售消费者退货退入库service
    
    public  XSTConsumerInStoragePushBackMQ() {
		super.setTags(MQPushBackOrderEnum.SALE_ORDER_STOCK_IN_PUSH_BACK.getTags());
		super.setTopics(MQPushBackOrderEnum.SALE_ORDER_STOCK_IN_PUSH_BACK.getTopic());
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("====================销售消费者退货退入库库存加减成功回推===================");
		try {
			xSTConsumerInStoragePushBackService.updateXSTConsumerInStoragePushBack(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("销售消费者退货退入库库存加减成功回推异常", e.getMessage());
			return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
