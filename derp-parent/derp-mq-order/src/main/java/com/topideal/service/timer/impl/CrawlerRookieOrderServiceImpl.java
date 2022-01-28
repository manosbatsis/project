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

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mongo.dao.MerchantShopRelMongoDao;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.CrawlerRookieOrderService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 获取菜鸟电商订单
 * @author 杨创
 *
 */
@Service
public class CrawlerRookieOrderServiceImpl implements CrawlerRookieOrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerRookieOrderServiceImpl.class);
	
	private static final Integer PAGE_SIZE = 1200 ;
	
	@Autowired
	private  ReptileConfigMongoDao reptileConfigMongoDao;	
	@Autowired
	private  MerchantShopRelMongoDao merchantShopRelMongoDao ;
	@Autowired
	private RMQProducer rocketMQProducer;

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201140002,model=DERP_LOG_POINT.POINT_13201140002_Label)
	public void getRookieOrderData(String json, String keys, String topics, String tags) throws SQLException {
		JSONObject jsonData = JSONObject.fromObject(json);
		String loginName = (String) jsonData.get("loginName");
		
		// 查询天猫的 爬虫数据信息
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(loginName)) {
			params.put("loginName", loginName);
		}
		params.put("platformType", "4");
		List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
		if (reptileConfigList == null || reptileConfigList.size() == 0) {			
			throw new RuntimeException("没有配置爬虫配置表");
		}
		//循环商家配置信息
		for (ReptileConfigMongo mongo : reptileConfigList) {			
			Long shopId = mongo.getShopId();
			if (shopId==null) continue;
			Map<String, Object> shopParams = new HashMap<String, Object>();
			shopParams.put("dataSourceCode", "5");//查询爬虫店铺数据
			shopParams.put("shopId", shopId);
			MerchantShopRelMongo merchantShopRelMongo = merchantShopRelMongoDao.findOne(shopParams);
			if (merchantShopRelMongo==null){
				LOGGER.info("没有查询到店铺信息");
				continue;
			} 
			if (!DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(merchantShopRelMongo.getShopTypeCode())) {
				LOGGER.info("运营类型必须是POP店");
				continue;
			}
			if (!DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRelMongo.getStoreTypeCode())) {
				LOGGER.info("店铺类型必须是单主店");
				continue;
			}
			// 根据店铺id查询店铺
			String loginNameMongo = mongo.getLoginName();
			if (StringUtils.isBlank(loginNameMongo))continue;
			saveSendData(mongo);
		}
						
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		LOGGER.info("vip end ...," + sf.format(date));
	}

	/**
	 * 
	 * @param loginNameMongo
	 * @throws SQLException 
	 */
	private void saveSendData(ReptileConfigMongo mongo) throws SQLException {
		
		String loginName = mongo.getLoginName();
		if (StringUtils.isBlank(loginName)) {
			return;
		}
		String loginNameAppend = "";
		loginNameAppend = " and userCode='" + loginName + "'";
		
		Connection conn = null;Statement pst = null;Statement pst2 = null;
		ResultSet rs2 = null;Statement pst3 = null;ResultSet rs3 = null;

		//分页查询订单
		String queryOrderStrLimit = "select DISTINCT tradeId from cainiao_order_info where  hasReceived = '0' " + loginNameAppend + "  limit ";		
		// 获取上面分页查询的订单数据
		String queryStr = "select * from cainiao_order_info where  hasReceived = '0' " + loginNameAppend + " and tradeId  in  (";
		// 修改为已经接收
		String updateStr = "update cainiao_order_info set hasReceived = '1' where tradeId  in ( ";

		
		List<String>updateStrList=new ArrayList<String>();
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
			while (true) {				
				rs2 = pst2.executeQuery(queryOrderStrLimit + start + "," + end);
				StringBuffer tradeIds=new StringBuffer();
				while (rs2.next()) {
					String tradeId = rs2.getString("tradeId");
					tradeIds.append("'"+tradeId+"',");
				}
				// 没有订单结束循环
				if (tradeIds.length()<=0) {
					break;
				}
				start += PAGE_SIZE;
				if (tradeIds.length()>0) {
					tradeIds.deleteCharAt(tradeIds.length() - 1);// 去掉最后一个字符串 即 去掉最后一个逗号
					tradeIds.append(")"); // 拼接小括号
					String queryTradeIdsStr=queryStr+tradeIds;
					rs3 = pst3.executeQuery(queryTradeIdsStr);					
					//封装推送单个爬虫电商订单表体
					Map<String, JSONArray>tradeIdMap=getOrderJSONObject(rs3);
					
					// 推送单个电商订单
					Set<String> keySet = tradeIdMap.keySet();
					for (String tradeId : keySet) {
						JSONArray jSONArray = tradeIdMap.get(tradeId);
						JSONObject jSONObject=new JSONObject();
						jSONObject.put("tradeId", tradeId);
						jSONObject.put("shopId", mongo.getShopId());
						jSONObject.put("shopName", mongo.getShopName());
						jSONObject.put("shopCode", mongo.getShopCode());						
						jSONObject.put("itemList", jSONArray);
						try {
							Thread.sleep(20);
							rocketMQProducer.send(jSONObject.toString(), MQOrderEnum.TIMER_CRAWLER_ROOKIE_GET_ONE_ORDER.getTopic(),MQOrderEnum.TIMER_CRAWLER_ROOKIE_GET_ONE_ORDER.getTags());				
						} catch (Exception e) {
							e.printStackTrace();
						}

						//System.out.println(jSONObject.toString());
					}
					//存储修改数据
					if (keySet.size()>0) {
						updateStrList.add(updateStr + tradeIds);
					}
				}		
			}
			//修改成已接收
			if (updateStrList.size()>0) {
				for (int i = 0; i < updateStrList.size(); i++) {
					String update = updateStrList.get(i);
					int result2 = pst.executeUpdate(update);
					LOGGER.debug("第"+i+"次更新数：" + result2);
				}
				//Thread.sleep(2000);
			}

			// 没有异常事物提交 关闭其他
			conn.commit();
			if (rs2 != null)rs2.close();
			if (rs3 != null)rs3.close();
			if (pst != null)pst.close();
			if (pst2 != null)pst2.close();
			if (pst3 != null)pst3.close();
			if (conn != null)conn.close();

		} catch (Exception e) {
			LOGGER.error("获取菜鸟电商订单数据失败", e);
			conn.rollback();
			if (rs2 != null)rs2.close();
			if (rs3 != null)rs3.close();
			if (pst != null) pst.close();
			if (pst2 != null)pst2.close();
			if (pst3 != null)pst3.close();
			if (conn != null)conn.close();

		}
		
	}

	/**
	 * 封装推送单个爬虫电商订单表体
	 * @param rs3
	 * @return
	 * @throws SQLException
	 */
	private Map<String, JSONArray> getOrderJSONObject(ResultSet rs3) throws SQLException {
		//封装推送单个爬虫电商订单表体
		Map<String, JSONArray>tradeIdMap=new HashMap<>();
		while (rs3.next()) {
			JSONObject jSONObject=new JSONObject();			
			String tradeId = rs3.getString("tradeId");
			
			jSONObject.put("tradeTime", TimeUtils.format(rs3.getTimestamp("tradeTime"), "yyyy-MM-dd HH:mm:ss"));
			jSONObject.put("consignTime",TimeUtils.format(rs3.getTimestamp("consignTime"), "yyyy-MM-dd HH:mm:ss"));
			jSONObject.put("tradeId", tradeId);
			jSONObject.put("logisticEnterprise", rs3.getString("logisticEnterprise"));
			jSONObject.put("waybillNo", rs3.getString("waybillNo"));
			jSONObject.put("consignee", rs3.getString("consignee"));
			jSONObject.put("phoneNumber", rs3.getString("phoneNumber"));
			jSONObject.put("cityAndDistrict", rs3.getString("cityAndDistrict"));
			jSONObject.put("address", rs3.getString("address"));
			jSONObject.put("amount", rs3.getBigDecimal("amount"));
			jSONObject.put("freight", rs3.getBigDecimal("freight"));
			jSONObject.put("quantity", rs3.getInt("quantity"));
			jSONObject.put("goodsCode", rs3.getString("goodsCode"));
			jSONObject.put("goodsId", rs3.getString("goodsId"));
			jSONObject.put("tax", rs3.getBigDecimal("tax"));
			jSONObject.put("taskId", rs3.getString("taskId"));
			jSONObject.put("userCode", rs3.getString("userCode"));
			jSONObject.put("createdAt",TimeUtils.format(rs3.getTimestamp("createdAt"), "yyyy-MM-dd HH:mm:ss"));
			jSONObject.put("modifiedAt", TimeUtils.format(rs3.getTimestamp("modifiedAt"), "yyyy-MM-dd HH:mm:ss"));
			if (tradeIdMap.containsKey(tradeId)) {
				JSONArray jSONArray  = tradeIdMap.get(tradeId);
				jSONArray.add(jSONObject);
				tradeIdMap.put(tradeId, jSONArray);
			}else {
				JSONArray jSONArray=new JSONArray();
				jSONArray.add(jSONObject);
				tradeIdMap.put(tradeId, jSONArray);
			}
		}
		return tradeIdMap;
	}

	
}
