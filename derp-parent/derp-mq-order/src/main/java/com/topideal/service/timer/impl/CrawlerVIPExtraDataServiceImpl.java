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

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.CrawlerVipExtraDataDao;
import com.topideal.dao.sale.CrawlerVipFileDataDao;
import com.topideal.entity.vo.sale.CrawlerVipExtraDataModel;
import com.topideal.entity.vo.sale.CrawlerVipFileDataModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.CrawlerVIPExtraDataService;

import net.sf.json.JSONObject;

@Service
public class CrawlerVIPExtraDataServiceImpl implements CrawlerVIPExtraDataService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerVIPExtraDataServiceImpl.class);
	
	private static final Integer PAGE_SIZE = 3000 ;
	
	@Autowired
	private CrawlerVipExtraDataDao crawlerVipExtraDataDao;
	@Autowired
	MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	ReptileConfigMongoDao reptileConfigMongoDao;
	@Autowired
	CrawlerVipFileDataDao crawlerVipFileDataDao ;

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201148800,model=DERP_LOG_POINT.POINT_13201148800_Label)
	public void insertVIPCrawlerExtraData(String json, String keys, String topics, String tags) throws SQLException {
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
		String queryTotalStr = "select count(*) as totalNum from vip_crawl_extra_data where  hasReceived = '0' "
				+ loginNameAppend;
		String queryStr = "select * from vip_crawl_extra_data where  hasReceived = '0' " + loginNameAppend + " limit ";
		String updateStr = "update vip_crawl_extra_data set hasReceived = '1' where id  in ( ";

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
				
				List<CrawlerVipExtraDataModel> saveList = new ArrayList<>() ;
				
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
						CrawlerVipExtraDataModel crawlerVipExtraDataModel = new CrawlerVipExtraDataModel();
						crawlerVipExtraDataModel.setMerchantId(reptileConfigModel.getMerchantId());
						crawlerVipExtraDataModel.setMerchantName(merchantInfo.getName());
						crawlerVipExtraDataModel.setMerchantCode(merchantInfo.getCode());
						crawlerVipExtraDataModel.setCustomerId(reptileConfigModel.getCustomerId());
						crawlerVipExtraDataModel.setCustomerName(reptileConfigModel.getCustomerName());
						crawlerVipExtraDataModel.setBrandName(rs2.getString("brandName"));
						crawlerVipExtraDataModel.setUserCode(rs2.getString("userCode"));
						crawlerVipExtraDataModel.setTopidealCode(merchantInfo.getTopidealCode());
						crawlerVipExtraDataModel.setCurrencyCode(rs2.getString("currencyCode"));
						crawlerVipExtraDataModel.setProcessingType(rs2.getString("processingType"));
						crawlerVipExtraDataModel.setBillCode(rs2.getString("billCode"));
						crawlerVipExtraDataModel.setPoNo(rs2.getString("poNumber"));
						crawlerVipExtraDataModel.setOldId(rs2.getInt("id"));
						crawlerVipExtraDataModel.setCreateDate(TimeUtils.getNow());
						crawlerVipExtraDataModel.setSaleOrderCode(rs2.getString("so"));
						crawlerVipExtraDataModel.setVendorNumber(rs2.getString("vendorNumber"));
						crawlerVipExtraDataModel.setVendorName(rs2.getString("vendorName"));
						crawlerVipExtraDataModel.setGoodsNo(rs2.getString("goodsNumber"));
						crawlerVipExtraDataModel.setGoodsName(rs2.getString("goodsDescription"));
						crawlerVipExtraDataModel.setQuantity(rs2.getInt("quantity"));
						crawlerVipExtraDataModel.setAmount(rs2.getBigDecimal("amount"));
						crawlerVipExtraDataModel.setUserCode(rs2.getString("userCode"));
						crawlerVipExtraDataModel.setIsUsed(DERP_ORDER.CRAWLERBILL_ISUSED_0);

						saveList.add(crawlerVipExtraDataModel) ;
						
						LOGGER.info("===========生成唯品账单活动折扣明细id：{}", rs2.getInt("id"));
						// 记录id
						ids.append(id + ",");
					}
					
					crawlerVipExtraDataDao.batchSave(saveList) ;
					
					start += PAGE_SIZE;
					idsMap.put(i, ids);

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
			LOGGER.error("唯品账单活动折扣明细生成失败", e);
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
			throw new RuntimeException("唯品账单活动折扣明细生成失败");
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		LOGGER.info("vip end ...," + sf.format(date));
	}

	@Override
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_13201148801,model=DERP_LOG_POINT.POINT_13201148801_Label)
	public void insertVIPCrawlerFileData(String json, String keys, String topics, String tags) throws SQLException {
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
		String queryTotalStr = "select count(*) as totalNum from vip_file_data where  hasReceived = '0' "
				+ loginNameAppend;
		String queryStr = "select * from vip_file_data where  hasReceived = '0' " + loginNameAppend + " limit ";
		String updateStr = "update vip_file_data set hasReceived = '1' where id  in ( ";

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
				
				for (int i = 0; i < pageSize; i++) {
					rs2 = pst2.executeQuery(queryStr + start + "," + end);
					
					while (rs2.next()) {
						Long id = rs2.getLong("id");
						// 根据登录用户名获取爬虫配置信息
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("loginName", rs2.getString("userCode"));
						List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
						if (reptileConfigList == null || reptileConfigList.size() == 0) {
							LOGGER.error("获取不到相应的配置信息，文件id为：" + id);
							continue;
						}

						ReptileConfigMongo reptileConfigModel = reptileConfigList.get(0);
						// 根据配置获取商家信息
						Map<String, Object> params1 = new HashMap<String, Object>();
						params1.put("merchantId", reptileConfigModel.getMerchantId());
						MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(params1);
						
						//根据爬虫ID判断是否存在需要修改记录
						CrawlerVipFileDataModel queryModel = new CrawlerVipFileDataModel();
						queryModel.setOldId(id.intValue());
						
						queryModel = crawlerVipFileDataDao.searchByModel(queryModel) ;
						
						// 保存爬虫账单
						CrawlerVipFileDataModel crawlerVipFileDataModel = new CrawlerVipFileDataModel();
						crawlerVipFileDataModel.setMerchantId(reptileConfigModel.getMerchantId());
						crawlerVipFileDataModel.setMerchantName(merchantInfo.getName());
						crawlerVipFileDataModel.setMerchantCode(merchantInfo.getCode());
						crawlerVipFileDataModel.setCustomerId(reptileConfigModel.getCustomerId());
						crawlerVipFileDataModel.setCustomerName(reptileConfigModel.getCustomerName());
						crawlerVipFileDataModel.setUserCode(rs2.getString("userCode"));
						crawlerVipFileDataModel.setTopidealCode(merchantInfo.getTopidealCode());
						crawlerVipFileDataModel.setBillCode(rs2.getString("billCode"));
						crawlerVipFileDataModel.setOldId(rs2.getInt("id"));
						crawlerVipFileDataModel.setUserCode(rs2.getString("userCode"));
						crawlerVipFileDataModel.setFileKey(rs2.getString("fileKey"));
						
						if(queryModel == null) {
							crawlerVipFileDataModel.setCreateDate(TimeUtils.getNow());
							crawlerVipFileDataDao.save(crawlerVipFileDataModel) ;
						}else {
							crawlerVipFileDataModel.setId(queryModel.getId());
							crawlerVipFileDataModel.setModifyDate(TimeUtils.getNow());
							
							crawlerVipFileDataDao.modify(crawlerVipFileDataModel) ;
						}

						
						LOGGER.info("===========生成唯品账单活动折扣文件id：{}", rs2.getInt("id"));
						// 记录id
						ids.append(id + ",");
					}
					
					start += PAGE_SIZE;
					idsMap.put(i, ids);

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
			LOGGER.error("唯品账单活动折扣文件生成失败", e);
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
			throw new RuntimeException("唯品账单活动折扣文件生成失败");
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		LOGGER.info("vip end ...," + sf.format(date));
		
	}
}
