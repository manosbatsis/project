package com.topideal.mq.report;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryAnalysisGenerateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 定时器—(销售洞察)库存量分析数据生成
 * @Author: Chen Yiluan
 * @Date: 2020/12/18 14:28
 **/
@Component
public class InventoryAnalysisGenerateMQ extends ConsumerMQAbstract {
    /**
     * 打印日志
     */
    public static final Logger LOGGER= LoggerFactory.getLogger(InventoryAnalysisGenerateMQ.class);

    @Autowired
    private InventoryAnalysisGenerateService inventoryAnalysisGenerateService;

    public InventoryAnalysisGenerateMQ(){
        super.setTags(MQReportEnum.INVENTORY_ANALYSIS_GENERATE.getTags());
        super.setTopics(MQReportEnum.INVENTORY_ANALYSIS_GENERATE.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String key, String topics, String tags) {
        LOGGER.info("=============生成(销售洞察)库存量分析数据===========");
        try {
            inventoryAnalysisGenerateService.saveInventoryAnalysis(json, key, topics, tags);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("生成(销售洞察)库存量分析数据异常:{}", e.getMessage());
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
