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
import com.topideal.service.SaleTargetAchievementService;
import com.topideal.service.SysDataService;

import net.sf.json.JSONObject;

/**
 * 销售目标达成率
 * @author Gy
 */
@Component
public class SaleTargetAchievementMQ extends ConsumerMQAbstract {

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(SaleTargetAchievementMQ.class);

	@Autowired
	SaleTargetAchievementService saleTargetAchievementService ;
	
	@Autowired
	private SysDataService sysDataService;

	public SaleTargetAchievementMQ() {
		super.setTags(MQReportEnum.SALES_TARGET_ACHIEVEMENT.getTags());
		super.setTopics(MQReportEnum.SALES_TARGET_ACHIEVEMENT.getTopic());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============销售目标达成率开始--===========");
		try {
			/*JSONObject jsonData = JSONObject.fromObject(json);
			Map<String, Object> jsonMap = jsonData;
			String syn = (String) jsonMap.get("syn");//同步数据 true/false
			
			if(!StringUtils.isEmpty(syn)&&syn.equals("true")){
				sysDataService.synsData(json, keys, topics, tags);
			}*/
			saleTargetAchievementService.saveSummaryReport(json, keys, topics, tags);
			LOGGER.info("=============销售目标达成率结束--===========");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("异常:{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
