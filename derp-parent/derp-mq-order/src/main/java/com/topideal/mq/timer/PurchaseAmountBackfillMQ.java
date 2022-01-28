package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.PurchaseAmountBackfillService;
/**
 * 采购本位币金额回填MQ
 */
@Component
public class PurchaseAmountBackfillMQ extends ConsumerMQAbstract{
	
	@Autowired
	private PurchaseAmountBackfillService purchaseAmountBackfillService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(PurchaseAmountBackfillMQ.class);
	
	public PurchaseAmountBackfillMQ(){
		super.setTags(MQOrderEnum.TIMER_PURCHASE_AMOUNT_BACKFILL.getTags());
		super.setTopics(MQOrderEnum.TIMER_PURCHASE_AMOUNT_BACKFILL.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================采购本位币金额回填==================");
		synchronized(this){
			try {
				
				// 采购本位币金额回填
				purchaseAmountBackfillService.savePurchaseAmountBackfill(json,keys,topics,tags);
				//	嘉宝的采购发票同步到宝信
				purchaseAmountBackfillService.savePurchaseInvoiceInfo(json,keys,topics,tags);
				
				LOGGER.info("===================采购本位币金额结束==================");
				
			} catch (Exception e) {
				LOGGER.error("采购本位币金额回填异常",e.getMessage());
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
