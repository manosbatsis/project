package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SaveToBTempReceiveBillService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description: 生成ToB暂估核销信息
 * @Author: Chen Yiluan
 * @Date: 2021/04/23 17:59
 **/
@Component
public class SaveToBTempReceiveBillMQ extends ConsumerMQAbstract {

    /* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveToBTempReceiveBillMQ.class);

    public SaveToBTempReceiveBillMQ() {
        super.setTopics(MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic());
        super.setTags(MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());
    }

    @Autowired
    private SaveToBTempReceiveBillService saveToBTempReceiveBillService;

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================生成tob暂估核销数据、应收核销tob暂估 开始=================="+json);
        synchronized (this) {
            try {
                Thread.sleep(5000);
                JSONObject jsonObj = JSONObject.fromObject(json);
                String sourceType = (String) jsonObj.get("sourceType");// 1-生成tob暂估 2-应收核销tob暂估
                String dataSource = (String) jsonObj.get("dataSource");// 1-上架单 2-类型为“退货退款”的购销销售退货订单

                if(StringUtils.isBlank(sourceType) || "1".equals(sourceType)){
                    LOGGER.info("===================生成tob暂估核销数据 开始==================" + json);

                    saveToBTempReceiveBillService.saveToBTempReceiveBill(json, keys, topics, tags);
                }

                if(StringUtils.isBlank(sourceType) || "2".equals(sourceType)){
                    if (StringUtils.isBlank(dataSource) || "1".equals(dataSource)) {
                        LOGGER.info("===================应收核销tob暂估 开始=================="+json);
                        saveToBTempReceiveBillService.updateVerifyToBTempBill(json, keys, topics, tags);
                    }
                }

                LOGGER.info("===================生成tob暂估核销数据、应收核销tob暂估 结束==================");
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("生成tob暂估核销数据、应收核销tob暂估异常",e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

    }


}
