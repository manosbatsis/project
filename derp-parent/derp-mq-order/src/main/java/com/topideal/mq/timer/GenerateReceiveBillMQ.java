package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.GenerateReceiveBillService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description: 自动生成应收账单
 * @Author: Chen Yiluan
 * @Date: 2021/06/21 10:08
 **/
@Component
public class GenerateReceiveBillMQ extends ConsumerMQAbstract {

    @Autowired
    private GenerateReceiveBillService generateReceiveBillService;
    /**
     * 打印日志
     */
    public static final Logger LOGGER= LoggerFactory.getLogger(GenerateReceiveBillMQ.class);

    public GenerateReceiveBillMQ(){
        super.setTags(MQOrderEnum.TIMER_GENERATE_RECEIVE_BILL.getTags());
        super.setTopics(MQOrderEnum.TIMER_GENERATE_RECEIVE_BILL.getTopic());
    }


    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("===================自动生成应收账单 开始" + json + "==================");
        try {
            JSONObject jsonData = JSONObject.fromObject(json);

            Integer merchantId = (Integer) jsonData.get("merchantId");// 商家Id
            String month = (String) jsonData.get("month");
            String receiveCodes = (String) jsonData.get("receiveCodes"); //指定开票的应收账单号，多个以英文逗号隔开
            String sourceType = (String) jsonData.get("sourceType"); //0-自动生成应收账单 1-自动开票

            List<MerchantInfoMongo> merchantInfoMongos = generateReceiveBillService.getMerchantList(merchantId);

            if (StringUtils.isBlank(month)) {
                month = TimeUtils.getLastMonth(new Date());
            }

            for (MerchantInfoMongo merchantInfo : merchantInfoMongos) {
                jsonData.put("merchantId", merchantInfo.getMerchantId());
                jsonData.put("merchantName", merchantInfo.getName());
                jsonData.put("month", month);

                List<String> receiveCodeList = new ArrayList<>();

                if (StringUtils.isBlank(sourceType) || "0".equals(sourceType)) {
                    LOGGER.info("===================自动生成应收账单并提交 开始" + json + "==================");
                    receiveCodeList = generateReceiveBillService.saveReceiveBill(jsonData.toString(),keys,topics,tags);
                    LOGGER.info("===================自动生成应收账单并提交 结束==================");
                }

                if (StringUtils.isBlank(sourceType) || "1".equals(sourceType)) {
                    if (receiveCodeList == null || StringUtils.isBlank(receiveCodes)) {
                        continue;
                    }

                    if (receiveCodeList != null) {
                        receiveCodes = StringUtils.join(receiveCodeList.toArray(), ",");
                    }
                    jsonData.put("receiveCodes", receiveCodes);


                    LOGGER.info("===================自动开票 开始" + json + "==================");



                    LOGGER.info("===================自动开票 结束==================");
                }
            }

            LOGGER.info("===================自动生成应收账单 结束==================");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("===================自动生成应收账单 异常" + e.getMessage() + "==================");
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
