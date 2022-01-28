package com.topideal.service.timer.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.PropertiesUtil;
import com.topideal.dao.sale.CrawlerInventoryDao;
import com.topideal.entity.vo.sale.CrawlerInventoryModel;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.FetchingYunJiInventoryService;

@Service
public class FetchingYunJiInventoryServiceImpl implements FetchingYunJiInventoryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FetchingYunJiInventoryServiceImpl.class);
	
	@Autowired
	CrawlerInventoryDao crawlerInventoryDao; 
	@Autowired
	MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	ReptileConfigMongoDao reptileConfigMongoDao;
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201141260,model=DERP_LOG_POINT.POINT_13201141260_Label)
	public void fetchingData(String json, String keys, String topics, String tags) throws SQLException {
		Connection conn = null;
		ResultSet rs2 = null;
		Statement pst = null;
		Statement pst2 = null;
		Statement pst3 = null;
	    ResultSet rs3 = null;
	    String queryTotalStr = "select count(*) as totalNum from yunji_inventory_data where hasReceived = '0'";
		String queryStr = "select * from yunji_inventory_data where hasReceived = '0' limit ";
		String updateStr = "update yunji_inventory_data set hasReceived = '1' where id  in ( ";
		StringBuffer ids = new StringBuffer();
		// 连接爬虫数据库
		try {
			Class.forName(ApolloUtilDB.jdbforName);
			conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,ApolloUtilDB.crawlerPassword);
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			pst = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pst2 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
							LOGGER.error("获取不到相应的配置信息，库存id为："+id);
							continue;
						}
						ReptileConfigMongo reptileConfigModel= reptileConfigList.get(0);
						//根据配置获取商家信息
						Map<String, Object> params1 = new HashMap<String, Object>();
						params1.put("merchantId", reptileConfigModel.getMerchantId());
						MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(params1);
						// 注入信息
						CrawlerInventoryModel model = new CrawlerInventoryModel();
						model.setMerchantCode(merchantInfo.getCode());// 商家编码
						model.setMerchantId(reptileConfigModel.getMerchantId());// 商家id
						model.setProxyId(reptileConfigModel.getProxyId());//代理商家id
						model.setMerchantName(merchantInfo.getName());// 商家名称
						model.setSku(rs2.getString("skuNo"));// 商品sku
						model.setGoodsName(rs2.getString("skuNOName"));// 商品名称
						model.setInventoryNum(rs2.getInt("finalTotalNum"));// 商品库存数量
						model.setUserCode(rs2.getString("userCode"));// 用户编码
						model.setCustomerId(reptileConfigModel.getCustomerId());
						model.setCustomerName(reptileConfigModel.getCustomerName());
						model.setInventoryDate(new Timestamp(rs2.getDate("date").getTime()));
						if("云集退货仓（新）".equals(rs2.getString("warehouseName"))) {
							model.setDepotName("云集退货仓（新）");
						}else {
							model.setDepotName("云集代销仓");
						}
						model.setYunjiDepotName(rs2.getString("warehouseName"));
						model.setOldId(Long.valueOf(id));// 爬虫数据表id
						//仓库信息均写定
						model.setDepotId(Long.valueOf(16));// 仓库id
						model.setDepotCode("WMS_360_04");// 仓库编码
						crawlerInventoryDao.save(model);
						LOGGER.info("===========生成云集库存爬虫数据 id：{}",model.getId());
						// 记录id
						ids.append(id + ",");
					}
					start += 10000;
				}
			}
			String idStr = "";
			if (ids.length() > 0) {
				idStr = ids.substring(0, ids.length() - 1);
			}
			if (!"".equals(idStr)) {
				int result2 = pst.executeUpdate(updateStr + idStr + ")");
				if (result2 > 0) {
					conn.commit();
				} else {
					conn.rollback();
				}
			}
		} catch (Exception e) {
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		LOGGER.info("yunji inventory end ...," + sf.format(date));
	}

}
