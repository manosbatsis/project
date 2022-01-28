package com.topideal.mq.report;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.GenerateOrderStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 首页—统计电商订单销售总量
 * @Date: 2019/12/24 11:38
 **/
@Component
public class GenerateOrderStatisticsMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(GenerateOrderStatisticsMQ.class);

    @Autowired
    private GenerateOrderStatisticsService generateOrderStatisticsService;

    public GenerateOrderStatisticsMQ() {
        super.setTags(MQReportEnum.ORDER_STATISTICS_DAILY.getTags());
        super.setTopics(MQReportEnum.ORDER_STATISTICS_DAILY.getTopic());
    }


    @Override
    public ConsumeConcurrentlyStatus execute(String json, String key, String topics, String tags) {
        LOGGER.info("=============根据店铺类型统计电商订单数据===========");
        try {
            generateOrderStatisticsService.generateOrderStatisticsByShopType(json);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据店铺类型统计电商订单数据异常:{}", e.getMessage());
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
