package com.topideal.mq.report;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InWareHouseDaysReportService;
import com.topideal.service.SysDataService;

import net.sf.json.JSONObject;

/**
 * 商品在库天数报表
 * @author 
 */
@Component
public class InWareHouseDaysReportMQ extends ConsumerMQAbstract {

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(InWareHouseDaysReportMQ.class);

	@Autowired
	private InWareHouseDaysReportService inWareHouseDaysReportService;
	@Autowired
	private SysDataService sysDataService;

	public InWareHouseDaysReportMQ() {
		super.setTags(MQReportEnum.IN_WAREHOUSE_DAYS.getTags());
		super.setTopics(MQReportEnum.IN_WAREHOUSE_DAYS.getTopic());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============生成在库天数报表开始--===========");
		try {

			inWareHouseDaysReportService.saveSummaryReport(json, keys, topics, tags);
			LOGGER.info("=============生成在库天数报表结束--===========");
		} catch (Exception e) {
			LOGGER.error("异常:{}", e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
