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
import com.topideal.service.SysDataService;
import com.topideal.service.VipAutomaticVerificationService;

import net.sf.json.JSONObject;

/**
 * 唯品自动校验MQ
 * @author gy
 *
 */
@Component
public class VipAutomaticVerificationMQ extends ConsumerMQAbstract{

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(VipAutomaticVerificationMQ.class);
	
	@Autowired
	private SysDataService sysDataService;
	
	@Autowired
	private VipAutomaticVerificationService vipAutomaticVerificationService ;
	
	public VipAutomaticVerificationMQ() {
		super.setTags(MQReportEnum.VIP_AUTO_VERI.getTags());
		super.setTopics(MQReportEnum.VIP_AUTO_VERI.getTopic());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============生成唯品自动校验报表开始--===========");
		try {
			/*JSONObject jsonData = JSONObject.fromObject(json);
			Map<String, Object> jsonMap = jsonData;
			String syn = (String) jsonMap.get("syn");//同步数据 true/false
			
			if(!StringUtils.isEmpty(syn)&&syn.equals("true")){
				sysDataService.synsData(json, keys, topics, tags);
			}*/
			vipAutomaticVerificationService.saveSummaryReport(json, keys, topics, tags);
			LOGGER.info("=============生成唯品自动校验报表结束--===========");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("异常:{}", e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
