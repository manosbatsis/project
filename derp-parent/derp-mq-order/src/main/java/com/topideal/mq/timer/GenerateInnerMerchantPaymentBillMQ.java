package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.GenerateInnerMerchantPaymentBillService;
/**
 * 生成内部公司应付账单
 *
 */
@Component
public class GenerateInnerMerchantPaymentBillMQ  extends ConsumerMQAbstract {
	public static final Logger LOGGER= LoggerFactory.getLogger(GenerateInnerMerchantPaymentBillMQ.class);

    @Autowired
    private GenerateInnerMerchantPaymentBillService generateInnerMerchantPaymentBillService;

    public GenerateInnerMerchantPaymentBillMQ(){
        super.setTags(MQOrderEnum.TIMER_GENERATE_INNER_MERCHANT_PAYMENT_BILL.getTags());
        super.setTopics(MQOrderEnum.TIMER_GENERATE_INNER_MERCHANT_PAYMENT_BILL.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================生成内部公司应付账单开始==================");
        try {
            generateInnerMerchantPaymentBillService.generateInnerMerchantPaymentBill(json,keys,topics,tags);
            LOGGER.info("===================生成内部公司应付账单结束==================");

        } catch (Exception e) {
            LOGGER.error("生成内部公司应付账单异常",e.getMessage());
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
