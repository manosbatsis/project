package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.YunJiReturnDetailPushBackBackService;
/**
 *云集退货爬虫明细库存加减成功回推
 * 2019/02/27
 * 杨创
 */
@Component
public class YunJiReturnDetailPushBackMQ extends ConsumerMQAbstract {
	/*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(YunJiReturnDetailPushBackMQ.class);
    @Autowired
    private YunJiReturnDetailPushBackBackService yunJiReturnDetailPushBackBackService;// 调拨入库service
    
    public  YunJiReturnDetailPushBackMQ() {
		super.setTags(MQPushBackOrderEnum.CRAWLER_YUNJI_RETURN_PUSH_BACK.getTags());
		super.setTopics(MQPushBackOrderEnum.CRAWLER_YUNJI_RETURN_PUSH_BACK.getTopic());
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("====================云集退货爬虫明细库存加减成功回推===================");
		try {
			yunJiReturnDetailPushBackBackService.updateYunJiReturnDetailPushBack(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("云集退货爬虫明细库存加减成功回推异常", e.getMessage());
			return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
