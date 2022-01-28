package com.topideal.mq.api.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.purchase.PurchaseStorageDeclareService;

/**
 * 入库申报MQ
 * @author zhanghx
 * 2018/7/16
 */
@Component
public class PurchaseStorageDeclareMQ extends ConsumerMQAbstract {
	
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(PurchaseStorageDeclareMQ.class);
	
	@Autowired
	private PurchaseStorageDeclareService storageDeclareService;//入库申报Service
	
	public PurchaseStorageDeclareMQ(){
		super.setTopics(MQOrderEnum.EPASS_STORAGE_DECLARE_1.getTopic());
		super.setTags(MQOrderEnum.EPASS_STORAGE_DECLARE_1.getTags());	
	}
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===============入库申报接口===========");
        try{
        	storageDeclareService.saveStorageDeclareInfo(json,keys,topics,tags);
        }catch(Exception e){
        	e.printStackTrace();
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
