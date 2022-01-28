package com.topideal.service.timer.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.CrawlerBillDao;
import com.topideal.entity.vo.sale.CrawlerBillModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.CrawlerBillService;

import net.sf.json.JSONObject;

@Service
public class CrawlerBillServiceImpl implements CrawlerBillService{
	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerBillServiceImpl.class);
	//爬虫账单
	@Autowired
	CrawlerBillDao crawlerBillDao;
	@Autowired
	MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	ReptileConfigMongoDao reptileConfigMongoDao;
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201141000,model=DERP_LOG_POINT.POINT_13201141000_Label)
	public void insertVIPCrawlerBill(String json,String keys,String topics,String tags) throws SQLException {
		
		JSONObject jsonData = JSONObject.fromObject(json);
		String loginName = (String)jsonData.get("loginName");
		String loginNameAppend="";
		if (StringUtils.isNotBlank(loginName)) {
			loginNameAppend= " and userCode='"+loginName+"'";
		}
		
		Connection conn = null;
		Statement pst = null;
	    Statement pst2 = null;
	    ResultSet rs2 = null;
	    Statement pst3 = null;
	    ResultSet rs3 = null;
	    String queryTotalStr = "select count(*) as totalNum from vip_crawl_data where ( processingType in ('Sales(销售)','Customer Return(客退)','Allocation(调拨)','Allocation(Reverse)(调拨-反向)','库存买断','Inventory shortage(库存盘亏)','Scrap(报废)','Inventory overage(库存盘盈)','Inspection inventory output(国检出库)') or processingType IS NULL ) and  hasReceived = '0' "+loginNameAppend;
		String queryStr = "select * from vip_crawl_data where ( processingType in ('Sales(销售)','Customer Return(客退)','Allocation(调拨)','Allocation(Reverse)(调拨-反向)','库存买断','Inventory shortage(库存盘亏)','Scrap(报废)','Inventory overage(库存盘盈)','Inspection inventory output(国检出库)') or processingType IS NULL )  and  hasReceived = '0' "+loginNameAppend+" limit ";
		String updateStr = "update vip_crawl_data set hasReceived = '1' where id  in ( ";
		StringBuffer ids = new StringBuffer();
		LOGGER.info("请求统计sql:"+queryTotalStr);
		LOGGER.info("请求统计sql:"+queryStr);	
		//销售明细包含类型
		String[] saleDetailsTypes = {"Sales(销售)","Customer Return(客退)","Allocation(调拨)","Allocation(Reverse)(调拨-反向)"} ;
		
		//连接爬虫数据库
		try {
			Class.forName(ApolloUtilDB.jdbforName);
			conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,ApolloUtilDB.crawlerPassword);
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			pst = conn.createStatement();
			pst2 = conn.createStatement();
			pst3 = conn.createStatement();
			int start = 0;
			int end = 10000;
			rs3 = pst3.executeQuery(queryTotalStr);
			// 用于存放修改爬虫账单的sql
			Map<Integer, StringBuffer> idsMap=new HashMap<>();			
			while (rs3.next()) {
				Integer totalNum = rs3.getInt("totalNum");
				Integer pageSize = totalNum/10000;
				if(totalNum%10000 != 0){
					pageSize +=1;
				}
				for(int i=0;i<pageSize;i++){
					rs2 = pst2.executeQuery(queryStr + start + "," + end);
					while (rs2.next()) {
						Long id = rs2.getLong("id");
						//根据登录用户名获取爬虫配置信息
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("loginName", rs2.getString("userCode"));
						List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
						if(reptileConfigList == null || reptileConfigList.size()==0){
							LOGGER.error("获取不到相应的配置信息，账单id为："+id);
							continue;
						}
						
						String billCode = rs2.getString("billCode");
						String dateStr = billCode.substring(3, 11);
						
						ReptileConfigMongo reptileConfigModel= reptileConfigList.get(0);
						//根据配置获取商家信息
						Map<String, Object> params1 = new HashMap<String, Object>();
						params1.put("merchantId", reptileConfigModel.getMerchantId());
						MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(params1);
						//获取客户信息
						Map<String, Object> params2 = new HashMap<String, Object>();
						params2.put("customerid", reptileConfigModel.getCustomerId());
						CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(params2);
						
						//保存爬虫账单
						CrawlerBillModel crawlerBillModel = new CrawlerBillModel();
						crawlerBillModel.setGoodsName(rs2.getString("itemDescription"));
						crawlerBillModel.setGoodsNo(rs2.getString("itemNumber"));
						crawlerBillModel.setType(DERP.CRAWLER_TYPE_2);
						crawlerBillModel.setMerchantId(reptileConfigModel.getMerchantId());
						crawlerBillModel.setMerchantName(merchantInfo.getName());
						crawlerBillModel.setCustomerId(reptileConfigModel.getCustomerId());
						crawlerBillModel.setCustomerName(customerInfo.getName());
						crawlerBillModel.setProxyId(reptileConfigModel.getProxyId());
						crawlerBillModel.setBrandName(rs2.getString("brandName"));
						crawlerBillModel.setUserCode(rs2.getString("userCode"));
						crawlerBillModel.setTopidealCode(merchantInfo.getTopidealCode());
						crawlerBillModel.setIsUsed(DERP_ORDER.CRAWLERBILL_ISUSED_0);
						crawlerBillModel.setBillAmount(rs2.getBigDecimal("billAmount"));
						crawlerBillModel.setBillCode(rs2.getString("billCode"));
						crawlerBillModel.setBillPrice(rs2.getBigDecimal("billPrice"));
						crawlerBillModel.setBillTaxPrice(rs2.getBigDecimal("billTaxPrice"));
						crawlerBillModel.setContractNumber(rs2.getString("contractNumber"));
						crawlerBillModel.setCurrencyCode(rs2.getString("currencyCode"));
						crawlerBillModel.setCustomerCode(customerInfo.getCode());
						crawlerBillModel.setDataSign(rs2.getString("dataSign"));
						crawlerBillModel.setFobType(rs2.getString("fobType"));
						crawlerBillModel.setHid(rs2.getString("hid"));
						crawlerBillModel.setLength(rs2.getString("length"));
						crawlerBillModel.setLotNumber(rs2.getString("lotNumber"));
						crawlerBillModel.setMerchandiser(rs2.getString("brandAdmin"));
						crawlerBillModel.setMerchantCode(merchantInfo.getCode());
						crawlerBillModel.setOriginPo(rs2.getString("originPo"));
						crawlerBillModel.setPoNo(rs2.getString("poNumber"));
						crawlerBillModel.setPoTime(rs2.getTimestamp("poTime"));
						crawlerBillModel.setCreateDate(TimeUtils.getNow());
						crawlerBillModel.setBillDate(TimeUtils.parse(dateStr, "yyyyMMdd"));
						crawlerBillModel.setOldId(rs2.getLong("id"));
						crawlerBillModel.setProcessingType(rs2.getString("processingType"));
						crawlerBillModel.setSaleOrderCode(rs2.getString("so"));
						crawlerBillModel.setScheduleName(rs2.getString("scheduleName"));
						crawlerBillModel.setScheduleNo(rs2.getString("scheduleNo"));
						crawlerBillModel.setTaxRate(rs2.getBigDecimal("taxRate"));
						crawlerBillModel.setTotalBillAmount(rs2.getBigDecimal("totalBillAmount"));
						crawlerBillModel.setTotalSalesQty(rs2.getInt("quantity"));
						crawlerBillModel.setUpcNo(rs2.getString("goodsNumber"));
						
						//账单类型字段(00-销售明细、01-库存买断、02库存盘亏、03报废、04库存盘盈、05国检抽样)
						if(Arrays.asList(saleDetailsTypes).contains(rs2.getString("processingType"))
								|| StringUtils.isBlank(rs2.getString("processingType"))) {
							crawlerBillModel.setBillType(DERP_ORDER.CRAWLER_BILLTYPE_00);
						}else if("库存买断".equals(rs2.getString("processingType"))) {
							crawlerBillModel.setBillType(DERP_ORDER.CRAWLER_BILLTYPE_01);
						}else if("Inventory shortage(库存盘亏)".equals(rs2.getString("processingType"))) {
							crawlerBillModel.setBillType(DERP_ORDER.CRAWLER_BILLTYPE_02);
						}else if("Scrap(报废)".equals(rs2.getString("processingType"))) {
							crawlerBillModel.setBillType(DERP_ORDER.CRAWLER_BILLTYPE_03);
						}else if("Inventory overage(库存盘盈)".equals(rs2.getString("processingType"))) {
							crawlerBillModel.setBillType(DERP_ORDER.CRAWLER_BILLTYPE_04);
						}else if("Inspection inventory output(国检出库)".equals(rs2.getString("processingType"))) {
							crawlerBillModel.setBillType(DERP_ORDER.CRAWLER_BILLTYPE_05);
						}
						
						crawlerBillDao.save(crawlerBillModel);
						LOGGER.info("===========生成爬虫账单id：{}",crawlerBillModel.getId());
						//记录id
						ids.append(id+",");
					}
					start += 10000;
					idsMap.put(i, ids);
					
					ids=new StringBuffer();
				}
			}
			Set<Integer> idsSet = idsMap.keySet();
			for (Integer i : idsSet) {
				StringBuffer idStringBuffer = idsMap.get(i);
				//更新爬取数据的状态为：“已爬取”
				String idStr = "";
				if(idStringBuffer.length()>0){
					idStr = idStringBuffer.substring(0, idStringBuffer.length()-1);
				}
				if(!"".equals(idStr)){
					int result2 = pst.executeUpdate(updateStr + idStr+")");					
				}
			}
			// 没有异常事物提交 关闭其他 
			conn.commit();
			if(rs2 != null){
				rs2.close();
			}
			if(rs3 != null){
				rs3.close();
			}
			if(pst != null){
				pst.close();
			}
			if(pst2 != null){
				pst2.close();
			}
			if(pst3 != null){
				pst3.close();
			}
			if(conn != null){
				conn.close();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			conn.rollback();						
			if(rs2 != null){
				rs2.close();
			}
			if(rs3 != null){
				rs3.close();
			}
			if(pst != null){
				pst.close();
			}
			if(pst2 != null){
				pst2.close();
			}
			if(pst3 != null){
				pst3.close();
			}
			if(conn != null){
				conn.close();
			}
			LOGGER.error("唯品爬虫账单生成失败");
			throw new  RuntimeException("唯品爬虫账单生成失败");
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		LOGGER.info("vip end ...,"+sf.format(date));
	}
	
	
	@Override
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_13201141001,model=DERP_LOG_POINT.POINT_13201141001_Label)
	public void insertYunJiCrawlerBill(String json,String keys,String topics,String tags) throws SQLException {
		Connection conn = null;
		Statement pst = null;
	    Statement pst2 = null;
	    ResultSet rs2 = null;
	    Statement pst3 = null;
	    ResultSet rs3 = null;
	    String queryTotalStr = "select count(*) as totalNum from yunji_crawl_data where hasReceived = '0'";
	    String queryStr = "select * from yunji_crawl_data where hasReceived = '0' limit " ;
		String updateStr = "update yunji_crawl_data set hasReceived = '1' where id  in ( ";
		StringBuffer ids = new StringBuffer();
		// 用于存放修改爬虫账单的sql
		Map<Integer, StringBuffer> idsMap=new HashMap<>();	
		//连接爬虫数据库
		try {
			Class.forName(ApolloUtilDB.jdbforName);
			conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,ApolloUtilDB.crawlerPassword);
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			pst = conn.createStatement();
			pst2 = conn.createStatement();
			pst3 = conn.createStatement();
			int start = 0;
			int end = 10000;
			rs3 = pst3.executeQuery(queryTotalStr);
			while (rs3.next()) {
				Integer totalNum = rs3.getInt("totalNum");
				Integer pageSize = totalNum/10000;
				if(totalNum%10000 != 0){
					pageSize +=1;
				}
				for(int i=0;i<pageSize;i++){
					rs2 = pst2.executeQuery(queryStr + start + "," + end);
					while (rs2.next()) {
						Long id = rs2.getLong("id");
						//根据登录用户名获取爬虫配置信息
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("loginName", rs2.getString("userCode"));
						List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
						if(reptileConfigList == null || reptileConfigList.size()==0){
							LOGGER.error("获取不到相应的配置信息，账单id为："+id);
							continue;
						}
						ReptileConfigMongo reptileConfigModel= reptileConfigList.get(0);
						//根据配置获取商家信息
						Map<String, Object> params1 = new HashMap<String, Object>();
						params1.put("merchantId", reptileConfigModel.getMerchantId());
						MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(params1);
						//获取客户信息
						Map<String, Object> params2 = new HashMap<String, Object>();
						params2.put("customerid", reptileConfigModel.getCustomerId());
						CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(params2);
						//保存爬虫账单
						CrawlerBillModel crawlerBillModel = new CrawlerBillModel();
						crawlerBillModel.setGoodsNo(rs2.getString("itemCode"));
						crawlerBillModel.setType(DERP.CRAWLER_TYPE_1); // 1 云集 ，2唯品
						crawlerBillModel.setGoodsName(rs2.getString("skuName"));
						crawlerBillModel.setBrandName(rs2.getString("brandName"));
						crawlerBillModel.setNormName(rs2.getString("normName"));
						crawlerBillModel.setGoodsName(rs2.getString("skuName"));
						crawlerBillModel.setTotalReturnQty(rs2.getInt("totalReturnQty"));
						crawlerBillModel.setTotalSalesQty(rs2.getInt("totalSalesQty"));
						crawlerBillModel.setUserCode(rs2.getString("userCode"));
						crawlerBillModel.setMerchantId(reptileConfigModel.getMerchantId());
						crawlerBillModel.setMerchantName(merchantInfo.getName());
						crawlerBillModel.setTopidealCode(merchantInfo.getTopidealCode());
						crawlerBillModel.setCustomerId(reptileConfigModel.getCustomerId());
						crawlerBillModel.setCustomerName(customerInfo.getName());
						crawlerBillModel.setProxyId(reptileConfigModel.getProxyId());
						crawlerBillModel.setCreateDate(TimeUtils.getNow());
						crawlerBillModel.setOldId(rs2.getLong("id"));
						Date date = rs2.getDate("cntDate");
						Calendar cal = Calendar.getInstance();
				        cal.setTime(date);  
				        crawlerBillModel.setBillDate(new Timestamp(cal.getTime().getTime()));
						crawlerBillDao.save(crawlerBillModel);
						LOGGER.info("===========生成爬虫账单id：{}",crawlerBillModel.getId());
						//记录id
						ids.append(id+",");
					}
					start += 10000;
					idsMap.put(i, ids);					
					ids=new StringBuffer();
				}
			}
			
			Set<Integer> idsSet = idsMap.keySet();
			for (Integer i : idsSet) {
				StringBuffer idStringBuffer = idsMap.get(i);
				//更新爬取数据的状态为：“已爬取”
				String idStr = "";
				if(idStringBuffer.length()>0){
					idStr = idStringBuffer.substring(0, idStringBuffer.length()-1);
				}
				if(!"".equals(idStr)){
					int result2 = pst.executeUpdate(updateStr + idStr+")");					
				}
			}
			// 没有异常事物提交 关闭其他 
			conn.commit();
			if(rs2 != null){
				rs2.close();
			}
			if(rs3 != null){
				rs3.close();
			}
			if(pst != null){
				pst.close();
			}
			if(pst2 != null){
				pst2.close();
			}
			if(pst3 != null){
				pst3.close();
			}
			if(conn != null){
				conn.close();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			if(rs2 != null){
				rs2.close();
			}
			if(rs3 != null){
				rs3.close();
			}
			if(pst != null){
				pst.close();
			}
			if(pst2 != null){
				pst2.close();
			}
			if(pst3 != null){
				pst3.close();
			}
			if(conn != null){
				conn.close();
			}
			LOGGER.error("云集爬虫账单生成失败");
			throw new  RuntimeException("云集爬虫账单生成失败");
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		LOGGER.info("yunji end ...,"+sf.format(date));
	}
}
