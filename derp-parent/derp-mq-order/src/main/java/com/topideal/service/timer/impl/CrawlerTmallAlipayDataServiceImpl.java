package com.topideal.service.timer.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.dao.platformdata.AlipayDailyFeeDao;
import com.topideal.dao.platformdata.AlipayDailySettleDao;
import com.topideal.dao.platformdata.AlipayDailySettlebatchDao;
import com.topideal.dao.platformdata.AlipayMonthlyFeeDao;
import com.topideal.entity.vo.platformdata.AlipayDailyFeeModel;
import com.topideal.entity.vo.platformdata.AlipayDailySettleModel;
import com.topideal.entity.vo.platformdata.AlipayDailySettlebatchModel;
import com.topideal.entity.vo.platformdata.AlipayMonthlyFeeModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.CrawlerTmallAlipayDataService;

import net.sf.json.JSONObject;

@Service
public class CrawlerTmallAlipayDataServiceImpl implements CrawlerTmallAlipayDataService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerTmallAlipayDataServiceImpl.class);
	
	private static final Integer PAGE_SIZE = 3000 ;
	
	@Autowired
	MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	ReptileConfigMongoDao reptileConfigMongoDao;
	@Autowired
	AlipayMonthlyFeeDao  alipayMonthlyFeeDao;
	@Autowired
	AlipayDailySettlebatchDao alipayDailySettlebatchDao;
	@Autowired
	AlipayDailySettleDao alipayDailySettleDao;
	@Autowired
	AlipayDailyFeeDao alipayDailyFeeDao;


	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201140001,model=DERP_LOG_POINT.POINT_13201140001_Label)
	public void insertDailySettleBatch(String json, String keys, String topics, String tags) throws SQLException {
		JSONObject jsonData = JSONObject.fromObject(json);
		String loginName = (String) jsonData.get("loginName");
		String loginNameAppend = "";
		if (StringUtils.isNotBlank(loginName)) {
			loginNameAppend = " and userCode='" + loginName + "'";
		}

		Connection conn = null;
		Statement pst = null;
		Statement pst2 = null;
		ResultSet rs2 = null;
		Statement pst3 = null;
		ResultSet rs3 = null;
		String queryTotalStr = "select count(*) as totalNum from alipay_daily_settlebatch where  hasReceived = '0' "
				+ loginNameAppend;
		String queryStr = "select * from alipay_daily_settlebatch where  hasReceived = '0' " + loginNameAppend + " limit ";
		String updateStr = "update alipay_daily_settlebatch set hasReceived = '1' where id  in ( ";

		StringBuffer ids = new StringBuffer();
		LOGGER.info("请求统计sql:" + queryTotalStr);
		LOGGER.info("请求统计sql:" + queryStr);

		// 连接爬虫数据库
		try {
			Class.forName(ApolloUtilDB.jdbforName);
			conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,
					ApolloUtilDB.crawlerPassword);
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			pst = conn.createStatement();
			pst2 = conn.createStatement();
			pst3 = conn.createStatement();
			int start = 0;
			int end = PAGE_SIZE;
			rs3 = pst3.executeQuery(queryTotalStr);
			// 用于存放修改爬虫账单的sql
			Map<Integer, StringBuffer> idsMap = new HashMap<>();
			while (rs3.next()) {
				Integer totalNum = rs3.getInt("totalNum");
				Integer pageSize = totalNum / PAGE_SIZE;
				if (totalNum % PAGE_SIZE != 0) {
					pageSize += 1;
				}
				
				List<AlipayDailySettlebatchModel> saveList = new ArrayList<>() ;
				
				for (int i = 0; i < pageSize; i++) {
					rs2 = pst2.executeQuery(queryStr + start + "," + end);
					
					saveList.clear(); 
					
					while (rs2.next()) {
						Long id = rs2.getLong("id");
						// 根据登录用户名获取爬虫配置信息
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("loginName", rs2.getString("userCode"));
						List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
						if (reptileConfigList == null || reptileConfigList.size() == 0) {
							LOGGER.error("获取不到相应的配置信息，账单id为：" + id);
							continue;
						}

//						String billCode = rs2.getString("billCode");
//						String dateStr = billCode.substring(3, 11);

						ReptileConfigMongo reptileConfigModel = reptileConfigList.get(0);
						// 根据配置获取商家信息
						Map<String, Object> params1 = new HashMap<String, Object>();
						params1.put("merchantId", reptileConfigModel.getMerchantId());
						MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(params1);
						
						// 保存爬虫账单
						AlipayDailySettlebatchModel model = new AlipayDailySettlebatchModel();
						model.setUserCode(rs2.getString("userCode"));
						model.setSettleNo(rs2.getString("settleNo"));
						model.setBatchNo(rs2.getString("batchNo"));
						model.setSettleDate(rs2.getTimestamp("settleDate"));
						model.setCurrency(rs2.getString("currency"));
						model.setAmount(rs2.getBigDecimal("amount"));
						model.setFee(rs2.getBigDecimal("fee"));
						model.setSettlement(rs2.getBigDecimal("settlement"));
						model.setAlipayCreateDate(rs2.getTimestamp("createdAt"));
						model.setAlipayModifyDate(rs2.getTimestamp("modifiedAt"));
						model.setMerchantId(merchantInfo.getMerchantId());
						model.setMerchantName(merchantInfo.getName());
						model.setFileKey(rs2.getString("fileKey"));
						model.setOldId(Long.valueOf(rs2.getInt("id")));
						

						

						saveList.add(model) ;
						
						LOGGER.info("===========同步每日结算数据汇总id：{}", rs2.getInt("id"));
						// 记录id
						ids.append(id + ",");
					}
					if (saveList.size()>0) {
						alipayDailySettlebatchDao.alipayBatchSave(saveList) ;
						idsMap.put(i, ids);
					}
					
					
					start += PAGE_SIZE;
					ids = new StringBuffer();
				}
			}
			Set<Integer> idsSet = idsMap.keySet();
			for (Integer i : idsSet) {
				StringBuffer idStringBuffer = idsMap.get(i);
				// 更新爬取数据的状态为：“已爬取”
				String idStr = "";
				if (idStringBuffer.length() > 0) {
					idStr = idStringBuffer.substring(0, idStringBuffer.length() - 1);
				}
				if (!"".equals(idStr)) {
					int result2 = pst.executeUpdate(updateStr + idStr + ")");
					
					LOGGER.debug("更新数：" + result2);
				}
			}
			// 没有异常事物提交 关闭其他
			conn.commit();
			if (rs2 != null) {
				rs2.close();
			}
			if (rs3 != null) {
				rs3.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (pst2 != null) {
				pst2.close();
			}
			if (pst3 != null) {
				pst3.close();
			}
			if (conn != null) {
				conn.close();
			}

		} catch (Exception e) {
			LOGGER.error("同步每日结算数据汇总失败", e);
			conn.rollback();
			if (rs2 != null) {
				rs2.close();
			}
			if (rs3 != null) {
				rs3.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (pst2 != null) {
				pst2.close();
			}
			if (pst3 != null) {
				pst3.close();
			}
			if (conn != null) {
				conn.close();
			}
			throw new RuntimeException("同步每日结算数据汇总失败");
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		LOGGER.info("vip end ...," + sf.format(date));
	}

	
	@Override
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_13201140012,model=DERP_LOG_POINT.POINT_13201140012_Label)
	public void insertDailySettle(String json, String keys, String topics, String tags) throws SQLException {
		JSONObject jsonData = JSONObject.fromObject(json);
		String loginName = (String) jsonData.get("loginName");
		String loginNameAppend = "";
		if (StringUtils.isNotBlank(loginName)) {
			loginNameAppend = " and userCode='" + loginName + "'";
		}

		Connection conn = null;
		Statement pst = null;
		Statement pst2 = null;
		ResultSet rs2 = null;
		Statement pst3 = null;
		ResultSet rs3 = null;
		String queryTotalStr = "select count(*) as totalNum from alipay_daily_settle where  hasReceived = '0' "
				+ loginNameAppend;
		String queryStr = "select * from alipay_daily_settle where  hasReceived = '0' " + loginNameAppend + " limit ";
		String updateStr = "update alipay_daily_settle set hasReceived = '1' where id  in ( ";

		StringBuffer ids = new StringBuffer();
		LOGGER.info("请求统计sql:" + queryTotalStr);
		LOGGER.info("请求统计sql:" + queryStr);

		// 连接爬虫数据库
		try {
			Class.forName(ApolloUtilDB.jdbforName);
			conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,
					ApolloUtilDB.crawlerPassword);
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			pst = conn.createStatement();
			pst2 = conn.createStatement();
			pst3 = conn.createStatement();
			int start = 0;
			int end = PAGE_SIZE;
			rs3 = pst3.executeQuery(queryTotalStr);
			// 用于存放修改爬虫账单的sql
			Map<Integer, StringBuffer> idsMap = new HashMap<>();
			while (rs3.next()) {
				Integer totalNum = rs3.getInt("totalNum");
				Integer pageSize = totalNum / PAGE_SIZE;
				if (totalNum % PAGE_SIZE != 0) {
					pageSize += 1;
				}
				
				List<AlipayDailySettleModel> saveList = new ArrayList<>() ;
				
				for (int i = 0; i < pageSize; i++) {
					rs2 = pst2.executeQuery(queryStr + start + "," + end);
					
					saveList.clear(); 
					
					while (rs2.next()) {
						Long id = rs2.getLong("id");
						// 根据登录用户名获取爬虫配置信息
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("loginName", rs2.getString("userCode"));
						List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
						if (reptileConfigList == null || reptileConfigList.size() == 0) {
							LOGGER.error("获取不到相应的配置信息，账单id为：" + id);
							continue;
						}

//						String billCode = rs2.getString("billCode");
//						String dateStr = billCode.substring(3, 11);

						ReptileConfigMongo reptileConfigModel = reptileConfigList.get(0);
						// 根据配置获取商家信息
						Map<String, Object> params1 = new HashMap<String, Object>();
						params1.put("merchantId", reptileConfigModel.getMerchantId());
						MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(params1);
						
						// 保存爬虫账单
						AlipayDailySettleModel model = new AlipayDailySettleModel();
						model.setUserCode(rs2.getString("userCode"));
						model.setSettleNo(rs2.getString("settleNo"));
						model.setPartnerTransactionId(rs2.getString("partnerTransactionId"));
						model.setAmount(rs2.getBigDecimal("amount"));
						model.setRmbAmount(rs2.getBigDecimal("rmbAmount"));
						model.setRate(rs2.getBigDecimal("rate"));
						model.setCurrency(rs2.getString("currency"));
						model.setAlipayCreateDate(rs2.getTimestamp("createdAt"));
						model.setAlipayModifyDate(rs2.getTimestamp("modifiedAt"));
						model.setMerchantId(merchantInfo.getMerchantId());
						model.setMerchantName(merchantInfo.getName());
						model.setOldId(Long.valueOf(rs2.getInt("id")));

						saveList.add(model) ;
						
						LOGGER.info("===========同步每日结算明细数据id：{}", rs2.getInt("id"));
						// 记录id
						ids.append(id + ",");
					}
					if (saveList.size()>0) {
						alipayDailySettleDao.alipayBatchSave(saveList) ;
						idsMap.put(i, ids);
					}
					
					
					start += PAGE_SIZE;
					

					ids = new StringBuffer();
				}
			}
			Set<Integer> idsSet = idsMap.keySet();
			for (Integer i : idsSet) {
				StringBuffer idStringBuffer = idsMap.get(i);
				// 更新爬取数据的状态为：“已爬取”
				String idStr = "";
				if (idStringBuffer.length() > 0) {
					idStr = idStringBuffer.substring(0, idStringBuffer.length() - 1);
				}
				if (!"".equals(idStr)) {
					int result2 = pst.executeUpdate(updateStr + idStr + ")");
					
					LOGGER.debug("更新数：" + result2);
				}
			}
			// 没有异常事物提交 关闭其他
			conn.commit();
			if (rs2 != null) {
				rs2.close();
			}
			if (rs3 != null) {
				rs3.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (pst2 != null) {
				pst2.close();
			}
			if (pst3 != null) {
				pst3.close();
			}
			if (conn != null) {
				conn.close();
			}

		} catch (Exception e) {
			LOGGER.error("同步每日结算明细数据失败", e);
			conn.rollback();
			if (rs2 != null) {
				rs2.close();
			}
			if (rs3 != null) {
				rs3.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (pst2 != null) {
				pst2.close();
			}
			if (pst3 != null) {
				pst3.close();
			}
			if (conn != null) {
				conn.close();
			}
			throw new RuntimeException("同步每日结算明细数据失败");
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		LOGGER.info("vip end ...," + sf.format(date));
	}
	@Override
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_13201140013,model=DERP_LOG_POINT.POINT_13201140013_Label)
	public void insertDailyFee(String json, String keys, String topics, String tags) throws SQLException {
		JSONObject jsonData = JSONObject.fromObject(json);
		String loginName = (String) jsonData.get("loginName");
		String loginNameAppend = "";
		if (StringUtils.isNotBlank(loginName)) {
			loginNameAppend = " and userCode='" + loginName + "'";
		}

		Connection conn = null;
		Statement pst = null;
		Statement pst2 = null;
		ResultSet rs2 = null;
		Statement pst3 = null;
		ResultSet rs3 = null;
		String queryTotalStr = "select count(*) as totalNum from alipay_daily_fee where  hasReceived = '0' "
				+ loginNameAppend;
		String queryStr = "select * from alipay_daily_fee where  hasReceived = '0' " + loginNameAppend + " limit ";
		String updateStr = "update alipay_daily_fee set hasReceived = '1' where id  in ( ";

		StringBuffer ids = new StringBuffer();
		LOGGER.info("请求统计sql:" + queryTotalStr);
		LOGGER.info("请求统计sql:" + queryStr);

		// 连接爬虫数据库
		try {
			Class.forName(ApolloUtilDB.jdbforName);
			conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,
					ApolloUtilDB.crawlerPassword);
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			pst = conn.createStatement();
			pst2 = conn.createStatement();
			pst3 = conn.createStatement();
			int start = 0;
			int end = PAGE_SIZE;
			rs3 = pst3.executeQuery(queryTotalStr);
			// 用于存放修改爬虫账单的sql
			Map<Integer, StringBuffer> idsMap = new HashMap<>();
			while (rs3.next()) {
				Integer totalNum = rs3.getInt("totalNum");
				Integer pageSize = totalNum / PAGE_SIZE;
				if (totalNum % PAGE_SIZE != 0) {
					pageSize += 1;
				}
				
				List<AlipayDailyFeeModel> saveList = new ArrayList<>() ;
				
				for (int i = 0; i < pageSize; i++) {
					rs2 = pst2.executeQuery(queryStr + start + "," + end);
					
					saveList.clear(); 
					
					while (rs2.next()) {
						Long id = rs2.getLong("id");
						// 根据登录用户名获取爬虫配置信息
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("loginName", rs2.getString("userCode"));
						List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
						if (reptileConfigList == null || reptileConfigList.size() == 0) {
							LOGGER.error("获取不到相应的配置信息，账单id为：" + id);
							continue;
						}

//						String billCode = rs2.getString("billCode");
//						String dateStr = billCode.substring(3, 11);

						ReptileConfigMongo reptileConfigModel = reptileConfigList.get(0);
						// 根据配置获取商家信息
						Map<String, Object> params1 = new HashMap<String, Object>();
						params1.put("merchantId", reptileConfigModel.getMerchantId());
						MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(params1);
						
						// 保存爬虫账单
						AlipayDailyFeeModel model = new AlipayDailyFeeModel();
						model.setUserCode(rs2.getString("userCode"));
						model.setSettleNo(rs2.getString("settleNo"));
						model.setPartnerTransactionId(rs2.getString("partnerTransactionId"));
						model.setAmount(rs2.getBigDecimal("amount"));
						model.setRmbAmount(rs2.getBigDecimal("rmbAmount"));
						model.setGrossAmount(rs2.getBigDecimal("grossAmount"));
						model.setRate(rs2.getBigDecimal("rate"));
						model.setRemark(rs2.getString("remark"));
						model.setCurrency(rs2.getString("currency"));
						model.setAlipayCreateDate(rs2.getTimestamp("createdAt"));
						model.setAlipayModifyDate(rs2.getTimestamp("modifiedAt"));
						model.setMerchantId(merchantInfo.getMerchantId());
						model.setMerchantName(merchantInfo.getName());
						model.setOldId(Long.valueOf(rs2.getInt("id")));

						saveList.add(model) ;
						
						LOGGER.info("===========每日费用明细数据id：{}", rs2.getInt("id"));
						// 记录id
						ids.append(id + ",");
					}
					if (saveList.size()>0) {
						alipayDailyFeeDao.alipayBatchSave(saveList) ;
						idsMap.put(i, ids);
					}
					
					
					start += PAGE_SIZE;
					

					ids = new StringBuffer();
				}
			}
			Set<Integer> idsSet = idsMap.keySet();
			for (Integer i : idsSet) {
				StringBuffer idStringBuffer = idsMap.get(i);
				// 更新爬取数据的状态为：“已爬取”
				String idStr = "";
				if (idStringBuffer.length() > 0) {
					idStr = idStringBuffer.substring(0, idStringBuffer.length() - 1);
				}
				if (!"".equals(idStr)) {
					int result2 = pst.executeUpdate(updateStr + idStr + ")");
					
					LOGGER.debug("更新数：" + result2);
				}
			}
			// 没有异常事物提交 关闭其他
			conn.commit();
			if (rs2 != null) {
				rs2.close();
			}
			if (rs3 != null) {
				rs3.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (pst2 != null) {
				pst2.close();
			}
			if (pst3 != null) {
				pst3.close();
			}
			if (conn != null) {
				conn.close();
			}

		} catch (Exception e) {
			LOGGER.error("每日费用明细数据失败", e);
			conn.rollback();
			if (rs2 != null) {
				rs2.close();
			}
			if (rs3 != null) {
				rs3.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (pst2 != null) {
				pst2.close();
			}
			if (pst3 != null) {
				pst3.close();
			}
			if (conn != null) {
				conn.close();
			}
			throw new RuntimeException("每日费用明细数据失败");
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		LOGGER.info("vip end ...," + sf.format(date));
	}
	@Override
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_13201140014,model=DERP_LOG_POINT.POINT_13201140014_Label)
	public void insertMonthlyFee(String json, String keys, String topics, String tags) throws SQLException {
		JSONObject jsonData = JSONObject.fromObject(json);
		String loginName = (String) jsonData.get("loginName");
		String loginNameAppend = "";
		if (StringUtils.isNotBlank(loginName)) {
			loginNameAppend = " and userCode='" + loginName + "'";
		}

		Connection conn = null;
		Statement pst = null;
		Statement pst2 = null;
		ResultSet rs2 = null;
		Statement pst3 = null;
		ResultSet rs3 = null;
		String queryTotalStr = "select count(*) as totalNum from alipay_monthly_fee where  hasReceived = '0' "
				+ loginNameAppend;
		String queryStr = "select * from alipay_monthly_fee where  hasReceived = '0' " + loginNameAppend + " limit ";
		String updateStr = "update alipay_monthly_fee set hasReceived = '1' where id  in ( ";

		StringBuffer ids = new StringBuffer();
		LOGGER.info("请求统计sql:" + queryTotalStr);
		LOGGER.info("请求统计sql:" + queryStr);

		// 连接爬虫数据库
		try {
			Class.forName(ApolloUtilDB.jdbforName);
			conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,
					ApolloUtilDB.crawlerPassword);
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			pst = conn.createStatement();
			pst2 = conn.createStatement();
			pst3 = conn.createStatement();
			int start = 0;
			int end = PAGE_SIZE;
			rs3 = pst3.executeQuery(queryTotalStr);
			// 用于存放修改爬虫账单的sql
			Map<Integer, StringBuffer> idsMap = new HashMap<>();
			while (rs3.next()) {
				Integer totalNum = rs3.getInt("totalNum");
				Integer pageSize = totalNum / PAGE_SIZE;
				if (totalNum % PAGE_SIZE != 0) {
					pageSize += 1;
				}
				
				List<AlipayMonthlyFeeModel> saveList = new ArrayList<>() ;
				
				for (int i = 0; i < pageSize; i++) {
					rs2 = pst2.executeQuery(queryStr + start + "," + end);
					
					saveList.clear(); 
					
					while (rs2.next()) {
						Long id = rs2.getLong("id");
						// 根据登录用户名获取爬虫配置信息
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("loginName", rs2.getString("userCode"));
						List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
						if (reptileConfigList == null || reptileConfigList.size() == 0) {
							LOGGER.error("获取不到相应的配置信息，账单id为：" + id);
							continue;
						}

//						String billCode = rs2.getString("billCode");
//						String dateStr = billCode.substring(3, 11);

						ReptileConfigMongo reptileConfigModel = reptileConfigList.get(0);
						// 根据配置获取商家信息
						Map<String, Object> params1 = new HashMap<String, Object>();
						params1.put("merchantId", reptileConfigModel.getMerchantId());
						MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(params1);
						
						// 保存爬虫账单
						AlipayMonthlyFeeModel model = new AlipayMonthlyFeeModel();
						model.setUserCode(rs2.getString("userCode"));
						model.setSettleNo(rs2.getString("settleNo"));
						model.setPartnerTransactionId(rs2.getString("partnerTransactionId"));
						model.setSettleYearMonth(rs2.getString("settleYearMonth"));
						model.setForeignFeeAmount(rs2.getBigDecimal("foreignFeeAmount"));
						model.setFeeAmount(rs2.getBigDecimal("feeAmount"));
						model.setExchangeRate(rs2.getBigDecimal("exchangeRate"));
						model.setFeeDesc(rs2.getString("feeDesc"));
						model.setCurrency(rs2.getString("currency"));
						model.setAlipayCreateDate(rs2.getTimestamp("createdAt"));
						model.setAlipayModifyDate(rs2.getTimestamp("modifiedAt"));
						model.setMerchantId(merchantInfo.getMerchantId());
						model.setMerchantName(merchantInfo.getName());
						model.setFileKey(rs2.getString("fileKey"));
						model.setOriginalPartnerTransactionId(rs2.getString("originalPartnerTransactionId"));
						model.setOldId(Long.valueOf(rs2.getInt("id")));

						saveList.add(model) ;
						
						LOGGER.info("===========同步每月结算费用id：{}", rs2.getInt("id"));
						// 记录id
						ids.append(id + ",");
					}
					if (saveList.size()>0) {
						alipayMonthlyFeeDao.alipayBatchSave(saveList) ;
						idsMap.put(i, ids);
						
					}
					
					
					start += PAGE_SIZE;
					

					ids = new StringBuffer();
				}
			}
			Set<Integer> idsSet = idsMap.keySet();
			for (Integer i : idsSet) {
				StringBuffer idStringBuffer = idsMap.get(i);
				// 更新爬取数据的状态为：“已爬取”
				String idStr = "";
				if (idStringBuffer.length() > 0) {
					idStr = idStringBuffer.substring(0, idStringBuffer.length() - 1);
				}
				if (!"".equals(idStr)) {
					int result2 = pst.executeUpdate(updateStr + idStr + ")");
					
					LOGGER.debug("更新数：" + result2);
				}
			}
			// 没有异常事物提交 关闭其他
			conn.commit();
			if (rs2 != null) {
				rs2.close();
			}
			if (rs3 != null) {
				rs3.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (pst2 != null) {
				pst2.close();
			}
			if (pst3 != null) {
				pst3.close();
			}
			if (conn != null) {
				conn.close();
			}

		} catch (Exception e) {
			LOGGER.error("同步每月结算费用失败", e);
			conn.rollback();
			if (rs2 != null) {
				rs2.close();
			}
			if (rs3 != null) {
				rs3.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (pst2 != null) {
				pst2.close();
			}
			if (pst3 != null) {
				pst3.close();
			}
			if (conn != null) {
				conn.close();
			}
			throw new RuntimeException("同步每月结算费用失败");
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		LOGGER.info("vip end ...," + sf.format(date));
	}


	
}
