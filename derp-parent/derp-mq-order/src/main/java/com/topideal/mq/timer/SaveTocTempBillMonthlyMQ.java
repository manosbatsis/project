package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.order.SaveTocTempBillMonthlyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/18 15:29
 * @Description: 生成toc暂估月结数据
 */
@Component
public class SaveTocTempBillMonthlyMQ extends ConsumerMQAbstract {

    public static final Logger LOGGER= LoggerFactory.getLogger(SaveTocTempBillMonthlyMQ.class);

    @Autowired
    private SaveTocTempBillMonthlyService saveTocTempBillMonthlyService;

    public SaveTocTempBillMonthlyMQ(){
        super.setTags(MQOrderEnum.TIMER_GENERATE_TOC_TEMP_MONTHLY_SNAPSHOT.getTags());
        super.setTopics(MQOrderEnum.TIMER_GENERATE_TOC_TEMP_MONTHLY_SNAPSHOT.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===================生成toc暂估月结数据MQ 开始=================="+json);
        synchronized (this) {
            try {
                saveTocTempBillMonthlyService.SaveToCTempMonthly(json, keys, topics, tags);
                LOGGER.info("===================生成toc暂估月结数据MQ 结束================" + json);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("生成toc暂估月结数据异常",e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
