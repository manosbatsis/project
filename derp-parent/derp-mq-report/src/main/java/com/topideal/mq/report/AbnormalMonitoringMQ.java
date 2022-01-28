package com.topideal.mq.report;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.AbnormalMonitoringService;
import com.topideal.service.SysDataService;

import net.sf.json.JSONObject;

/**
 * 异常监控预警
 * @author gy
 */
@Component
public class AbnormalMonitoringMQ extends ConsumerMQAbstract {

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(AbnormalMonitoringMQ.class);

	@Autowired
	private SysDataService sysDataService;
	@Autowired
	private AbnormalMonitoringService abnormalMonitoringService;

	public AbnormalMonitoringMQ() {
		super.setTags(MQReportEnum.ABNORMAL_MONITORING.getTags());
		super.setTopics(MQReportEnum.ABNORMAL_MONITORING.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============异常监控预警开始--===========");
		try {
			
			String selectTime = TimeUtils.format(new Date(), "yyyy-MM-dd");
			/*JSONObject jsonObject = new JSONObject() ;
			jsonObject.put("selectTime", selectTime) ;
			sysDataService.synsData(jsonObject.toString(), keys, topics, tags);*/
			
			abnormalMonitoringService.sendMonitoringMail(json, keys, topics, tags);
			LOGGER.info("=============异常监控预警结束--===========");
		} catch (Exception e) {
			LOGGER.error("异常:{}", e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
