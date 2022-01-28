package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.XSTSaleReturnIdepotPushBackService;
/**
 * 消费者销售退上架入库加成功回推
 * 2020/10/25
 * 杨创
 */
@Component
public class XSTSaleReturnIdepotPushBackMQ extends ConsumerMQAbstract {
	/*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(XSTSaleReturnIdepotPushBackMQ.class);
    @Autowired
    private XSTSaleReturnIdepotPushBackService xSTSaleReturnIdepotPushBackService;// 退货退入库service
    
    public  XSTSaleReturnIdepotPushBackMQ() {
		super.setTags(MQPushBackOrderEnum.SALER_ETURN_IDEPOT_IN_PUSH_BACK.getTags());
		super.setTopics(MQPushBackOrderEnum.SALER_ETURN_IDEPOT_IN_PUSH_BACK.getTopic());
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("====================销售退货上架入库库存加减成功回推===================");
		try {
			xSTSaleReturnIdepotPushBackService.updateXSTSaleReturnIdepotPushBack(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("销售退货上架入库库存加减成功回推异常", e.getMessage());
			return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
