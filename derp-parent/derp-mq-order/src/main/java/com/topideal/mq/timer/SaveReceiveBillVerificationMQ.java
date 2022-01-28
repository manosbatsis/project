package com.topideal.mq.timer;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SaveReceiveBillVerificationService;
/**
 * 收款核销跟踪 生成和刷新MQ
 */
@Component
public class SaveReceiveBillVerificationMQ extends ConsumerMQAbstract{
	
	@Autowired
	private SaveReceiveBillVerificationService saveReceiveBillVerificationService;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(SaveReceiveBillVerificationMQ.class);
	
	public SaveReceiveBillVerificationMQ(){
		super.setTags(MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTags());
		super.setTopics(MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTopic());
	}
	
	
	@Override 
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================收款核销跟踪 生成和刷新MQ=================="+json);
		synchronized(this){
			try {
				Thread.sleep(2000);

				JSONObject jsonData = JSONObject.fromObject(json);
				String operateType = (String) jsonData.get("operateType"); //0-审核通过 1-作废通过

				if (StringUtils.isBlank(operateType) || "0".equals(operateType)) {
					saveReceiveBillVerificationService.saveReceiveBillVerification(json, keys, topics, tags);
				}

				if (StringUtils.isBlank(operateType) || "1".equals(operateType)) {
					saveReceiveBillVerificationService.delReceiveBillVerification(json, keys, topics, tags);
				}

			} catch (Exception e) {
				LOGGER.error("收款核销跟踪 生成和刷新MQ异常",e.getMessage());
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		}
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
