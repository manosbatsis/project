package com.topideal.mq.api.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.sale.XSTStorageDeclareService;

/**
 * 入库申报MQ
 */
@Component
public class XSTStorageDeclareMQ extends ConsumerMQAbstract {
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(XSTStorageDeclareMQ.class);
	@Autowired
	private XSTStorageDeclareService xSTStorageDeclareService;//入库申报Service
	
	public XSTStorageDeclareMQ(){
		super.setTopics(MQOrderEnum.EPASS_STORAGE_DECLARE_2.getTopic());
		super.setTags(MQOrderEnum.EPASS_STORAGE_DECLARE_2.getTags());	
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===============入库申报接口===========");
        try{
        	xSTStorageDeclareService.saveStorageDeclareInfo(json,keys,topics,tags);
        }catch(Exception e){
        	e.printStackTrace();
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

	}

}
