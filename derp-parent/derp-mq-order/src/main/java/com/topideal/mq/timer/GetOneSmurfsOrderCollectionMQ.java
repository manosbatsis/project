package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.GrabSmurfsOrderCollectionService;
/**
 * 抓取蓝精灵订单采集发货和完成订单MQ
 */
@Component
public class GetOneSmurfsOrderCollectionMQ extends ConsumerMQAbstract{
	
	@Autowired
	private GrabSmurfsOrderCollectionService grabSmurfsOrderCollectionService;

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(GetOneSmurfsOrderCollectionMQ.class);
	
	public GetOneSmurfsOrderCollectionMQ(){
		super.setTags(MQOrderEnum.GETONE_SMURFS_ORDER_COLLECTION_ORDER.getTags());
		super.setTopics(MQOrderEnum.GETONE_SMURFS_ORDER_COLLECTION_ORDER.getTopic());
	}
	
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================消费单个蓝精灵订单采集发货和完成订单==================");
		//LOGGER.info("消费单个蓝精灵订单采集发货和完成订单开始"+json);		
		// 推送service
		try {			
			grabSmurfsOrderCollectionService.insertGrabSmurfsOrderCollection(json,keys,topics,tags);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		
	}




}
