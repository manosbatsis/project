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
import org.apache.commons.lang.StringUtils;
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
import com.topideal.service.timer.CrawlerTmallPurchaseOderService;

import net.sf.json.JSONObject;

@Service
public class CrawlerTmallPurchaseOderServiceImpl implements CrawlerTmallPurchaseOderService{
	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerTmallPurchaseOderServiceImpl.class);

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
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201148500,model=DERP_LOG_POINT.POINT_13201148500_Label)
	public void insertTmallPurchaseOder(String json,String keys,String topics,String tags) throws Exception {		
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
	    String queryTotalStr =  "SELECT COUNT(*) as totalNum from tmallSCM_purchase_order where 1=1 and (gmtCreate >="+lastMonthDay +" OR modifiedAt >="+lastMonthDay+")";	    	    
	    String queryStr = "SELECT *  from tmallSCM_purchase_order where 1=1 and  (gmtCreate >="+lastMonthDay +" OR modifiedAt >="+lastMonthDay+") " + " limit ";	    
	    String queryItemTotalStr="SELECT COUNT(*) as totalNum from  tmallSCM_purchase_order_detail where purchaseOrderNo IN (";	 
	    String queryItemStr="SELECT * from  tmallSCM_purchase_order_detail where purchaseOrderNo IN (";		    
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
					//生成天猫采购订表头
					Map<String, Object> resultMap = saveTmallPurchaseOder(rs2);
					StringBuffer orderIds= (StringBuffer) resultMap.get("orderIds");
					Map<String, PlatformPurchaseOrderModel> orderMap = (Map<String, PlatformPurchaseOrderModel>) resultMap.get("orderMap");
					
					
					// 生产京天猫采购订单表体
					if (orderIds.length()>0){
						saveJDPurchaseOderItem(pst3,pst4,rs3,rs4,queryItemTotalStr,queryItemStr,orderIds,orderMap);
					}
						
					start += 1000;
				}
			}
	
		}catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("生成天猫平台采购订单失败");
			throw new  RuntimeException("生成天猫平台采购订单失败");
		}finally {
			conn.commit();	
			if(conn != null)conn.close();
			if(pst != null)pst.close();if(pst2 != null)pst2.close();if(pst3 != null)pst3.close();
			if (rs2!=null)rs2.close();if (rs3!=null)rs3.close();if (rs4!=null)rs4.close();
			
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		LOGGER.info("天猫平台采购订单生成 end ...,"+sf.format(date));
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
	private void saveJDPurchaseOderItem(Statement pst3, Statement pst4,ResultSet rs3,ResultSet rs4, String queryItemTotalStr, String queryItemStr,
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
					String purchaseOrderNo = rs4.getString("purchaseOrderNo");
					String scItemId = rs4.getString("scItemId");// 货号
					String userCode = rs4.getString("userCode");
					//根据登录用户名获取爬虫配置信息
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("loginName", userCode);
					params.put("platformType", DERP.CRAWLER_TYPE_4);
					List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
					if(reptileConfigList == null || reptileConfigList.size()==0){
						LOGGER.error("获取不到相应的配置信息，采购订单号purchaseOrderNo为："+purchaseOrderNo);
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
					String orderKey=reptileConfigModel.getMerchantId()+","+purchaseOrderNo;
					// 获取平台采购单
					PlatformPurchaseOrderModel purchaseOrder = orderMap.get(orderKey);
					if (purchaseOrder==null) continue;
					
					String orderUserKey=purchaseOrder.getMerchantId()+","+purchaseOrder.getPoNo()+","+purchaseOrder.getUserCode();
					String itemUserKey=merchantInfo.getMerchantId()+","+purchaseOrderNo+","+userCode;
					//(表头商家+po 有多个用户 取最后一个用户)  表体 如果 商家+po+用户和头部一样不用生成
					if (!orderUserKey.equals(itemUserKey))continue;
					
					//封装实体
					PlatformPurchaseOrderItemModel itemModel=new PlatformPurchaseOrderItemModel();
					itemModel.setPlatformGoodsName(rs4.getString("title"));
					itemModel.setPlatformBarcode(rs4.getString("barcode"));
					itemModel.setNum(rs4.getInt("quantity"));
					String priceOfYuan = rs4.getString("priceOfYuan");
					if (StringUtils.isNotBlank(priceOfYuan)){
						itemModel.setPrice(new BigDecimal(priceOfYuan));
					}
					
					itemModel.setAmount(rs4.getBigDecimal("purchaseAmountDec"));
					itemModel.setReceiptOkNum(rs4.getInt("receivedNormalQty"));
					itemModel.setReceiptBadNum(rs4.getInt("receivedDefectiveQty"));
					itemModel.setOrderId(purchaseOrder.getId());
					itemModel.setMerchantId(merchantInfo.getMerchantId());
					itemModel.setUserCode(userCode);
					itemModel.setPlatformGoodsNo(scItemId);

					PlatformPurchaseOrderItemModel itemQuery=new PlatformPurchaseOrderItemModel();
					//itemQuery.setUserCode(platformPurchase.getUserCode());
					itemQuery.setOrderId(purchaseOrder.getId());
					itemQuery.setPlatformGoodsNo(scItemId);
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
	private  Map<String, Object>  saveTmallPurchaseOder(ResultSet rs2) throws SQLException {
		
		Map<String, Object> resultMap=new HashMap<>();
		StringBuffer orderIds=new StringBuffer();	
		Map<String, PlatformPurchaseOrderModel>orderMap=new HashMap<>();
		while (rs2.next()) {			
			String bizId = rs2.getString("bizId");
			String userCode = rs2.getString("userCode");
			//根据登录用户名获取爬虫配置信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("loginName", userCode);
			params.put("platformType", DERP.CRAWLER_TYPE_4);
			List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
			if(reptileConfigList == null || reptileConfigList.size()==0){
				LOGGER.error("获取不到相应的配置信息，采购订单号bizId为："+bizId);
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
			platformPurchase.setOrderSource("2");//天猫
			platformPurchase.setMerchantId(merchantInfo.getMerchantId());// 商家id
			platformPurchase.setMerchantName(merchantInfo.getName());//商家名称
			platformPurchase.setPlatformDepotName(rs2.getString("warehouseName"));// 客户仓库
			platformPurchase.setCustomerId(reptileConfigModel.getCustomerId());//客户id
			platformPurchase.setCustomerName(reptileConfigModel.getCustomerName());// 客户名称
			platformPurchase.setPoNo(rs2.getString("bizId"));// 采购订单号
			platformPurchase.setOrderTime(rs2.getTimestamp("gmtCreate"));//下单时间
			platformPurchase.setDeliverDate(rs2.getTimestamp("gmtReceiveFinish"));//发货时间
			platformPurchase.setCurrency(rs2.getString("currency"));
			//platformPurchase.setAmount(amount);
			//platformPurchase.setNum(num);
			String stateName = rs2.getString("bizStatusDesc");
			/*if ("待供应商发货".equals(stateName)) {
				stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_6;
			}else if("等待收货".equals(stateName)){
				stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_7;
			}else if("收货完成".equals(stateName)){
				stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_8;
			}*/		
			platformPurchase.setPlatformStatus(stateName);
			platformPurchase.setUserCode(userCode);
					
			PlatformPurchaseOrderModel platformPurchaseQuery= new PlatformPurchaseOrderModel();
			//platformPurchaseQuery.setUserCode(model.getUserCode());
			platformPurchaseQuery.setMerchantId(merchantInfo.getMerchantId());
			platformPurchaseQuery.setPoNo(rs2.getString("bizId"));
			platformPurchaseQuery = platformPurchaseOrderDao.searchByModel(platformPurchaseQuery);
			if (platformPurchaseQuery==null) {// 新增	
				platformPurchase.setStatus(DERP_ORDER.PLATFORM_PURCHASE_STATUS_2);
				platformPurchaseOrderDao.save(platformPurchase);
			}else {//修改			
				platformPurchase.setId(platformPurchaseQuery.getId());
				platformPurchase.setModifyDate(TimeUtils.getNow());
				platformPurchaseOrderDao.modify(platformPurchase);
			}

			orderIds.append("'"+bizId+"',");
			String orderKey=merchantInfo.getMerchantId()+","+rs2.getString("bizId");
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
