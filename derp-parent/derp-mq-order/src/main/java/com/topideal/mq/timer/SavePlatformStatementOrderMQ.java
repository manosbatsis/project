package com.topideal.mq.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SavePlatformStatementOrderService;

import net.sf.json.JSONObject;
/**
 * 生成平台结算订单
 */
@Component
public class SavePlatformStatementOrderMQ extends ConsumerMQAbstract{
	
	@Autowired
	private SavePlatformStatementOrderService savePlatformStatementOrderService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(SavePlatformStatementOrderMQ.class);
	
	public SavePlatformStatementOrderMQ(){
		super.setTags(MQOrderEnum.TIMER_PLATFORM_STATEMENT_ORDER.getTags());
		super.setTopics(MQOrderEnum.TIMER_PLATFORM_STATEMENT_ORDER.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================生成平台结算订单MQ=================="+json);
		
		synchronized (this) {
			try {
				JSONObject jsonData = JSONObject.fromObject(json);
				
				String customerType = null ;
				
				if(jsonData.get("customerType") != null) {
					customerType = jsonData.getString("customerType") ;
				}
				
				if(DERP_ORDER.PLATFORM_STATEMENT_CUSTOMER_TYPE_1.equals(customerType)) {
					
					/**生成云集结算单*/
					savePlatformStatementOrderService.saveYJPlatStatementOrder(json, keys, topics, tags) ;
					
				}else if(DERP_ORDER.PLATFORM_STATEMENT_CUSTOMER_TYPE_2.equals(customerType)) {
					
					/**生成唯品结算单*/
					savePlatformStatementOrderService.saveVipPlatStatementOrder(json, keys, topics, tags) ;
					
				}else if(DERP_ORDER.PLATFORM_STATEMENT_CUSTOMER_TYPE_3.equals(customerType)) {
					
					/**生成天猫结算单*/
					savePlatformStatementOrderService.saveTMPlatStatementOrder(json, keys, topics, tags) ;
					
				}else {
					
					// 定时器
					/**生成云集结算单*/
					savePlatformStatementOrderService.saveYJPlatStatementOrder(json, keys, topics, tags) ;
					/**生成唯品结算单*/
					savePlatformStatementOrderService.saveVipPlatStatementOrder(json, keys, topics, tags) ;
					/**生成天猫结算单*/
					savePlatformStatementOrderService.saveTMPlatStatementOrder(json, keys, topics, tags) ;
				}
				
		
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("生成平台费用订单异常",e);
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

	}

}
