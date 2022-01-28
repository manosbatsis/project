package com.topideal.service.timer.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.JdPurchaseOrderDao;
import com.topideal.dao.sale.JdPurchaseOrderDetailDao;
import com.topideal.dao.sale.PlatformPurchaseOrderDao;
import com.topideal.dao.sale.PlatformPurchaseOrderItemDao;
import com.topideal.dao.sale.TmallscmPurchaseOrderDao;
import com.topideal.dao.sale.TmallscmPurchaseOrderDetailDao;
import com.topideal.entity.vo.sale.JdPurchaseOrderDetailModel;
import com.topideal.entity.vo.sale.JdPurchaseOrderModel;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderItemModel;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderModel;
import com.topideal.entity.vo.sale.TmallscmPurchaseOrderDetailModel;
import com.topideal.entity.vo.sale.TmallscmPurchaseOrderModel;
import com.topideal.service.timer.SavePlatformPurchaseOrderService;

import net.sf.json.JSONObject;

/**
 * 
 * 生成/更新平台采购订单
 * @author 杨创 2020/08/11
 */
@Service
public class SavePlatformPurchaseOrderServiceImpl implements SavePlatformPurchaseOrderService {

	@Autowired	
	private PlatformPurchaseOrderDao platformPurchaseOrderDao;// 平台采购订单
	@Autowired	
	private PlatformPurchaseOrderItemDao platformPurchaseOrderItemDao;// 平台采购订单 表体
	@Autowired	
	private JdPurchaseOrderDao jdPurchaseOrderDao;// 京东平台采购订单 表头
	@Autowired	
	private JdPurchaseOrderDetailDao jdPurchaseOrderDetailDao;//京东 平台采购订单 表体
	@Autowired	
	private TmallscmPurchaseOrderDao tmallscmPurchaseOrderDao;// 天猫平台采购订单 表头
	@Autowired	
	private TmallscmPurchaseOrderDetailDao tmallscmPurchaseOrderDetailDao;// 天猫平台采购订单 表体
	
	
	
	

	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(SavePlatformPurchaseOrderServiceImpl.class);	

	
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201148600,model=DERP_LOG_POINT.POINT_13201148600_Label)
	public boolean saveJDPlatformPurchaseOrder(String json, String keys, String topics, String tags) throws Exception {
		LOGGER.info("生成或更新平台采购订单(京东)json:"+json);
		JSONObject jsonData = JSONObject.fromObject(json);
		// 获取上个月1号时间
		Timestamp getfirstDate = TimeUtils.getfirstDate();
		// 指定时间更新
		if(jsonData.get("date") != null) {
			String date = jsonData.getString("date") ;
			
			getfirstDate = TimeUtils.parse(date, "yyyy-MM-dd") ;
		}
		
		String lastMonthDay = TimeUtils.formatFullTime(getfirstDate);// 上个月第一天时间		
		int startIndex = 0;
		int pageSize = 1000;//每页1000
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("lastMonthDay", lastMonthDay);
		paramMap.put("pageSize", pageSize);
		paramMap.put("startIndex", startIndex);
				
		while(true){
			paramMap.put("startIndex", startIndex);
			List<JdPurchaseOrderModel> JdPurchaseOrderList = jdPurchaseOrderDao.getPlatformPurchaseList(paramMap);
			// 分页查询结束结束循环
			if (JdPurchaseOrderList==null||JdPurchaseOrderList.size()==0) {
				LOGGER.info("京东平台采购订单无数据");
				break;
			}
			for (JdPurchaseOrderModel model : JdPurchaseOrderList) {
				// 生成和更新平台采购订单
				PlatformPurchaseOrderModel platformPurchase=saveorUpdateOrder(model);
				// 生成和更新平台采购订单表体
				saveorUpdateOrderItem(model,platformPurchase);
			}
			startIndex+=1000;
		}
		
		LOGGER.info("生成或更新平台采购订单(京东)结束:");
		

			
		return true;
	}
	/**
	 * 生成货更新平台采购订单表体
	 * @param model
	 * @param platformPurchase
	 * @throws SQLException 
	 */
	private void saveorUpdateOrderItem(JdPurchaseOrderModel model, PlatformPurchaseOrderModel platformPurchase) throws SQLException {
		// 查询平台采购订单表体
		JdPurchaseOrderDetailModel detailQuery=new JdPurchaseOrderDetailModel();
		detailQuery.setOrderId(model.getOrderId());
		detailQuery.setUserCode(platformPurchase.getUserCode());
		List<JdPurchaseOrderDetailModel> detailList = jdPurchaseOrderDetailDao.list(detailQuery);
		
		Integer orderTotalNum=0;
		BigDecimal orderTotalAmount= new BigDecimal(0);
		for (JdPurchaseOrderDetailModel detailModel : detailList) {
			Integer purchaseNum = detailModel.getPurchaseNum();
			BigDecimal totalAmount = detailModel.getTotalAmount();
			if (purchaseNum==null) purchaseNum=0;
			orderTotalNum=orderTotalNum+purchaseNum;
			if (totalAmount==null) totalAmount=new BigDecimal(0);
			orderTotalAmount=orderTotalAmount.add(totalAmount);			
			PlatformPurchaseOrderItemModel itemModel=new PlatformPurchaseOrderItemModel();
			itemModel.setPlatformGoodsName(detailModel.getGoodsName());
			itemModel.setPlatformBarcode(detailModel.getUpc());
			itemModel.setNum(detailModel.getPurchaseNum());
			itemModel.setPrice(detailModel.getPurchasePrice());
			itemModel.setAmount(detailModel.getTotalAmount());
			itemModel.setReceiptOkNum(detailModel.getActualNum());
			itemModel.setReceiptBadNum(0);
			itemModel.setMerchantId(platformPurchase.getMerchantId());
			itemModel.setUserCode(platformPurchase.getUserCode());
			itemModel.setOrderId(platformPurchase.getId());
			itemModel.setPlatformGoodsNo(detailModel.getGoodsNo());

			PlatformPurchaseOrderItemModel itemQuery=new PlatformPurchaseOrderItemModel();
			//itemQuery.setUserCode(platformPurchase.getUserCode());
			itemQuery.setOrderId(platformPurchase.getId());
			itemQuery.setPlatformGoodsNo(detailModel.getGoodsNo());
			itemQuery = platformPurchaseOrderItemDao.searchByModel(itemQuery);
			if (itemQuery!=null) {// 修改
				itemModel.setId(itemQuery.getId());
				itemModel.setModifyDate(TimeUtils.getNow());
				platformPurchaseOrderItemDao.modify(itemModel);
			}else {// 新增
				platformPurchaseOrderItemDao.save(itemModel);
			}						
		}
		PlatformPurchaseOrderModel platform = new PlatformPurchaseOrderModel();
		platform.setId(platformPurchase.getId());
		platform.setNum(orderTotalNum);
		platform.setAmount(orderTotalAmount);
		platform.setModifyDate(TimeUtils.getNow());
		platformPurchaseOrderDao.modify(platform);//修改平台采购订单
		
	}
	/**
	 * 生产或更新平台采购订单
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	private PlatformPurchaseOrderModel saveorUpdateOrder(JdPurchaseOrderModel model) throws SQLException {
		PlatformPurchaseOrderModel platformPurchase= new PlatformPurchaseOrderModel();
		platformPurchase.setOrderSource("1");//京东
		platformPurchase.setMerchantId(model.getMerchantId());// 商家id
		platformPurchase.setMerchantName(model.getMerchantName());//商家名称
		platformPurchase.setPlatformDepotName(model.getDeliverCenterName());// 客户仓库
		platformPurchase.setCustomerId(model.getCustomerId());//客户id
		platformPurchase.setCustomerName(model.getCustomerName());// 客户名称
		platformPurchase.setPoNo(model.getOrderId());// 采购订单号
		platformPurchase.setOrderTime(model.getJdCreatedDate());//下单时间
		platformPurchase.setDeliverDate(model.getStorageTime());//发货时间
		platformPurchase.setCurrency(DERP.CURRENCYCODE_USD);
		//platformPurchase.setAmount(amount);
		//platformPurchase.setNum(num);
		String stateName = model.getStateName();
		if ("待发货确认".equals(stateName)) {
			stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_1;
		}else if("等待签收".equals(stateName)){
			stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_2;
		}else if("等待入库".equals(stateName)){
			stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_3;
		}else if("部分收货".equals(stateName)){
			stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_4;
		}else if("已完成".equals(stateName)){
			stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_5;
		}else if("等待审核".equals(stateName)){
			stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_9;
		}else if("待确认".equals(stateName)){
			stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_10;
		}else if("审核不通过".equals(stateName)){
			stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_11;
		}		
		platformPurchase.setPlatformStatus(stateName);		
		platformPurchase.setUserCode(model.getUserCode());
		
		PlatformPurchaseOrderModel platformPurchaseQuery= new PlatformPurchaseOrderModel();
		//platformPurchaseQuery.setUserCode(model.getUserCode());;
		platformPurchaseQuery.setMerchantId(model.getMerchantId());
		platformPurchaseQuery.setPoNo(model.getOrderId());
		platformPurchaseQuery = platformPurchaseOrderDao.searchByModel(platformPurchaseQuery);
		if (platformPurchaseQuery==null) {// 新增
			platformPurchase.setStatus(DERP_ORDER.PLATFORM_PURCHASE_STATUS_2);
			platformPurchaseOrderDao.save(platformPurchase);
		}else {//修改
			platformPurchase.setId(platformPurchaseQuery.getId());
			platformPurchase.setModifyDate(TimeUtils.getNow());
			platformPurchaseOrderDao.modify(platformPurchase);
		}


		return platformPurchase;
	}
	
	
	@Override
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_13201148700,model=DERP_LOG_POINT.POINT_13201148700_Label)
	public boolean saveTmallPlatformPurchaseOrder(String json, String keys, String topics, String tags)
			throws Exception {
		LOGGER.info("生成或更新平台采购订单(天猫)json:"+json);
		JSONObject jsonData = JSONObject.fromObject(json);
		// 获取上个月1号时间
		Timestamp getfirstDate = TimeUtils.getfirstDate();
		
		// 指定时间更新
		if(jsonData.get("date") != null) {
			String date = jsonData.getString("date") ;
			
			getfirstDate = TimeUtils.parse(date, "yyyy-MM-dd") ;
		}
		
		String lastMonthDay = TimeUtils.formatFullTime(getfirstDate);// 上个月第一天时间		
		int startIndex = 0;
		int pageSize = 1000;//每页1000
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("lastMonthDay", lastMonthDay);
		paramMap.put("pageSize", pageSize);
		paramMap.put("startIndex", startIndex);
			
		while(true){
			paramMap.put("startIndex", startIndex);
			List<TmallscmPurchaseOrderModel> tmallPlatformPurchaseList = tmallscmPurchaseOrderDao.getTmallPlatformPurchaseList(paramMap);
			// 分页查询结束结束循环
			if (tmallPlatformPurchaseList==null||tmallPlatformPurchaseList.size()==0) {
				LOGGER.info("京东平台采购订单无数据");
				break;
			}
			for (TmallscmPurchaseOrderModel model : tmallPlatformPurchaseList) {
				// 生成和更新平台采购订单
				PlatformPurchaseOrderModel platformPurchase=saveorUpdateTamllOrder(model);
				// 生成和更新平台采购订单表体
				saveorUpdateTmallOrderItem(model,platformPurchase);
			}
			startIndex+=1000;
			
		}
		
		LOGGER.info("生成或更新平台采购订单(天猫)结束:");
		return true;
	}
	
	/**
	 * 生成天猫平台采购订单表体
	 * @param model
	 * @param platformPurchase
	 * @throws SQLException 
	 */
	private void saveorUpdateTmallOrderItem(TmallscmPurchaseOrderModel model,PlatformPurchaseOrderModel platformPurchase) throws SQLException {

		// 查询平台采购订单表体
		TmallscmPurchaseOrderDetailModel detailQuery=new TmallscmPurchaseOrderDetailModel();
		detailQuery.setPurchaseOrderNo(model.getBizId());
		detailQuery.setUserCode(model.getUserCode());
		List<TmallscmPurchaseOrderDetailModel> detailList = tmallscmPurchaseOrderDetailDao.list(detailQuery);
		
		Integer orderTotalNum=0;
		BigDecimal orderTotalAmount= new BigDecimal(0);
		for (TmallscmPurchaseOrderDetailModel detailModel : detailList) {
			Integer purchaseNum = detailModel.getQuantity();
			BigDecimal totalAmount = detailModel.getPurchaseAmountDec();
			if (purchaseNum==null) purchaseNum=0;
			orderTotalNum=orderTotalNum+purchaseNum;
			if (totalAmount==null) totalAmount=new BigDecimal(0);
			orderTotalAmount=orderTotalAmount.add(totalAmount);	
			//封装实体
			PlatformPurchaseOrderItemModel itemModel=new PlatformPurchaseOrderItemModel();
			itemModel.setPlatformGoodsName(detailModel.getTitle());
			itemModel.setPlatformBarcode(detailModel.getBarcode());
			itemModel.setNum(detailModel.getQuantity());
			String priceOfYuan = detailModel.getPriceOfYuan();
			if (StringUtils.isNotBlank(priceOfYuan)){
				itemModel.setPrice(new BigDecimal(priceOfYuan));
			}
			
			itemModel.setAmount(totalAmount);
			itemModel.setReceiptOkNum(detailModel.getReceivedNormalQty());
			itemModel.setReceiptBadNum(detailModel.getReceivedDefectiveQty());
			itemModel.setOrderId(platformPurchase.getId());
			itemModel.setMerchantId(platformPurchase.getMerchantId());
			itemModel.setUserCode(platformPurchase.getUserCode());
			itemModel.setPlatformGoodsNo(detailModel.getScItemId());

			PlatformPurchaseOrderItemModel itemQuery=new PlatformPurchaseOrderItemModel();
			//itemQuery.setUserCode(platformPurchase.getUserCode());
			itemQuery.setOrderId(platformPurchase.getId());
			itemQuery.setPlatformGoodsNo(detailModel.getScItemId());
			itemQuery = platformPurchaseOrderItemDao.searchByModel(itemQuery);
			if (itemQuery!=null) {// 修改
				itemModel.setId(itemQuery.getId());
				itemModel.setModifyDate(TimeUtils.getNow());
				platformPurchaseOrderItemDao.modify(itemModel);
			}else {// 新增
				platformPurchaseOrderItemDao.save(itemModel);
			}						
		}
		PlatformPurchaseOrderModel platform = new PlatformPurchaseOrderModel();
		platform.setId(platformPurchase.getId());
		platform.setNum(orderTotalNum);
		platform.setAmount(orderTotalAmount);
		platform.setModifyDate(TimeUtils.getNow());
		platformPurchaseOrderDao.modify(platform);//修改平台采购订单
		
	}
	/**
	 * 生成京东平台采购订单表体
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	private PlatformPurchaseOrderModel saveorUpdateTamllOrder(TmallscmPurchaseOrderModel model) throws SQLException {
		PlatformPurchaseOrderModel platformPurchase= new PlatformPurchaseOrderModel();
		platformPurchase.setOrderSource("2");//天猫
		platformPurchase.setMerchantId(model.getMerchantId());// 商家id
		platformPurchase.setMerchantName(model.getMerchantName());//商家名称
		platformPurchase.setPlatformDepotName(model.getWarehouseName());// 客户仓库
		platformPurchase.setCustomerId(model.getCustomerId());//客户id
		platformPurchase.setCustomerName(model.getCustomerName());// 客户名称
		platformPurchase.setPoNo(model.getBizId());// 采购订单号
		platformPurchase.setOrderTime(model.getGmtCreate());//下单时间
		platformPurchase.setDeliverDate(model.getGmtReceiveFinish());//发货时间
		platformPurchase.setCurrency(model.getCurrency());
		//platformPurchase.setAmount(amount);
		//platformPurchase.setNum(num);
		String stateName = model.getBizStatusDesc();
		if ("待供应商发货".equals(stateName)) {
			stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_6;
		}else if("等待收货".equals(stateName)){
			stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_7;
		}else if("收货完成".equals(stateName)){
			stateName=DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_8;
		}		
		platformPurchase.setPlatformStatus(stateName);
		platformPurchase.setUserCode(model.getUserCode());
				
		PlatformPurchaseOrderModel platformPurchaseQuery= new PlatformPurchaseOrderModel();
		//platformPurchaseQuery.setUserCode(model.getUserCode());
		platformPurchaseQuery.setMerchantId(model.getMerchantId());
		platformPurchaseQuery.setPoNo(model.getBizId());
		platformPurchaseQuery = platformPurchaseOrderDao.searchByModel(platformPurchaseQuery);
		if (platformPurchaseQuery==null) {// 新增	
			platformPurchase.setStatus(DERP_ORDER.PLATFORM_PURCHASE_STATUS_2);
			platformPurchaseOrderDao.save(platformPurchase);
		}else {//修改			
			platformPurchase.setId(platformPurchaseQuery.getId());
			platformPurchase.setModifyDate(TimeUtils.getNow());
			platformPurchaseOrderDao.modify(platformPurchase);
		}
		return platformPurchase;
	}

	
			
	
}
