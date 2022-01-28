package com.topideal.mq.report;


import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.service.impl.InventoryFallingPriceReservesServiceImpl;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.InventoryFallingPriceReservesService;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


/**
 * 存货跌价准备
 * @author gy
 *
 */
@Component
public class InventoryFallingPriceReservesMQ extends ConsumerMQAbstract{

	/* 打印日志 */
	private static final Logger logger = LoggerFactory.getLogger(InventoryFallingPriceReservesMQ.class);

	@Autowired
	private InventoryFallingPriceReservesService inventoryFallingPriceReservesService ;
	// 商家dao
	@Autowired
	private MerchantInfoDao merchantInfoDao;

	public InventoryFallingPriceReservesMQ() {
		super.setTags(MQReportEnum.INVENTORY_FALLING_PRICE_RESERVES.getTags());
		super.setTopics(MQReportEnum.INVENTORY_FALLING_PRICE_RESERVES.getTopic());
	}
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		logger.info("=============生成存货跌价准备报表开始--===========");
		try {
			JSONObject jsonData = JSONObject.fromObject(json);
			Map<String, Object> jsonMap = jsonData;
			Integer merchantId = (Integer) jsonMap.get("merchantId");// 商家Id
			String months = (String) jsonMap.get("month");//报表月份

			//计算要刷新的月份
			if(StringUtils.isEmpty(months)){
				//若没有指定月份则取当前时间前一天日期近二个月月份,刷新未关账的，本月、上月
				months = TimeUtils.getLastTwoMonthsByNow();
			}
			String[] montharr = months.split(",");

			/**1.查询所有启用的公司,若有指定公司则查指定公司 (过滤测试商家)*/
			MerchantInfoModel model = new MerchantInfoModel();
			if (merchantId != null && merchantId.longValue() > 0) {
				model.setId(Long.valueOf(merchantId));
			}
			model.setStatus(DERP_SYS.MERCHANTINFO_STATUS_1);//启用
			List<MerchantInfoModel> merchantList = merchantInfoDao.list(model);
            if(merchantList==null||merchantList.size()<=0){
				logger.info("公司数量为0，结束");
			}
			for(MerchantInfoModel merchantInfoModel : merchantList){
				for(String month : montharr){
					//过滤测试商家
					if("toipidealtest".equals(merchantInfoModel.getTopidealCode())) continue;
					inventoryFallingPriceReservesService.saveSummaryReport(json, keys, topics, tags,month,merchantInfoModel);
				}

			}


			logger.info("=============生成存货跌价准备报表结束--===========");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:{}", e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
