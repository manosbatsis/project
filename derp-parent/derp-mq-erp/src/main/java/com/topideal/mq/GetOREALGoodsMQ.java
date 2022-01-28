package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.GetOREALGoodsService;

/**
 * 获取欧莱雅供应商商品
 **/
@Component
public class GetOREALGoodsMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(GetOREALGoodsMQ.class);

    @Autowired
    private GetOREALGoodsService getOREALGoodsService;

    public GetOREALGoodsMQ() {
        super.setTags(MQErpEnum.GET_OREAL_GOODS.getTags());
        super.setTopics(MQErpEnum.GET_OREAL_GOODS.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("----获取欧莱雅供应商商品开始----");
        try {

        	getOREALGoodsService.saveOREALGoods(json);
        } catch(Exception e) {
            LOGGER.error("获取欧莱雅供应商商品异常:{}", e);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
