package com.topideal.mq.api;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.tools.CollectionEnum;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/7 15:07
 * @Description:
 */
@Component
public class PushApiLogCommonMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger logger= LoggerFactory.getLogger(PushApiLogCommonMQ.class);

    @Autowired
    private RMQLogProducer rocketMQProducer;

    // 库存日志
    @Autowired
    private JSONMongoDao jsonMongoDao;

    public PushApiLogCommonMQ(){
        super.setTags(MQPushApiEnum.PUSH_API_LOG_COMMON.getTags());
        super.setTopics(MQPushApiEnum.PUSH_API_LOG_COMMON.getTopic());
    }

    @Override
    public String execute(String json,String keys,String topics,String tags)throws Exception{
    	logger.info("===================异步推送api日志通用接口==================");
    	logger.debug("json:{}", json);
        JSONObject jsonObject= JSONObject.fromObject(json);

        jsonObject.put("endDate", System.currentTimeMillis());
        jsonObject.put("state", 1);
        //mongodb 日志记录报文
        jsonObject.put("id", UUID.randomUUID().toString());
        //推送到日志MQ平台
        jsonObject.put("moduleCode", LogModuleType.MODULE_PUSH_API.getType());
        SendResult sendResult = rocketMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());
        jsonObject.put("modulCode", "1002");
        logger.info("==报文："+jsonObject.toString()+"==");
        logger.info("==响应报文："+sendResult+"==");
        if (sendResult==null||!sendResult.getSendStatus().name().equals("SEND_OK")) {
            jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
            logger.info("==报文丢失："+jsonObject.toString()+"==");
        }

        return "成功";
    }



}
