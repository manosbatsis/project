package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.OrderApportionTaxAndFreightService;

import net.sf.json.JSONObject;

/**
  * 电商订单分摊税费运费
 * @author qiancheng.chen
 * @date 2020-12-24
 *
 */
@Component
public class OrderApportionTaxAndFreightMQ  extends ConsumerMQAbstract{
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(OrderApportionTaxAndFreightMQ.class);
	@Autowired
	public OrderApportionTaxAndFreightService orderApportionTaxAndFreightService;
	
	
	public OrderApportionTaxAndFreightMQ() {
		super.setTags(MQOrderEnum.TIMER_ORDER_APPORTION_TAX_FREIGHT.getTags());
		super.setTopics(MQOrderEnum.TIMER_ORDER_APPORTION_TAX_FREIGHT.getTopic());
	}
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("==================电商订单分摊税费运费开始==================");
		synchronized(this){
			try {
				JSONObject jsonTagData = JSONObject.fromObject(json);
				Object remarkObj = jsonTagData.get("remark");// remark 为空来源定时器（）  不为空来源其他（睡眠60秒）
				if (remarkObj!=null) Thread.sleep(60000);
				
				orderApportionTaxAndFreightService.apportionTaxAndFreight(json, keys, topics, tags);				
				LOGGER.info("===================电商订单分摊税费运费结束==================");
				
			} catch (Exception e) {
				LOGGER.error("电商订单分摊税费运费异常："+e,e.getMessage());
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
