package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.SendSapienceGoodsService;

import net.sf.json.JSONObject;

/**
 * 向卓普信推送商品档案
 **/
@Component
public class SendSapienceGoodsMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(SendSapienceGoodsMQ.class);

    @Autowired
    private SendSapienceGoodsService sendSapienceGoodsService;

    public SendSapienceGoodsMQ() {
        super.setTags(MQErpEnum.SEND_SAPIENCE_GOODS.getTags());
        super.setTopics(MQErpEnum.SEND_SAPIENCE_GOODS.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("----向卓普信推送商品档案开始----");
        try {
        	
        	JSONObject jsonData = JSONObject.fromObject(json);
        	Object codeObj = jsonData.get("code");// 
        	if (codeObj!=null) {
        		jsonData.put("derpTime", codeObj.toString());
			}else {
				jsonData.put("derpTime", TimeUtils.getNow().getTime());
			}
        	
        	sendSapienceGoodsService.sendSapienceGoods(jsonData.toString(), keys, topics, tags);
        } catch(Exception e) {
            LOGGER.error("向卓普信推送商品档案异常:{}", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
