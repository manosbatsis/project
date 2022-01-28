package com.topideal.mq.pushback.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.purchase.OnTheWayPushbackService;

/**
 * 采购 中转仓入库 回推
 * @author zhanghx
 * 2018/7/16
 */
@Component
public class OnTheWayPushbackMQ extends ConsumerMQAbstract {

    @Autowired 
    private OnTheWayPushbackService service;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(OnTheWayPushbackMQ.class);

    public OnTheWayPushbackMQ(){
        super.setTopics(MQPushBackOrderEnum.ON_THE_WAY_PUSH_BACK.getTopic());
        super.setTags(MQPushBackOrderEnum.ON_THE_WAY_PUSH_BACK.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============采购 中转仓入库 回推===========");
        try{
        	/**
        	 * 主线程睡眠5秒，防止采购链路生成销售订单、销售出库单事务未提交。库存已回推
        	 */
        	Thread.sleep(5000L);
        	
        	service.modifyStatus(json, keys, topics, tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e);
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
