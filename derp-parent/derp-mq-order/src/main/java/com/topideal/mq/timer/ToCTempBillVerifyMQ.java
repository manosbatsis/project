package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.ToCTempBillVerifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * toc暂估应收账单核销toc结算单
 */
@Component
public class ToCTempBillVerifyMQ extends ConsumerMQAbstract{

	@Autowired
	private ToCTempBillVerifyService toCTempBillVerifyService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(ToCTempBillVerifyMQ.class);

	public ToCTempBillVerifyMQ(){
		super.setTags(MQOrderEnum.TOC_TEMP_RECEIVE_BILL_VERIFY.getTags());
		super.setTopics(MQOrderEnum.TOC_TEMP_RECEIVE_BILL_VERIFY.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================toc暂估应收账单核销toc结算单MQ 开始=================="+json);
		try {
			toCTempBillVerifyService.updateVerifyToCTempBill(json, keys, topics, tags);
			LOGGER.info("===================toc暂估应收账单核销toc结算单MQ 结束==================");
		} catch (Exception e) {
			LOGGER.error("toc暂估应收账单核销toc结算单MQ异常",e.getMessage());
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
