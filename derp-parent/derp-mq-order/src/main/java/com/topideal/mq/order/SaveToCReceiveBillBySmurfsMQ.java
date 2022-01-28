package com.topideal.mq.order;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.order.SaveToCReceiveBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/1 16:36
 * @Description: 经分销调用蓝精灵获取Toc结算数据，生成Toc应收账单
 */
@Component
public class SaveToCReceiveBillBySmurfsMQ extends ConsumerMQAbstract {

    public static final Logger LOGGER = LoggerFactory.getLogger(SaveToCReceiveBillBySmurfsMQ.class);

    @Autowired
    private SaveToCReceiveBillService saveToCReceiveBillService;

    public SaveToCReceiveBillBySmurfsMQ(){
        super.setTags(MQOrderEnum.TOC_RECEIVE_BILL_BY_SMURFS_GENERATE.getTags());
        super.setTopics(MQOrderEnum.TOC_RECEIVE_BILL_BY_SMURFS_GENERATE.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        try {
            LOGGER.info("经分销调用蓝精灵生成ToC结算单：json ======================" + json);
            saveToCReceiveBillService.saveToCReceiveBillBySmurfs(json, keys, topics, tags);
        } catch (Exception e) {
            LOGGER.error("经分销调用蓝精灵生成ToC结算单失败", e);
            e.printStackTrace();
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        LOGGER.info("经分销调用蓝精灵生成ToC结算单结束");
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
