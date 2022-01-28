package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.ReceiveCloseAccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/18 15:29
 * @Description: 生成应收关账数据
 */
@Component
public class ReceiveCloseAccountsMQ extends ConsumerMQAbstract {

    public static final Logger LOGGER= LoggerFactory.getLogger(ReceiveCloseAccountsMQ.class);

    @Autowired
    private ReceiveCloseAccountsService receiveCloseAccountsService;

    public ReceiveCloseAccountsMQ(){
        super.setTags(MQOrderEnum.TIMER_GENERATE_RECEIVE_CLOSE_ACCOUNT.getTags());
        super.setTopics(MQOrderEnum.TIMER_GENERATE_RECEIVE_CLOSE_ACCOUNT.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===================生成应收关账数据MQ 开始=================="+json);
        synchronized (this) {
            try {
                receiveCloseAccountsService.saveReceiveCloseAccounts(json, keys, topics, tags);
                LOGGER.info("===================生成应收关账数据MQ 结束==================");
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("生成应收关数据异常",e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
