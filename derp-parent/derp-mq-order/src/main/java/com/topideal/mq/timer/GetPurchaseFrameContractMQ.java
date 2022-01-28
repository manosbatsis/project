package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.GetPurchaseDateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/23 15:59
 * @Description: 触发条件：定时器任务，频率，一天两次，中午休息和晚上各一次，查询当前日期T+7天的数据
 * 从OA获取采购框架合同   OA(5.2 框架合同同步接口)
 */
@Component
public class GetPurchaseFrameContractMQ extends ConsumerMQAbstract {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetPurchaseFrameContractMQ.class);

    @Autowired
    private GetPurchaseDateService service;

    public GetPurchaseFrameContractMQ(){
        super.setTags(MQOrderEnum.GET_PURCHASE_FRAME_CONTRACT.getTags());
        super.setTopics(MQOrderEnum.GET_PURCHASE_FRAME_CONTRACT.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        synchronized (this) {
            try {
                LOGGER.info("===================从OA获取采购框架合同MQ 开始=================="+json);
                service.getPurchaseFrameContractFromOA(json, keys, topics, tags);
                LOGGER.info("===================从OA获取采购框架合同MQ 结束================" + json);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("从OA获取采购框架合同异常",e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
