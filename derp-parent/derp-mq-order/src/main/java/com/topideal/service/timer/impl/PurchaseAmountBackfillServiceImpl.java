package com.topideal.service.timer.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseOrderItemDao;
import com.topideal.dao.purchase.WarehouseOrderRelDao;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.common.CommonBusinessService;
import com.topideal.service.timer.PurchaseAmountBackfillService;

@Service
public class PurchaseAmountBackfillServiceImpl implements PurchaseAmountBackfillService{

	@Autowired
	private PurchaseOrderDao purchaseOrderDao ;
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao ;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao ;
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201119400,model=DERP_LOG_POINT.POINT_13201119400_Label)
	public void savePurchaseAmountBackfill(String json, String keys, String topics, String tags) throws SQLException {
		
		Map<String, Object> paramMap = new HashMap<>() ;
		
		JSONObject jSONObject = (JSONObject)JSONObject.parse(json);
		
		if(jSONObject.get("codes") != null) {
			paramMap.put("codes", jSONObject.getString("codes")) ;
		}
		
		/**
		 * 获取本月和前月的无入库本位币金额的采购单
		 */
		List<PurchaseOrderModel> purchaseList = purchaseOrderDao.getNoTgtAmountOrder(paramMap) ;
		
		for (PurchaseOrderModel purchaseOrderModel : purchaseList) {
			List<PurchaseWarehouseModel> warehouseList = warehouseOrderRelDao.getInboundDateById(purchaseOrderModel.getId());
			
			for (PurchaseWarehouseModel purchaseWarehouseModel : warehouseList) {
				commonBusinessService.saveRate(purchaseOrderModel, purchaseWarehouseModel.getInboundDate());
			}
		}
		
		
	}

	@Override
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_13201119401,model=DERP_LOG_POINT.POINT_13201119401_Label)
	public void savePurchaseInvoiceInfo(String json, String keys, String topics, String tags) throws SQLException {
		
		Map<String, Object> map = new HashMap<>() ;
		map.put("code", "ERP31194100022") ;
		
		MerchantInfoMongo bxMerchant = merchantInfoMongoDao.findOne(map);
		
		map = new HashMap<>() ;
		map.put("code", "ERP16114000043") ;
		
		MerchantInfoMongo jbMerchant = merchantInfoMongoDao.findOne(map);
		
		/**
		 * 嘉宝的采购发票同步到宝信
		 */
		Map<String, Object> queryOrderMap = new HashMap<>() ;
		queryOrderMap.put("merchantId", bxMerchant.getMerchantId()) ;
		
		List<PurchaseOrderModel> purchaseList = purchaseOrderDao.getNoneInvoiceOrderList(queryOrderMap) ;
		
		for (PurchaseOrderModel purchaseOrderModel : purchaseList) {
			
			String poNo = purchaseOrderModel.getPoNo();
			
			PurchaseOrderModel queryModel = new PurchaseOrderModel() ;
			queryModel.setPoNo(poNo);
			queryModel.setMerchantId(jbMerchant.getMerchantId());
			
			List<PurchaseOrderModel> jbOrderList = purchaseOrderDao.list(queryModel);
			
			for (PurchaseOrderModel jbTempOrder : jbOrderList) {
				
				PurchaseOrderModel updateModel = new PurchaseOrderModel() ;
				
				if(jbTempOrder.getInvoiceName() != null) {
					updateModel.setId(purchaseOrderModel.getId());
					updateModel.setInvoiceName(jbTempOrder.getInvoiceName());
				}
				
				if(jbTempOrder.getDrawInvoiceDate() != null) {
					updateModel.setId(purchaseOrderModel.getId());
					updateModel.setDrawInvoiceDate(jbTempOrder.getDrawInvoiceDate());
				}
				
				if(jbTempOrder.getCargoCuttingDate() != null) {
					updateModel.setId(purchaseOrderModel.getId());
					updateModel.setCargoCuttingDate(jbTempOrder.getCargoCuttingDate());
				}
				
				if(jbTempOrder.getBillStatus() != null) {
					updateModel.setId(purchaseOrderModel.getId());
					updateModel.setBillStatus(jbTempOrder.getBillStatus());
				}
				
				if(jbTempOrder.getInvoiceNo() != null) {
					updateModel.setId(purchaseOrderModel.getId());
					updateModel.setInvoiceNo(jbTempOrder.getInvoiceNo());
				}
				
				if(updateModel.getId() != null) {
					updateModel.setModifyDate(TimeUtils.getNow());
					
					purchaseOrderDao.modify(updateModel) ;
				}
				
				PurchaseOrderItemModel queryItemModel = new PurchaseOrderItemModel() ;
				queryItemModel.setPurchaseOrderId(jbTempOrder.getId());
				
				List<PurchaseOrderItemModel> goodsList = purchaseOrderItemDao.list(queryItemModel);
				
				for (PurchaseOrderItemModel itemModel : goodsList) {
					
					if(itemModel.getInvoiceAmount() == null) {
						continue ;
					}
						
					PurchaseOrderItemModel tempItemModel = new PurchaseOrderItemModel() ;
					tempItemModel.setPurchaseOrderId(purchaseOrderModel.getId());
					tempItemModel.setGoodsNo(itemModel.getGoodsNo());
					
					tempItemModel = purchaseOrderItemDao.searchByModel(tempItemModel) ;
						
					if(tempItemModel == null) {
						continue ;
					}
						
					tempItemModel.setInvoiceAmount(itemModel.getInvoiceAmount());
					tempItemModel.setModifyDate(TimeUtils.getNow());
					
					purchaseOrderItemDao.modify(tempItemModel) ;	
				}
			}
			
		}
		
	}
	
}
