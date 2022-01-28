package com.topideal.service.timer.impl;

import java.math.BigDecimal;
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
import java.util.Set;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.PlatformPurchaseOrderDao;
import com.topideal.dao.sale.PlatformPurchaseOrderItemDao;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderItemModel;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.CrawlerJDPurchaseOderService;

import net.sf.json.JSONObject;

@Service
public class CrawlerJDPurchaseOderServiceImpl implements CrawlerJDPurchaseOderService{
	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerJDPurchaseOderServiceImpl.class);

	@Autowired
	MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	CustomerInfoMongoDao customerInfoMongoDao;

	@Autowired
	ReptileConfigMongoDao reptileConfigMongoDao;
	@Autowired	
	private PlatformPurchaseOrderDao platformPurchaseOrderDao;// 平台采购订单
	@Autowired	
	private PlatformPurchaseOrderItemDao platformPurchaseOrderItemDao;// 平台采购订单 表体


	
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201148400,model=DERP_LOG_POINT.POINT_13201148400_Label)
	public void insertJDPurchaseOder(String json,String keys,String topics,String tags) throws Exception {		
		// 获取上个月1号时间
		Timestamp getfirstDate = TimeUtils.getfirstDate();
		
		// 获取指定日期前同步
		JSONObject jsonObj = JSONObject.fromObject(json);
		if(jsonObj.get("date") != null) {
			String date = jsonObj.getString("date") ;
			
			getfirstDate = TimeUtils.parse(date, "yyyy-MM-dd") ;
		}
		
		String lastMonthDay = TimeUtils.formatFullTime(getfirstDate);// 上个月第一天时间
		lastMonthDay="'"+lastMonthDay+"'";
	    String queryTotalStr =  "SELECT COUNT(*) as totalNum from jd_purchase_order where 1=1 and (createdDate >="+lastMonthDay +" OR modifiedAt>="+lastMonthDay+")";	    	    
	    String queryStr = "SELECT * from jd_purchase_order where 1=1  and (createdDate >="+lastMonthDay +" OR modifiedAt>="+lastMonthDay+")"+ " limit ";	    
	    String queryItemTotalStr="SELECT COUNT(*) as totalNum from  jd_purchase_order_detail where orderId IN (";	 
	    String queryItemStr="SELECT * from  jd_purchase_order_detail where orderId IN (";		    
		//连接爬虫数据库
	    Class.forName(ApolloUtilDB.jdbforName);
	    Connection conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,ApolloUtilDB.crawlerPassword);
		if (conn==null) throw new RuntimeException ("jdbc 链接失败");
		conn.setAutoCommit(false);// 关闭事务自动提交
		Statement pst = conn.createStatement(); // 创建 表头查询总量
		Statement pst2 = conn.createStatement();// 创建 表头所查询
		Statement pst3 = conn.createStatement();// 在表头的基础上查询 表体总量
		Statement pst4 = conn.createStatement();// 在表头的基础上查询 表体
		ResultSet rs2=null;ResultSet rs3=null; ResultSet rs4=null;
		try {						
			int start = 0;
			int end = 1000;
			ResultSet rs = pst.executeQuery(queryTotalStr);
			while (rs.next()) {
				Integer totalNum = rs.getInt("totalNum");
				Integer pageSize = totalNum/1000;
				if(totalNum%1000 != 0){
					pageSize +=1;
				}
				for(int i=0;i<pageSize;i++){
					rs2 = pst2.executeQuery(queryStr + start + "," + end);
					//生成京东采购订表头
					Map<String, Object> resultMap = saveJDPurchaseOder(rs2);
					StringBuffer orderIds= (StringBuffer) resultMap.get("orderIds");
					Map<String, PlatformPurchaseOrderModel> orderMap = (Map<String, PlatformPurchaseOrderModel>) resultMap.get("orderMap");
					
					// 生产京东采购订单表体
					if (orderIds.length()>0)saveJDPurchaseOderItem(pst3,pst4,rs3,rs4,queryItemTotalStr,queryItemStr,orderIds,orderMap);
					start += 1000;
				}
			}
	
		}catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("同步京东平台采购订单失败");
			throw new  RuntimeException("同步京东平台采购订单失败");
		}finally {
			conn.commit();	
			if(conn != null)conn.close();
			if(pst != null)pst.close();if(pst2 != null)pst2.close();if(pst3 != null)pst3.close();
			if (rs2!=null)rs2.close();if (rs3!=null)rs3.close();if (rs4!=null)rs4.close();
			
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		LOGGER.info("平台采购订单生成 end ...,"+sf.format(date));
	}


	/**
	 * 生成京东平台采购订单表体
	 * @param pst3
	 * @param pst4
	 * @param queryItemTotalStr
	 * @param queryItemStr
	 * @param orderIds
	 * @throws SQLException 
	 */
	private void saveJDPurchaseOderItem(Statement pst3, Statement pst4,ResultSet rs3,ResultSet rs4, 
			String queryItemTotalStr, String queryItemStr,
			StringBuffer orderIds,
			Map<String, PlatformPurchaseOrderModel> orderMap) throws SQLException {
		queryItemTotalStr=queryItemTotalStr+orderIds;
		queryItemStr=queryItemStr+orderIds +" limit ";
		int start = 0;
		int end = 1000;
		rs3 = pst3.executeQuery(queryItemTotalStr);
		
		// 修改表头总金额/总数量
		Map<Long, PlatformPurchaseOrderModel> updatePurchaseMap=new HashMap<>();
		while (rs3.next()) {
			Integer totalNum = rs3.getInt("totalNum");
			Integer pageSize = totalNum/1000;
			if(totalNum%1000 != 0){
				pageSize +=1;
			}
			for(int i=0;i<pageSize;i++){
				rs4 = pst4.executeQuery(queryItemStr + start + "," + end);
				while (rs4.next()) {
					
					String orderId = rs4.getString("orderId");
					String goodsNo = rs4.getString("goodsNo");					
					String userCode = rs4.getString("userCode");
					//根据登录用户名获取爬虫配置信息
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("loginName", userCode);
					params.put("platformType", DERP.CRAWLER_TYPE_3);
					List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
					if(reptileConfigList == null || reptileConfigList.size()==0){
						LOGGER.error("获取不到相应的配置信息，采购订单号orderId为："+orderId);
						continue;
					}
					ReptileConfigMongo reptileConfigModel= reptileConfigList.get(0);
					//根据配置获取商家信息
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("merchantId", reptileConfigModel.getMerchantId());
					MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(params1);
					if(merchantInfo == null){
						LOGGER.error("爬虫配置表的商家id,找不到商家信息,商家id:"+reptileConfigModel.getMerchantId());
						continue;
					}
					
					String orderKey=reptileConfigModel.getMerchantId()+","+orderId;
					PlatformPurchaseOrderModel purchaseOrder = orderMap.get(orderKey);
					if (purchaseOrder==null) continue;
					
					String orderUserKey=purchaseOrder.getMerchantId()+","+purchaseOrder.getPoNo()+","+purchaseOrder.getUserCode();
					String itemUserKey=merchantInfo.getMerchantId()+","+orderId+","+userCode;
					//(表头商家+po 有多个用户 取最后一个用户)  表体 如果 商家+po+用户和头部一样不用生成
					if (!orderUserKey.equals(itemUserKey))continue;
					
					
							
					PlatformPurchaseOrderItemModel itemModel=new PlatformPurchaseOrderItemModel();
					itemModel.setPlatformGoodsName(rs4.getString("goodsName"));
					itemModel.setPlatformBarcode(rs4.getString("UPC"));
					itemModel.setNum(rs4.getInt("purchaseNum"));
					itemModel.setPrice(rs4.getBigDecimal("purchasePrice"));
					itemModel.setAmount(rs4.getBigDecimal("totalAmount"));
					itemModel.setReceiptOkNum(rs4.getInt("actualNum"));
					itemModel.setReceiptBadNum(0);
					itemModel.setMerchantId(purchaseOrder.getMerchantId());
					itemModel.setUserCode(userCode);
					itemModel.setOrderId(purchaseOrder.getId());
					itemModel.setPlatformGoodsNo(goodsNo);

					PlatformPurchaseOrderItemModel itemQuery=new PlatformPurchaseOrderItemModel();
					//itemQuery.setUserCode(platformPurchase.getUserCode());
					itemQuery.setOrderId(purchaseOrder.getId());
					itemQuery.setPlatformGoodsNo(goodsNo);
					itemQuery = platformPurchaseOrderItemDao.searchByModel(itemQuery);
					if (itemQuery!=null) {// 修改
						itemModel.setId(itemQuery.getId());
						itemModel.setModifyDate(TimeUtils.getNow());
						platformPurchaseOrderItemDao.modify(itemModel);
					}else {// 新增
						platformPurchaseOrderItemDao.save(itemModel);
					}			


					// 存储修改表头的 订单总金额和/数量
					PlatformPurchaseOrderModel upatePurchase = updatePurchaseMap.get(purchaseOrder.getId());
					if (upatePurchase==null)upatePurchase=new PlatformPurchaseOrderModel();
					Integer num = itemModel.getNum();
					BigDecimal amount = itemModel.getAmount();
					Integer updateNum = upatePurchase.getNum();
					BigDecimal updateAmount = upatePurchase.getAmount();
					if (num==null)num=0;
					if (amount==null)amount=new BigDecimal(0);
					if (updateNum==null)updateNum=0;
					if (updateAmount==null)updateAmount=new BigDecimal(0);
					
					upatePurchase.setId(purchaseOrder.getId());
					upatePurchase.setNum(num+updateNum);
					upatePurchase.setAmount(amount.add(updateAmount));
					updatePurchaseMap.put(purchaseOrder.getId(), upatePurchase);
				}

				start += 1000;

			}
		}
		
		// 修改平台采购订单的总金额
		Set<Long> purchaseSet = updatePurchaseMap.keySet();
		for (Long id : purchaseSet) {
			PlatformPurchaseOrderModel updatePurchaseModel = updatePurchaseMap.get(id);
			updatePurchaseModel.setModifyDate(TimeUtils.getNow());
			platformPurchaseOrderDao.modify(updatePurchaseModel);//修改平台采购订单
		}		
	}



	/**
	 * 生成京东采购订单表头
	 * @param rs2
	 * @throws SQLException 
	 */
	private  Map<String, Object>  saveJDPurchaseOder(ResultSet rs2) throws SQLException {
		Map<String, Object> resultMap=new HashMap<>();
		StringBuffer orderIds=new StringBuffer();	
		Map<String, PlatformPurchaseOrderModel>orderMap=new HashMap<>();
		while (rs2.next()) {			
			String orderId = rs2.getString("orderId");
			String userCode = rs2.getString("userCode");
			//根据登录用户名获取爬虫配置信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("loginName", userCode);
			params.put("platformType", DERP.CRAWLER_TYPE_3);
			List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
			if(reptileConfigList == null || reptileConfigList.size()==0){
				LOGGER.error("获取不到相应的配置信息，采购订单号orderId为："+orderId);
				continue;
			}
			ReptileConfigMongo reptileConfigModel= reptileConfigList.get(0);
			//根据配置获取商家信息
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("merchantId", reptileConfigModel.getMerchantId());
			MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(params1);
			if(merchantInfo == null){
				LOGGER.error("爬虫配置表的商家id,找不到商家信息,商家id:"+reptileConfigModel.getMerchantId());
				continue;
			}
			
			PlatformPurchaseOrderModel platformPurchase= new PlatformPurchaseOrderModel();
			platformPurchase.setOrderSource("1");//京东
			platformPurchase.setMerchantId(merchantInfo.getMerchantId());// 商家id
			platformPurchase.setMerchantName(merchantInfo.getName());//商家名称
			platformPurchase.setPlatformDepotName(rs2.getString("deliverCenterName"));// 客户仓库
			platformPurchase.setCustomerId(reptileConfigModel.getCustomerId());//客户id
			platformPurchase.setCustomerName(reptileConfigModel.getCustomerName());// 客户名称
			platformPurchase.setPoNo(orderId);// 采购订单号
			platformPurchase.setOrderTime(rs2.getTimestamp("createdDate"));//下单时间
			platformPurchase.setDeliverDate(rs2.getTimestamp("storageTime"));//发货时间			
			String currency = rs2.getString("currency");// 币种
			platformPurchase.setCurrency(currency);
			//platformPurchase.setAmount(amount);
			//platformPurchase.setNum(num);
			String stateName = rs2.getString("stateName");
					
			platformPurchase.setPlatformStatus(stateName);		
			platformPurchase.setUserCode(userCode);
			
			PlatformPurchaseOrderModel platformPurchaseQuery= new PlatformPurchaseOrderModel();
			//platformPurchaseQuery.setUserCode(model.getUserCode());;
			platformPurchaseQuery.setMerchantId(merchantInfo.getMerchantId());
			platformPurchaseQuery.setPoNo(orderId);
			platformPurchaseQuery = platformPurchaseOrderDao.searchByModel(platformPurchaseQuery);
			if (platformPurchaseQuery==null) {// 新增
				platformPurchase.setStatus(DERP_ORDER.PLATFORM_PURCHASE_STATUS_2);
				platformPurchaseOrderDao.save(platformPurchase);
			}else {//修改
				platformPurchase.setId(platformPurchaseQuery.getId());
				platformPurchase.setModifyDate(TimeUtils.getNow());
				platformPurchaseOrderDao.modify(platformPurchase);
			}

			orderIds.append("'"+orderId+"',");
			String orderKey=merchantInfo.getMerchantId()+","+rs2.getString("orderId");
			//不同用户 商家和bizId 一样 取最后一条
			orderMap.put(orderKey, platformPurchase);
		}
		if (orderIds.length()>0) {
			orderIds.deleteCharAt(orderIds.length() - 1);// 去掉最后一个字符串 即 去掉最后一个逗号
			orderIds.append(")"); // 拼接小括号
		}		
		resultMap.put("orderIds", orderIds);
		resultMap.put("orderMap", orderMap);
		return resultMap;
	}
	
}
