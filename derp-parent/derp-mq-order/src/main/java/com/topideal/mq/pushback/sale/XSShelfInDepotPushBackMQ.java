package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.XSShelfInDepotPushBackService;
import com.topideal.service.pushback.sale.XSTConsumerInStoragePushBackService;
/**
 * 上架入库库存加成功回推
 * @author wenyan
 *
 */
@Component
public class XSShelfInDepotPushBackMQ extends ConsumerMQAbstract {
	/*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(XSShelfInDepotPushBackMQ.class);
    @Autowired
    private XSShelfInDepotPushBackService xSShelfInDepotPushBackService;// 销售上架入库service
    
    public  XSShelfInDepotPushBackMQ() {
		super.setTags(MQPushBackOrderEnum.SALE_SHELF_IN_DEPOT_PUSH_BACK.getTags());
		super.setTopics(MQPushBackOrderEnum.SALE_SHELF_IN_DEPOT_PUSH_BACK.getTopic());
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("====================销售上架入库库存加减成功回推===================");
		try {
			xSShelfInDepotPushBackService.updateXSShelfInDepotPushPushBack(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("销售上架入库库存加减成功回推异常", e.getMessage());
			return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
