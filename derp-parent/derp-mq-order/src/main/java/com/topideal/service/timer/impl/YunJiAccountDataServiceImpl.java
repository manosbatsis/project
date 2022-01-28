package com.topideal.service.timer.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.topideal.dao.sale.YunjiAccountDataDao;
import com.topideal.dao.sale.YunjiDeliveryDetailDao;
import com.topideal.dao.sale.YunjiReturnDetailDao;
import com.topideal.entity.vo.sale.YunjiAccountDataModel;
import com.topideal.entity.vo.sale.YunjiDeliveryDetailModel;
import com.topideal.entity.vo.sale.YunjiReturnDetailModel;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.YunJiAccountDataService;

import net.sf.json.JSONObject;

@Service
public class YunJiAccountDataServiceImpl implements YunJiAccountDataService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(YunJiAccountDataServiceImpl.class);

	@Autowired
	private YunjiAccountDataDao yunjiAccountDataDao ;
	@Autowired
	private ReptileConfigMongoDao reptileConfigMongoDao ;
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao ;
	@Autowired
	private YunjiDeliveryDetailDao yunjiDeliveryDetailDao ;
	@Autowired
	private YunjiReturnDetailDao yunjiReturnDetailDao ;
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201146200,model=DERP_LOG_POINT.POINT_13201146200_Label)
	public void saveAccountData(String json, String keys, String topics, String tags) throws Exception {
		
		/**查询SQL*/
		String queryAccountDataStr = " select * from yunji_account_data where hasReceived = '0' " ;
		String queryDeliveryDetailStr = " select settleId, orderId, skuNo, srcOrderId, itemName, normName, payTime, "
				+ " wmsDeliverTime, qty, settlementPrice, taxRate, settlementTotalPrice, settlementTaxPrice, "
				+ " businessData, userCode, createTime, id "
				+ " from yunji_delivery_detail where hasReceived = '0' " ;
		String queryDeliveryDetailNumStr = " select count(1) as totalNum from yunji_delivery_detail where hasReceived = '0' " ;
		String queryReturnDetailStr = " select * from yunji_return_detail where hasReceived = '0' " ;
		/**更新SQL*/
		String updateAccountDataStr = "update yunji_account_data set hasReceived = '1' where id = ? " ;
		String updateDeliveryDetailStr = "update yunji_delivery_detail set hasReceived = '1' where id = ? " ;
		String updateReturnDetailStr = "update yunji_return_detail set hasReceived = '1' where id = ? " ;
		
		JSONObject jsonData = JSONObject.fromObject(json);
		String loginName = (String)jsonData.get("loginName");
		String sId = (String)jsonData.get("settleId");
		
		//根据登录用户名获取爬虫配置信息
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(loginName)) {
			params.put("loginName", loginName);
		}
		
		List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
		if(reptileConfigList == null || reptileConfigList.size()==0){
			LOGGER.error("获取不到相应的爬虫配置信息");
			return ;
		}
		
		for (ReptileConfigMongo reptileConfigMongo : reptileConfigList) {
			//根据配置获取商家信息
			Map<String, Object> merchantParams = new HashMap<String, Object>();
			merchantParams.put("merchantId", reptileConfigMongo.getMerchantId());
			MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantParams);
			
			loginName = reptileConfigMongo.getLoginName() ;
			
			// 同步云集结算表
			syncAccount(loginName, sId, queryAccountDataStr, updateAccountDataStr, merchantInfo);
			// 同步云集退货明细表
			syncReturn(loginName, sId, queryReturnDetailStr, updateReturnDetailStr, merchantInfo);
			// 同步云集发货明细表
			syncDelivery(loginName, sId, queryDeliveryDetailNumStr, queryDeliveryDetailStr, updateDeliveryDetailStr, merchantInfo);
		}

	}
	
	/**
	 * 同步发货账单
	 * @param loginName
	 * @param sId
	 * @param queryStr
	 * @param updateStr
	 * @param reptileConfigMap
	 * @throws Exception
	 */
	private void syncDelivery(String loginName, String sId,
			String queryNumStr, String queryStr,String updateStr,
			MerchantInfoMongo merchantInfo) throws Exception {
		if (StringUtils.isNotBlank(loginName)) {
			queryNumStr += " AND userCode='"+loginName+"' ";
			queryStr += " AND userCode='"+loginName+"' ";
		}
		
		if(StringUtils.isNotBlank(sId)) {
			queryNumStr += " AND settleId = '"+ sId +"' " ;
			queryStr += " AND settleId = '"+ sId +"' " ;
		}
		
		//查询云集发货明细链接
		Connection deliveryDetailConn = null;
		Statement deliveryDetailNumPst = null;
		ResultSet deliveryDetailNumRs = null ;
		Statement deliveryDetailPst = null;
		PreparedStatement deliveryDetailUpPst = null ;
		ResultSet deliveryDetailRs = null ;
		
		try {
			//根据结算单据ID查询对应总量
			deliveryDetailConn = createSQLConn() ;
			deliveryDetailConn.setAutoCommit(false);
			deliveryDetailNumPst = deliveryDetailConn.createStatement() ;
			deliveryDetailNumRs = deliveryDetailNumPst.executeQuery(queryNumStr) ;
			
			Integer totalNum = 0 ;
			while(deliveryDetailNumRs.next()) {
				totalNum = deliveryDetailNumRs.getInt("totalNum");
			}
			
			int pageSize = 5000 ;
			int pageNum = totalNum/pageSize;
			if(totalNum%pageSize != 0){
				pageNum +=1;
			}
			
			deliveryDetailPst = deliveryDetailConn.createStatement() ;
			deliveryDetailUpPst = deliveryDetailConn.prepareStatement(updateStr) ;
			
			for(int i = 0 ; i < pageNum ; i ++) {
				int start = i * pageSize ;
				String pageSql = queryStr + " limit " + start + "," + pageSize ;
				LOGGER.info("分页查询发货明细sql:" + pageSql);
				
				//查询发货明细
				deliveryDetailRs = deliveryDetailPst.executeQuery(pageSql) ;
				
				while(deliveryDetailRs.next()) {
					
					Long oldId = deliveryDetailRs.getLong("id") ;
					
					YunjiDeliveryDetailModel deliveryDetailModel = new YunjiDeliveryDetailModel() ;
					deliveryDetailModel.setOldId(oldId);
					deliveryDetailModel = yunjiDeliveryDetailDao.searchByModel(deliveryDetailModel) ;
					if(deliveryDetailModel != null) {
						LOGGER.info("发货结id为：" + oldId + ", 已存在");
						continue ;
					}
					
					deliveryDetailModel = new YunjiDeliveryDetailModel() ;
					deliveryDetailModel.setSettleId(deliveryDetailRs.getString("settleId"));
					deliveryDetailModel.setOrderId(deliveryDetailRs.getString("orderId")) ;
					deliveryDetailModel.setSrcOrderId(deliveryDetailRs.getString("srcOrderId")) ;
					deliveryDetailModel.setSkuNo(deliveryDetailRs.getString("skuNo"));
					deliveryDetailModel.setItemName(deliveryDetailRs.getString("itemName"));
					deliveryDetailModel.setNormName(deliveryDetailRs.getString("normName"));
					deliveryDetailModel.setPayTime(deliveryDetailRs.getTimestamp("payTime"));
					deliveryDetailModel.setWmsdelivertime(deliveryDetailRs.getTimestamp("wmsDeliverTime"));
					deliveryDetailModel.setQty(deliveryDetailRs.getInt("qty"));
					deliveryDetailModel.setSettlementPrice(deliveryDetailRs.getBigDecimal("settlementPrice")) ;
					deliveryDetailModel.setTaxRate(deliveryDetailRs.getBigDecimal("taxRate")) ;
					deliveryDetailModel.setSettlementTotalPrice(deliveryDetailRs.getBigDecimal("settlementTotalPrice")) ;
					deliveryDetailModel.setSettlementTaxPrice(deliveryDetailRs.getBigDecimal("settlementTaxPrice")) ;
					deliveryDetailModel.setBusinessData(deliveryDetailRs.getTimestamp("businessData")) ;
					deliveryDetailModel.setUserCode(deliveryDetailRs.getString("userCode"));
					deliveryDetailModel.setMerchantId(merchantInfo.getMerchantId());
					deliveryDetailModel.setMerchantName(merchantInfo.getName());
					deliveryDetailModel.setIsUsed(DERP_ORDER.YJ_ACCOUNT_DATA_ISUSED_0);
					deliveryDetailModel.setOldId(oldId);
					deliveryDetailModel.setCreateTime(deliveryDetailRs.getTimestamp("createTime"));
					deliveryDetailModel.setCreateDate(TimeUtils.getNow());
					
					yunjiDeliveryDetailDao.save(deliveryDetailModel) ;
					
					deliveryDetailUpPst.setLong(1, oldId);
					deliveryDetailUpPst.addBatch();
					
				}
				
				
			}
			deliveryDetailUpPst.executeBatch() ;
			
			deliveryDetailConn.commit();
			
			closeSQLConn(null, deliveryDetailPst, deliveryDetailUpPst, deliveryDetailRs);
			closeSQLConn(deliveryDetailConn, deliveryDetailNumPst, null, deliveryDetailNumRs);
			
		} catch (Exception e) {
			closeSQLConn(null, deliveryDetailPst, deliveryDetailUpPst, deliveryDetailRs);
			closeSQLConn(deliveryDetailConn, deliveryDetailNumPst, null, deliveryDetailNumRs);
			
			LOGGER.error("云集结算爬虫发货账单生成失败 :" + e.getMessage());
			throw new RuntimeException("云集结算爬虫发货账单生成失败") ;
		}
	}
	
	/**
	 * 同步退货账单
	 * @param loginName
	 * @param sId
	 * @param queryStr
	 * @param updateStr
	 * @param reptileConfigMap
	 * @throws Exception
	 */
	private void syncReturn(String loginName, String sId,
			String queryStr, String updateStr, MerchantInfoMongo merchantInfo) throws Exception {
		if (StringUtils.isNotBlank(loginName)) {
			queryStr += " AND userCode='"+loginName+"' ";
		}
		
		if(StringUtils.isNotBlank(sId)) {
			queryStr += " AND settleId = '"+ sId +"'" ;
		}
		
		//查询云集退货明细链接
		Connection returnDetailConn = null;
		Statement returnDetailPst = null;
		PreparedStatement returnDetailUpPst = null ;
		ResultSet returnDetailRs = null ;
		
		try {
			//查询退货明细
			returnDetailConn = createSQLConn() ;
			returnDetailConn.setAutoCommit(false);
			returnDetailPst = returnDetailConn.createStatement() ;
			returnDetailUpPst = returnDetailConn.prepareStatement(updateStr) ;
			returnDetailRs = returnDetailPst.executeQuery(queryStr) ;
			LOGGER.info("查询退货明细SQL:" + queryStr );
			
			while(returnDetailRs.next()) {
				
				Long oldId = returnDetailRs.getLong("id") ;
				
				YunjiReturnDetailModel returnDetailModel = new YunjiReturnDetailModel() ;
				returnDetailModel.setOldId(oldId);
				returnDetailModel = yunjiReturnDetailDao.searchByModel(returnDetailModel) ;
				
				if(returnDetailModel != null) {
					LOGGER.info("退货id为："+ oldId + ", 已存在");	
					continue ;
				}
				
				returnDetailModel = new YunjiReturnDetailModel() ;
				returnDetailModel.setSettleId(returnDetailRs.getString("settleId"));
				returnDetailModel.setReturnOrderId(returnDetailRs.getString("returnOrderId"));
				returnDetailModel.setOrderId(returnDetailRs.getString("orderId")) ;
				returnDetailModel.setSrcOrderId(returnDetailRs.getString("srcOrderId")) ;
				returnDetailModel.setSkuNo(returnDetailRs.getString("skuNo"));
				returnDetailModel.setItemName(returnDetailRs.getString("itemName"));
				returnDetailModel.setNormName(returnDetailRs.getString("normName"));
				returnDetailModel.setPayTime(returnDetailRs.getTimestamp("payTime"));
				returnDetailModel.setQty(returnDetailRs.getInt("qty"));
				returnDetailModel.setSettlementPrice(returnDetailRs.getBigDecimal("settlementPrice")) ;
				returnDetailModel.setSettlementTotalPrice(returnDetailRs.getBigDecimal("settlementTotalPrice")) ;
				returnDetailModel.setSettlementTaxPrice(returnDetailRs.getBigDecimal("settlementTaxPrice")) ;
				returnDetailModel.setReturnType(returnDetailRs.getString("returnType"));
				returnDetailModel.setReturnFinishTime(returnDetailRs.getTimestamp("returnFinishTime")) ;
				returnDetailModel.setUserCode(returnDetailRs.getString("userCode"));
				returnDetailModel.setMerchantId(merchantInfo.getMerchantId());
				returnDetailModel.setMerchantName(merchantInfo.getName());
				returnDetailModel.setCreateTime(returnDetailRs.getTimestamp("createTime"));
				returnDetailModel.setIsUsed(DERP_ORDER.YJ_ACCOUNT_DATA_ISUSED_0);
				returnDetailModel.setOldId(oldId);
				returnDetailModel.setCreateDate(TimeUtils.getNow());
				
				yunjiReturnDetailDao.save(returnDetailModel) ;
				
				returnDetailUpPst.setLong(1, oldId);
				returnDetailUpPst.addBatch();
			}
			
			returnDetailUpPst.executeBatch() ;
			returnDetailConn.commit();
			
			closeSQLConn(returnDetailConn, returnDetailPst, returnDetailUpPst, returnDetailRs);
			
		} catch (Exception e) {
			closeSQLConn(returnDetailConn, returnDetailPst, returnDetailUpPst, returnDetailRs);
			
			LOGGER.error("云集结算爬虫退货账单生成失败 :" + e.getMessage());
			throw new RuntimeException("云集结算爬虫退货账单生成失败") ;
		}
	}
	
	/**
	 * 同步云集结算表
	 * @param loginName
	 * @param sId
	 * @param queryAccountDataStr
	 * @param updateAccountDataStr
	 * @param reptileConfigMap
	 * @throws Exception
	 */
	private void syncAccount(String loginName, String sId,
			String queryStr, String updateStr, MerchantInfoMongo merchantInfo) throws Exception {
		if (StringUtils.isNotBlank(loginName)) {
			queryStr += " AND userCode='"+loginName+"' ";
		}
		
		if(StringUtils.isNotBlank(sId)) {
			queryStr += " AND id = '"+ sId +"'" ;
		}
		
		LOGGER.info("请求统计sql:"+queryStr);	
		
		//查询云集结算账单链接
		Connection accountDataConn = null;
		Statement accountDataPst = null;
		PreparedStatement  accountDataUpPst = null ;
		ResultSet accountDataRs = null ;
		
		try {
			accountDataConn = createSQLConn() ;
			accountDataConn.setAutoCommit(false);
			accountDataPst = accountDataConn.createStatement();
			accountDataUpPst = accountDataConn.prepareStatement(updateStr);
			accountDataRs = accountDataPst.executeQuery(queryStr) ;
			LOGGER.info("查询结算表头SQL:" + queryStr);
			
			while(accountDataRs.next()) {
				//结算单号
				String settleId = accountDataRs.getString("id") ;
				//供应商编码
				String supplierCode = accountDataRs.getString("supplierCode") ;
				
				//根据结算单号和供应商编码查询是否存在记录
				YunjiAccountDataModel accountDataModel = new YunjiAccountDataModel() ;
				accountDataModel.setSettleId(settleId);
				accountDataModel.setSupplierCode(supplierCode);
				
				accountDataModel = yunjiAccountDataDao.searchByModel(accountDataModel) ;
				
				if(accountDataModel != null) {
					LOGGER.info("结算单编码：" +settleId + "，供应商编码：" + supplierCode + ", 已存在");	
					continue ;
				}
				
				//封装表头
				accountDataModel = new YunjiAccountDataModel() ;
				accountDataModel.setSettleId(settleId);
				accountDataModel.setSupplierCode(supplierCode);
				accountDataModel.setSupplierName(accountDataRs.getString("supplierName"));
				accountDataModel.setBusinessStartDate(accountDataRs.getDate("businessStartDate"));
				accountDataModel.setBusinessEndDate(accountDataRs.getDate("businessEndDate"));
				accountDataModel.setCurrencyType(accountDataRs.getString("currencyType"));
				accountDataModel.setDepartmentType(accountDataRs.getString("departmentType"));
				accountDataModel.setGoodsType(accountDataRs.getString("goodsType"));
				accountDataModel.setSettlementPriceTotal(accountDataRs.getBigDecimal("settlementPriceTotal"));
				accountDataModel.setTotalCostPrice(accountDataRs.getBigDecimal("totalCostPrice"));
				accountDataModel.setFinalPriceTotal(accountDataRs.getBigDecimal("finalPriceTotal"));
				accountDataModel.setMechanismCode(accountDataRs.getString("mechanismCode")) ;
				accountDataModel.setSettleDate(accountDataRs.getDate("settleDate"));
				accountDataModel.setUserCode(accountDataRs.getString("userCode"));
				accountDataModel.setStatus(accountDataRs.getString("status"));
				accountDataModel.setMerchantId(merchantInfo.getMerchantId());
				accountDataModel.setMerchantName(merchantInfo.getName());
				accountDataModel.setCreateTime(accountDataRs.getTimestamp("createTime"));
				accountDataModel.setCreateDate(TimeUtils.getNow());
				accountDataModel.setFileKey(accountDataRs.getString("fileKey"));

				yunjiAccountDataDao.save(accountDataModel) ;
				
				accountDataUpPst.setString(1, settleId);
				accountDataUpPst.addBatch();
			}
			
			accountDataUpPst.executeBatch() ;
			accountDataConn.commit();
			
			closeSQLConn(accountDataConn, accountDataPst, accountDataUpPst, accountDataRs);
			
		} catch (Exception e) {
			closeSQLConn(accountDataConn, accountDataPst, accountDataUpPst, accountDataRs);
			
			LOGGER.error("云集结算爬虫账单生成失败 :" + e.getMessage());
			throw new RuntimeException("云集结算爬虫账单生成失败") ;
		}
	}
	
	private void closeSQLConn(Connection conn, Statement st, PreparedStatement pst, ResultSet rs) throws SQLException {
		if(rs != null) {
			rs.close();
		}
		
		if(pst != null) {
			pst.close();
		}
		
		if(st != null) {
			st.close(); 
		}
		
		if(conn != null) {
			conn.close();
		}
	}
	
	/**
	 * 创建数据库链接
	 * @return
	 * @throws Exception
	 */
	private Connection createSQLConn() throws Exception {
		Class.forName(ApolloUtilDB.jdbforName);
		Connection conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,ApolloUtilDB.crawlerPassword);
	
		return conn ;
	}

}
