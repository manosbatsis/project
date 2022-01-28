package com.topideal.service.api.common.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.reporting.BuFinanceInventorySummaryDao;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.service.api.common.ReportSysMongoDataService;

import net.sf.json.JSONObject;
/**
 * 同步数据到mongodb
 */
@Service
public class ReportSysMongoDataServiceImpl implements ReportSysMongoDataService{
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportSysMongoDataServiceImpl.class);
	

	@Autowired
	private BuFinanceInventorySummaryDao buFinanceInventorySummaryDao;//事业部财务进销存总表	
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;


	/**
	 * 删除/同步数据 分发方法
	 */
	@Override
	public boolean sysMongoData(String json, String keys, String topics, String tags) throws Exception {
		
		JSONObject jsonData = JSONObject.fromObject(json);
		if (jsonData.get("tableName")==null||StringUtils.isBlank(jsonData.getString("tableName"))) {
			throw new RuntimeException("要同步的表名不能为空");
		}	
		String tableName = jsonData.getString("tableName");		
		if ("r_bu_finance_inventory_summary".equals(tableName)) {
			saveBuFinanceInventorySummary(jsonData);
		}
		
		return true;
	}
	
	/**
	 * 同步财务经存总表商家事业部月份最大关账日期到mongdb
	 * @param jsonData
	 * @throws SQLException 
	 */
	private void saveBuFinanceInventorySummary(JSONObject jsonData) throws SQLException {
		// 统计汇总事业部财务经销存总表
		List<Map<String, Object>>maxCloseMap=buFinanceInventorySummaryDao.getMaxCloseAccountMerchantBu();
		
		for (Map<String, Object> map : maxCloseMap) {
			Long merchantId = (Long) map.get("merchant_id");
			String merchantName = (String) map.get("merchant_name");
			Long buId = (Long) map.get("bu_id");
			String buName = (String) map.get("bu_name");
			String month = (String) map.get("month");			
			FinanceCloseAccountsMongo mongo=new FinanceCloseAccountsMongo();
			mongo.setMerchantId(merchantId);
			mongo.setMerchantName(merchantName);
			mongo.setBuId(buId);
			mongo.setBuName(buName);
			mongo.setMonth(month);
			mongo.setStatus(DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_030);//
			mongo.setSource(DERP.CLOSE_ACCOUNTS_SOURCE_1);
			String createDateStr = TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd HH:mm:ss");
			mongo.setCreateDateStr(createDateStr);			
			JSONObject jsonObject=JSONObject.fromObject(mongo);
			financeCloseAccountsMongoDao.insertJson(jsonObject.toString());
		}



	}
}
