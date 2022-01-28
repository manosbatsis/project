//package com.topideal.mq.pushback.purchase;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import com.topideal.common.enums.MQPushBackOrderEnum;
//import com.topideal.mq.tools.ConsumerMQAbstract;
//import com.topideal.service.pushback.purchase.ConfirmDepotPushbackService;
//
///**
// * 采购 确认入仓 回推
// * @author zhanghx
// * 2018/7/16
// */
//@Component
//public class ConfirmDepotPushbackMQ extends ConsumerMQAbstract {
//
//    @Autowired
//    private ConfirmDepotPushbackService service;
//
//    /*打印日志*/
//    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmDepotPushbackMQ.class);
//
//    public ConfirmDepotPushbackMQ(){
//        super.setTopics(MQPushBackOrderEnum.CONFIRM_DEPOT_PUSH_BACK.getTopic());
//        super.setTags(MQPushBackOrderEnum.CONFIRM_DEPOT_PUSH_BACK.getTags());
//    }
//
//    @Override
//    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
//        LOGGER.info("===============采购 确认入仓 回推===========");
//        try{
//        	service.modifyStatus(json, keys, topics, tags);
//        }catch(Exception e){
//            LOGGER.error("异常:{}",e);
//            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
//        }
//        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//    }
//
//}
