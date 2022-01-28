package com.topideal.mq.order;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.order.SaveToCReceiveBillService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 生成toc结算单mq
 * @Author: Chen Yiluan
 * @Date: 2021/03/08 14:02
 **/
@Component
public class SaveToCReceiveBillMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(SaveToCReceiveBillMQ.class);

    @Autowired
    private SaveToCReceiveBillService saveToCReceiveBillService;

    public SaveToCReceiveBillMQ(){
        super.setTags(MQOrderEnum.TOC_RECEIVE_BILL_GENERATE.getTags());
        super.setTopics(MQOrderEnum.TOC_RECEIVE_BILL_GENERATE.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        try {
            LOGGER.info("生成ToC结算单：json ======================" + json);
            JSONObject jsonData = JSONObject.fromObject(json);
            String billCode = (String) jsonData.get("billCode");
            if (!StringUtils.isNotBlank(billCode)) {
                LOGGER.error("ToC结算单号：" + billCode + "不能为空");
                throw new RuntimeException("ToC结算单号：" + billCode + "不能为空");
            }
            saveToCReceiveBillService.saveToCReceiveBill(json, keys, topics, tags);
        } catch (Exception e) {
            LOGGER.error("生成ToC结算单失败", e);
            e.printStackTrace();
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        LOGGER.info("生成ToC结算单结束");
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
