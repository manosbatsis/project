package com.topideal.mq.report;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.SysDataService;
import com.topideal.service.VipPoBillVerificationService;

import net.sf.json.JSONObject;

/**
 * 唯品PO账单核销报表MQ
 * @author gy
 *
 */
@Component
public class VipPoBillVerificationMQ extends ConsumerMQAbstract{

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(VipPoBillVerificationMQ.class);
	
	@Autowired
	private SysDataService sysDataService;
	
	@Autowired
	private VipPoBillVerificationService vipPoBillVerificationService ;
	
	public VipPoBillVerificationMQ() {
		super.setTags(MQReportEnum.VIP_PO_BILL_VERI.getTags());
		super.setTopics(MQReportEnum.VIP_PO_BILL_VERI.getTopic());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============生成唯品by po报表开始--===========");
		try {
			JSONObject jsonData = JSONObject.fromObject(json);
			Map<String, Object> jsonMap = jsonData;
			
			/**生成唯品by po报表*/
			vipPoBillVerificationService.saveSummaryReport(json, keys, topics, tags);
			/**PO表数据同步业务库*/
			//页面触发所带标识，不同步数据
			if(jsonMap.get("fromPage") == null) {
				vipPoBillVerificationService.synsOrderData(json, keys, topics, tags) ;
			}
			
			LOGGER.info("=============生成唯品by po报表结束--===========");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("异常:{}", e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
