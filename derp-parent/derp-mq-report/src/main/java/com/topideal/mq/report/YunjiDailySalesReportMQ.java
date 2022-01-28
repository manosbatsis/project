package com.topideal.mq.report;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.constant.DERP;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.YunjiDailySalesReportService;

import net.sf.json.JSONObject;

@Component
public class YunjiDailySalesReportMQ extends ConsumerMQAbstract {

	@Autowired
	private YunjiDailySalesReportService yunjiDailySalesReportService;
	@Autowired
	ReptileConfigMongoDao reptileConfigMongoDao;
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(YunjiDailySalesReportMQ.class);

	public YunjiDailySalesReportMQ() {
		super.setTags(MQReportEnum.YUNJI_DAILY_SALES_REPORT.getTags());
		super.setTopics(MQReportEnum.YUNJI_DAILY_SALES_REPORT.getTopic());
	}
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============每日云集采销日报天数据生成开始"+json+"===========");
		try {
			//时间多个 用英文逗号隔开
			JSONObject jsonData = JSONObject.fromObject(json);
	        String snapshotDateArr = (String) jsonData.get("snapshotDate");	
	        String merchantIdStr = (String)jsonData.get("merchantId");//商家id
	        
	        // 爬虫配置表
	        Map<String, Object> params = new HashMap<String, Object>();
	        if (StringUtils.isNotBlank(merchantIdStr)) {
	        	params.put("merchantId", Long.valueOf(merchantIdStr));
			}
			params.put("platformType", DERP.CRAWLER_TYPE_1);
			List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
			if(reptileConfigList == null || reptileConfigList.size()==0){
				LOGGER.error("获取不到相应的爬虫配置表信息");
				throw new RuntimeException("获取不到相应的爬虫配置表信息");
			}
			Map<Long, ReptileConfigMongo> reptileConfigMongoMap =new HashMap<>();
			// 一个商家对应多个用户去重
			for (ReptileConfigMongo reptileConfigMongo : reptileConfigList) {
				Long merchantId = reptileConfigMongo.getMerchantId();
				if (!reptileConfigMongoMap.containsKey(merchantId)) {
					reptileConfigMongoMap.put(merchantId, reptileConfigMongo);
				}
			}
			
			Set<Long> keySet = reptileConfigMongoMap.keySet();
			/**
			 * 多商家和多日期刷新
			 */
			for (Long merchantId : keySet) {				
				MerchantInfoModel merchantInfo = merchantInfoDao.searchById(merchantId);
				if(merchantInfo==null){
					LOGGER.error("没有找到对应的商家信");
					continue;
				}
				if (StringUtils.isNotBlank(snapshotDateArr)) {
		        	List<String> snapshotDateList = Arrays.asList(snapshotDateArr.split(","));
		        	for (String snapshotDate : snapshotDateList) {
		        		if (StringUtils.isNotBlank(snapshotDate)) {
		        			jsonData.put("snapshotDate", snapshotDate);
			        		yunjiDailySalesReportService.getYunjiDailySalesReport(jsonData.toString(), keys, topics, tags,merchantInfo);		
						}
		        	}
				}else {
	        		yunjiDailySalesReportService.getYunjiDailySalesReport(jsonData.toString(), keys, topics, tags,merchantInfo);
				}
			}
	        
	        
	        
	        
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("异常:{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
