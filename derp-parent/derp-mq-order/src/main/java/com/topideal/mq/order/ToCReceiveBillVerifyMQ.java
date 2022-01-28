package com.topideal.mq.order;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.order.ToCReceiveBillVerifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: toc结算单审核核销toc暂估应收
 * @Author: Chen Yiluan
 * @Date: 2021/01/07 14:54
 **/
@Component
public class ToCReceiveBillVerifyMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger LOGGER= LoggerFactory.getLogger(ToCReceiveBillVerifyMQ.class);

    public ToCReceiveBillVerifyMQ(){
        super.setTags(MQOrderEnum.TOC_RECEIVE_BILL_VERIFY.getTags());
        super.setTopics(MQOrderEnum.TOC_RECEIVE_BILL_VERIFY.getTopic());
    }

    @Autowired
    private ToCReceiveBillVerifyService toCReceiveBillVerifyService;

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================toc结算单审核核销toc暂估应收MQ 开始=================="+json);
        synchronized (this) {
            try {
                toCReceiveBillVerifyService.updateVerifyToCTempBill(json, keys, topics, tags);
                LOGGER.info("===================toc结算单审核核销toc暂估应收MQ 结束==================");
            } catch (Exception e) {
                LOGGER.error("toc结算单审核核销toc暂估应收MQ异常", e);
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

    }

}
