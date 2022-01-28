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
 * 从OA获取采购试单申请列表   OA(9.1 立项/试单同步接口)
 */
@Component
public class GetPurchaseTryApplyOrderMQ extends ConsumerMQAbstract {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetPurchaseTryApplyOrderMQ.class);

    @Autowired
    private GetPurchaseDateService service;

    public GetPurchaseTryApplyOrderMQ(){
        super.setTags(MQOrderEnum.GET_PURCHASE_TRY_APPLY_ORDER.getTags());
        super.setTopics(MQOrderEnum.GET_PURCHASE_TRY_APPLY_ORDER.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        synchronized (this) {
            try {
                LOGGER.info("===================从OA获取采购试单申请列表MQ 开始=================="+json);
                service.getPurchaseTryApplyOrderFromOA(json, keys, topics, tags);
                LOGGER.info("===================从OA获取采购试单申请列表MQ 结束================" + json);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("从OA获取采购试单申请列表数据异常",e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
