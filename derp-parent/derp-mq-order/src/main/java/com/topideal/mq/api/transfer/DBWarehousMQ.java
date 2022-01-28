package com.topideal.mq.api.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.transfer.DBWarehousService;

/**
 * 入库申报MQ-调拨
 * @author yucaifu
 */
@Component
public class DBWarehousMQ extends ConsumerMQAbstract {
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(DBWarehousMQ.class);
	@Autowired
	private DBWarehousService dBWarehousService;//入库申报Service
	
	public DBWarehousMQ(){
		super.setTopics(MQOrderEnum.EPASS_STORAGE_DECLARE_3.getTopic());
		super.setTags(MQOrderEnum.EPASS_STORAGE_DECLARE_3.getTags());	
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===============入库申报接口===========");
        try{
        	dBWarehousService.saveWarehousInfo(json,keys,topics,tags);
        }catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage());
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

	}

}
