package com.topideal.mq.api.bill;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.api.transfer.DBFirstTallyMQ;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.bill.OABillExamineLogService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * OA 账单审核过程日志
 */
@Component
public class OABillExamineLogMQ  extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(OABillExamineLogMQ.class);

    @Autowired
    private OABillExamineLogService oABillExamineLogService;

    public OABillExamineLogMQ(){
        super.setTopics(MQOrderEnum.OAEXAMINE_ACOUNT_LOG.getTopic());
        super.setTags(MQOrderEnum.OAEXAMINE_ACOUNT_LOG.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("====================OA账单审批日志记录开始==================="+json);
        try {
            oABillExamineLogService.insertExamineLog(json,keys,topics,tags);

            LOGGER.info("===================OA账单审批日志记录结束==================");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("OA账单审批日志，异常", e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
