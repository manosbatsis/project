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
import com.topideal.service.BuInventorySummaryReportService;
import com.topideal.service.SysDataService;

import net.sf.json.JSONObject;

/**
 * 经销存事业部汇总报表
 * 
 * @author 
 */
@Component
public class BuInventorySummaryReportMQ extends ConsumerMQAbstract {

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(BuInventorySummaryReportMQ.class);

	@Autowired
	private BuInventorySummaryReportService buInventorySummaryReportService;
	@Autowired
	private SysDataService sysDataService;

	public BuInventorySummaryReportMQ() {
		super.setTags(MQReportEnum.BU_INVENTORY_SUMMARY.getTags());
		super.setTopics(MQReportEnum.BU_INVENTORY_SUMMARY.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============生成事业部经销存汇总报表开始===========");
		try {
			/*JSONObject jsonData = JSONObject.fromObject(json);
			Map<String, Object> jsonMap = jsonData;
			String syn = (String) jsonMap.get("syn");//同步数据 true/false
			
			if(!StringUtils.isEmpty(syn)&&syn.equals("true")){
				sysDataService.synsData(json, keys, topics, tags);
			}*/
			buInventorySummaryReportService.saveBuSummaryReport(json, topics, tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("事业部业务经销存异常:{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
