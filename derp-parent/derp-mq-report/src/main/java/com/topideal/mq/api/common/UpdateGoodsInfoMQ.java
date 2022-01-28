package com.topideal.mq.api.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.common.UpdateGoodsInfoService;
/**
 * 修改商品信息MQ
 */
@Component
public class UpdateGoodsInfoMQ extends ConsumerMQAbstract{
	
	@Autowired
	private UpdateGoodsInfoService updateGoodsInfoService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(UpdateGoodsInfoMQ.class);
	
	public UpdateGoodsInfoMQ(){
		super.setTags(MQReportEnum.UPDATE_REPORT_GOODS_INFO.getTags());
		super.setTopics(MQReportEnum.UPDATE_REPORT_GOODS_INFO.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags ) {
		LOGGER.info("===================report修改商品信息==================");
		try {
			updateGoodsInfoService.updateGoodsInfo(json,keys,topics,tags);
		} catch (Exception e) {
			LOGGER.error("report修改商品信息异常："+e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
