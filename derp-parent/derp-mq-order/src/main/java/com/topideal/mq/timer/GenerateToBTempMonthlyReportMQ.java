package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.GenerateToBTempMonthlyReportService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 生成Tob暂估月结报表
 * @Author: Chen Yiluan
 **/
@Component
public class GenerateToBTempMonthlyReportMQ extends ConsumerMQAbstract {

    /* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateToBTempMonthlyReportMQ.class);

    public GenerateToBTempMonthlyReportMQ() {
        super.setTopics(MQOrderEnum.TIMER_GENERATE_TOB_TEMP_MONTHLY_REPORT.getTopic());
        super.setTags(MQOrderEnum.TIMER_GENERATE_TOB_TEMP_MONTHLY_REPORT.getTags());

    }

    @Autowired
    private GenerateToBTempMonthlyReportService generateToBTempMonthlyReportService;

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================生成Tob暂估月结报表 开始=================="+json);
        synchronized (this) {
            try {
                JSONObject jsonObj = JSONObject.fromObject(json);
                String sourceType = (String) jsonObj.get("sourceType");// 1-生成tob暂估收入月结快照 2-生成tob暂估费用月结快照

                if(StringUtils.isBlank(sourceType) || "1".equals(sourceType)){
                    LOGGER.info("===================生成tob暂估收入月结快照 开始==================" + json);

                    generateToBTempMonthlyReportService.Save2BRevenueMonthly(json, keys, topics, tags);
                }

                if(StringUtils.isBlank(sourceType) || "2".equals(sourceType)){
                    LOGGER.info("===================生成tob暂估费用月结快照 开始=================="+json);
                    generateToBTempMonthlyReportService.Save2BCostMonthly(json, keys, topics, tags);
                }

                LOGGER.info("===================生成tob暂估收入月结快照 结束==================");
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("生成tob暂估收入月结快照异常",e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
