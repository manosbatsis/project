package com.topideal.mq.api.bill;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.bill.OAExamineResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * OA 审批结果推送接口
 */
@Component
public class OAExamineResultMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(OAExamineResultMQ.class);

    @Autowired
    private OAExamineResultService oaExamineResultService;

    public OAExamineResultMQ(){
        super.setTopics(MQOrderEnum.OAEXAMINE_ACOUNT_RESULT.getTopic());
        super.setTags(MQOrderEnum.OAEXAMINE_ACOUNT_RESULT.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("====================OA审批结果推送接口==================="+json);
        try {
            oaExamineResultService.updateExamineResult(json,keys,topics,tags);
            LOGGER.info("===================OA审批结果修改状态结束==================");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("OA账单审批日志，异常", e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
