package com.topideal.service.timer.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.PlatformPurchaseOrderDao;
import com.topideal.dao.sale.PlatformPurchaseOrderItemDao;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderDTO;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderItemModel;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderModel;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.CrawlerKLPurchaseOderService;

import net.sf.json.JSONObject;

@Service
public class CrawlerKLPurchaseOderServiceImpl implements CrawlerKLPurchaseOderService{
	
	@Autowired
	private ReptileConfigMongoDao reptileConfigMongoDao;
	@Autowired
	private PlatformPurchaseOrderDao platformPurchaseOrderDao ;
	@Autowired
	private PlatformPurchaseOrderItemDao platformPurchaseOrderItemDao ;

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201148602,model=DERP_LOG_POINT.POINT_13201148602_Label)
	public void insertKLPurchaseOder(String json, String keys, String topics, String tags) throws Exception {
		
		// 获取上个月1号时间
		Timestamp getfirstDate = TimeUtils.getfirstDate();
		String userCode = null ;
		
		// 获取指定日期前同步
		JSONObject jsonObj = JSONObject.fromObject(json);
		if(jsonObj.get("date") != null) {
			String date = jsonObj.getString("date") ;
			
			getfirstDate = TimeUtils.parse(date, "yyyy-MM-dd") ;
		}
		
		if(jsonObj.get("userCode") != null) {
			userCode = jsonObj.getString("userCode") ;
		}
		
		String lastMonthDay = TimeUtils.formatFullTime(getfirstDate);// 上个月第一天时间
		lastMonthDay = "'"+lastMonthDay+"'";
	    String queryTotalStr =  "SELECT COUNT(*) as totalNum from kl_new_purchase_order where  userCode = ? and (gmtCreate >= "+lastMonthDay + "or modifiedAt>="+lastMonthDay+")";
	    String queryStr = "SELECT *  from kl_new_purchase_order where  userCode = ? and (gmtCreate >= "+lastMonthDay + " or modifiedAt>="+lastMonthDay+" ) limit ?, ? ";
	    String queryItemStr = "SELECT *  from kl_new_purchase_order_detail where purchaseOrderNo = ? ";
	    
	    
	    Class.forName(ApolloUtilDB.jdbforName);
	    Connection conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,ApolloUtilDB.crawlerPassword);
		
	    if (conn==null) {
			throw new RuntimeException ("jdbc 链接失败");
		}
	    
	    Map<String, Object> queryMap = new HashMap<String, Object>() ;
	    
	    queryMap.put("platformType", DERP.CRAWLER_TYPE_5);
	    
	    if(StringUtils.isNotBlank(userCode)) {
	    	
	    	queryMap.put("loginName", userCode);
	    	
	    }
	    
	    List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(queryMap);
	    
	    PreparedStatement totalStatement = conn.prepareStatement(queryTotalStr);
	    PreparedStatement resultStatement = conn.prepareStatement(queryStr);
	    PreparedStatement resultItemStatement = conn.prepareStatement(queryItemStr);
	    
	    for (ReptileConfigMongo reptileConfigMongo : reptileConfigList) {
	    	
	    	List<PlatformPurchaseOrderDTO> dtoList = new ArrayList<PlatformPurchaseOrderDTO>() ;
			
	    	totalStatement.setString(1, reptileConfigMongo.getLoginName());
	    	
	    	ResultSet resultMap = totalStatement.executeQuery();
	    	
			while (resultMap.next()) {
				
				Integer totalNum = resultMap.getInt("totalNum");
				Integer pageSize = 1000;
				Integer pageNum = totalNum / pageSize ;
				
				if(totalNum % 1000 != 0){
					pageNum +=1;
				}
				
				for(int i = 0; i < pageNum; i++){
					
					int start = i * pageSize ;
					
					//设置userCode
					resultStatement.setString(1, reptileConfigMongo.getLoginName());
					//设置页数
					resultStatement.setInt(2, start);
					resultStatement.setInt(3, pageSize);
					
					ResultSet resultSet = resultStatement.executeQuery();
					
					while(resultSet.next()) {
						
						String poNo = resultSet.getString("bizId");
						
						PlatformPurchaseOrderDTO platformPurchaseOrderModel = new PlatformPurchaseOrderDTO() ;
						
						platformPurchaseOrderModel.setOrderSource(DERP.CRAWLER_TYPE_5);
						platformPurchaseOrderModel.setCustomerId(reptileConfigMongo.getCustomerId());
						platformPurchaseOrderModel.setCustomerName(reptileConfigMongo.getCustomerName());
						platformPurchaseOrderModel.setMerchantId(reptileConfigMongo.getMerchantId());
						platformPurchaseOrderModel.setMerchantName(reptileConfigMongo.getMerchantName());
						platformPurchaseOrderModel.setAmount(resultSet.getBigDecimal("totalAmount"));
						platformPurchaseOrderModel.setCurrency(resultSet.getString("currency"));
						platformPurchaseOrderModel.setDeliverDate(resultSet.getTimestamp("gmtReceiveFinish"));
						platformPurchaseOrderModel.setOrderTime(resultSet.getTimestamp("gmtCreate"));
						platformPurchaseOrderModel.setPoNo(poNo);
						platformPurchaseOrderModel.setPlatformDepotName(resultSet.getString("warehouseName"));
						
						String stateName = resultSet.getString("bizStatusDesc");
						//stateName = String.valueOf(DERP_ORDER.getKeyByLabel(DERP_ORDER.platformPurchase_platformStatusList, stateName)) ;
						
						platformPurchaseOrderModel.setPlatformStatus(stateName);
						
						// 构造表体
						resultItemStatement.setString(1, poNo);
						ResultSet itemResultSet = resultItemStatement.executeQuery();
						
						List<PlatformPurchaseOrderItemModel> itemList = generateItem(itemResultSet ,reptileConfigMongo) ;
						platformPurchaseOrderModel.setItemList(itemList);
						
						//汇总数量
						BigDecimal totalAmount = itemList.stream().map(PlatformPurchaseOrderItemModel::getAmount)
								.filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
						
						Integer num = itemList.stream().mapToInt(PlatformPurchaseOrderItemModel::getNum).sum() ;
						
						platformPurchaseOrderModel.setAmount(totalAmount);
						platformPurchaseOrderModel.setNum(num);
						
						dtoList.add(platformPurchaseOrderModel) ;
					}
					
				}
			}
			
			for (PlatformPurchaseOrderDTO dto : dtoList) {
				
				PlatformPurchaseOrderModel queryModel = new PlatformPurchaseOrderModel() ;
				queryModel.setMerchantId(dto.getMerchantId());
				queryModel.setCustomerId(dto.getCustomerId());
				queryModel.setPoNo(dto.getPoNo());
				
				queryModel = platformPurchaseOrderDao.searchByModel(queryModel) ;
				
				PlatformPurchaseOrderModel model = new PlatformPurchaseOrderModel() ;
				
				BeanUtils.copyProperties(dto, model);
				
				if(queryModel == null) {
					
					model.setStatus(DERP_ORDER.PLATFORM_PURCHASE_STATUS_2);
					model.setCreateDate(TimeUtils.getNow());
					model.setUserCode(reptileConfigMongo.getLoginName());
					platformPurchaseOrderDao.save(model) ;
					
				}else {
					
					model.setId(queryModel.getId());
					model.setModifyDate(TimeUtils.getNow());
					platformPurchaseOrderDao.modify(model) ;
					
				}
				
				List<PlatformPurchaseOrderItemModel> itemList = dto.getItemList();
				
				for (PlatformPurchaseOrderItemModel itemModel : itemList) {
					
					PlatformPurchaseOrderItemModel queryItemModel = new PlatformPurchaseOrderItemModel() ;
					queryItemModel.setOrderId(model.getId());
					queryItemModel.setPlatformBarcode(itemModel.getPlatformBarcode());
					
					queryItemModel = platformPurchaseOrderItemDao.searchByModel(queryItemModel) ;
					
					if(queryItemModel == null) {
						
						itemModel.setOrderId(model.getId());
						itemModel.setCreateDate(TimeUtils.getNow());
						
						platformPurchaseOrderItemDao.save(itemModel) ;
						
					}else {
						
						itemModel.setId(queryItemModel.getId());
						itemModel.setModifyDate(TimeUtils.getNow());
						
						platformPurchaseOrderItemDao.modify(itemModel) ;
					}
					
				}
			}
		}
	}

	/***
	 * 构造表体
	 * @param poNo
	 * @return
	 * @throws SQLException 
	 */
	private List<PlatformPurchaseOrderItemModel> generateItem(ResultSet itemResultSet, ReptileConfigMongo reptileConfigMongo) throws SQLException {
		
		List<PlatformPurchaseOrderItemModel> itemList = new ArrayList<PlatformPurchaseOrderItemModel>() ;
		
		while (itemResultSet.next()) {
			
			PlatformPurchaseOrderItemModel itemModel = new PlatformPurchaseOrderItemModel() ;
			
			itemModel.setMerchantId(reptileConfigMongo.getMerchantId());
			itemModel.setUserCode(reptileConfigMongo.getLoginName());
			itemModel.setPlatformBarcode(itemResultSet.getString("barcode"));
			itemModel.setPlatformGoodsName(itemResultSet.getString("title"));
			itemModel.setNum(itemResultSet.getInt("quantity"));
			itemModel.setPrice(itemResultSet.getBigDecimal("purchasePrice"));
			itemModel.setAmount(itemResultSet.getBigDecimal("purchaseAmountDec"));
			itemModel.setReceiptOkNum(itemResultSet.getInt("receivedNormalQty"));
			itemModel.setReceiptBadNum(itemResultSet.getInt("receivedDefectiveQty"));
			itemModel.setPlatformGoodsNo(itemResultSet.getString("scItemId"));
			
			itemList.add(itemModel) ;
			
		}
		
		return itemList;
	}
	

}
