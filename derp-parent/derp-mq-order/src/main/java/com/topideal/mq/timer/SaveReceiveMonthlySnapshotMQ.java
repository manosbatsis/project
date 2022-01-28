package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SaveReceiveMonthlySnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 生成应收月结快照MQ
 * @Author: Chen Yiluan
 **/
@Component
public class SaveReceiveMonthlySnapshotMQ extends ConsumerMQAbstract {

    /* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveReceiveMonthlySnapshotMQ.class);

    public SaveReceiveMonthlySnapshotMQ() {
        super.setTopics(MQOrderEnum.TIMER_GENERATE_MONTHLY_SNAPSHOT.getTopic());
        super.setTags(MQOrderEnum.TIMER_GENERATE_MONTHLY_SNAPSHOT.getTags());
    }

    @Autowired
    private SaveReceiveMonthlySnapshotService saveReceiveMonthlySnapshotService;

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================生成应收月结快照MQ 开始==================" + json);
        synchronized (this) {
            try {
                saveReceiveMonthlySnapshotService.SaveReceiveMonthlySnapshot(json, keys, topics, tags);
                LOGGER.info("===================生成应收月结快照MQ 结束==================");
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("生成应收月结快照MQ异常", e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

    }


}
