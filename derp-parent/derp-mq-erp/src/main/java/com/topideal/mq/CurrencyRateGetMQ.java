package com.topideal.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.CurrencyRateGetService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 获取汇率信息MQ
 **/
@Component
public class CurrencyRateGetMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(CurrencyRateGetMQ.class);

    @Autowired
    private CurrencyRateGetService currencyRateGetService;

    public CurrencyRateGetMQ() {
        super.setTags(MQErpEnum.GET_CURRENCY_RATE.getTags());
        super.setTopics(MQErpEnum.GET_CURRENCY_RATE.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("----【获取汇率信息开始...】----");
        try {

            currencyRateGetService.currencyRateGet(json);
        } catch(Exception e) {
            LOGGER.error("获取汇率信息异常:{}", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
