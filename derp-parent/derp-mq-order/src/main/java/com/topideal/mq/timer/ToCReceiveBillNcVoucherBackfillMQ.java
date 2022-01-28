package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.ReceiveBillNcVoucherBackfillService;
import com.topideal.service.timer.ToCReceiveBillNcVoucherBackfillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时器循环调NC4.14凭证查询接口获取ToC应收账单凭证
 * @author gy
 *
 */
@Component
public class ToCReceiveBillNcVoucherBackfillMQ extends ConsumerMQAbstract{

	public static final Logger LOGGER= LoggerFactory.getLogger(ToCReceiveBillNcVoucherBackfillMQ.class);

	@Autowired
	private ToCReceiveBillNcVoucherBackfillService toCReceiveBillNcVoucherBackfillService ;

	public ToCReceiveBillNcVoucherBackfillMQ(){
		super.setTags(MQOrderEnum.TIMER_TOC_RECEVICE_BILL_VOUCHER_BACKFILL.getTags());
		super.setTopics(MQOrderEnum.TIMER_TOC_RECEVICE_BILL_VOUCHER_BACKFILL.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================toc NC凭证查询==================");
		try {
			// 获取应收账单凭证
			toCReceiveBillNcVoucherBackfillService.saveReceiveBillNcVoucherBackfill(json,keys,topics,tags);
			LOGGER.info("===================toc NC凭证查询结束==================");
			
		} catch (Exception e) {
			LOGGER.error("toc NC凭证查询异常",e.getMessage());
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
