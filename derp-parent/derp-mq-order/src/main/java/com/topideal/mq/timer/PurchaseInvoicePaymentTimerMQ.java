package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.PurchaseInvoicePaymentSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 采购、发票预计付款增加定时器提醒
 * @author huangrenya
 **/
@Component
public class PurchaseInvoicePaymentTimerMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger LOGGER= LoggerFactory.getLogger(PurchaseInvoicePaymentTimerMQ.class);

    public PurchaseInvoicePaymentTimerMQ() {
        super.setTags(MQOrderEnum.TIMER_PURCHASE_INVOICE_PAYMENTDATE_ORDER.getTags());
        super.setTopics(MQOrderEnum.TIMER_PURCHASE_INVOICE_PAYMENTDATE_ORDER.getTopic());
    }

    @Autowired
    private PurchaseInvoicePaymentSerivce purchaseInvoicePaymentSerivce;

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================采购单、发票单预计付款提醒邮件开始=================");
        try {

            purchaseInvoicePaymentSerivce.savePurchaseInvoicePayment(json,keys,topics,tags);

            LOGGER.info("===================采购单、发票单预计付款提醒邮件结束==================");
        } catch (Exception e) {
            LOGGER.error("采购单、发票单预计付款提醒邮件报错信息",e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
