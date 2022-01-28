package com.topideal.service.main.impl;

import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.CustomerInfoDao;
import com.topideal.dao.main.PurchaseCommissionDao;
import com.topideal.entity.dto.main.PurchaseCommissionDTO;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.entity.vo.main.PurchaseCommissionModel;
import com.topideal.mongo.dao.PurchaseCommissionMongoDao;
import com.topideal.mongo.entity.PurchaseCommissionMongo;
import com.topideal.service.main.PurchaseCommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PurchaseCommissionServiceImpl implements PurchaseCommissionService {

	@Autowired
    PurchaseCommissionDao purchaseCommissionDao ;
	
	@Autowired
	CustomerInfoDao customerInfoDao ;

	@Autowired
    PurchaseCommissionMongoDao purchaseCommissionMongoDao ;
	
	@Override

	public Map<String, Object> savePurchaseCommission(PurchaseCommissionModel model) throws Exception {
		Map<String,Object> map = new  HashMap<>();
		// 1成功 0失败
		map.put("status", "1");
		//同一个配置类型下,同一个客户、同一个供应商仅能维护一条记录
		PurchaseCommissionModel searchModel = new PurchaseCommissionModel() ;
		searchModel.setConfigType(model.getConfigType());
		searchModel.setCustomerId(model.getCustomerId());
		searchModel.setSupplierId(model.getSupplierId());
		searchModel.setMerchantId(model.getMerchantId());
		
		searchModel = purchaseCommissionDao.searchByModel(searchModel); 
		
		if(searchModel != null ) {
			map.put("status", "0");
			map.put("message", "同一个配置类型下,同一个客户、同一个供应商仅能维护一条记录");
		}else {
			
			//获取客户信息
			CustomerInfoModel customerInfo = customerInfoDao.searchById(model.getCustomerId());
			model.setCustomerCode(customerInfo.getCode());
			model.setCustomerName(customerInfo.getName());

			CustomerInfoModel supplierInfo = customerInfoDao.searchById(model.getSupplierId());
			model.setSupplierCode(supplierInfo.getCode());
			model.setSupplierName(supplierInfo.getName());
			
			double purchaseCommissionValue = model.getPurchaseCommission().doubleValue(); 
			purchaseCommissionValue = purchaseCommissionValue / 100 ;
			model.setPurchaseCommission(new BigDecimal(purchaseCommissionValue));
			
			double saleRebateValue = model.getSaleRebate().doubleValue(); 
			saleRebateValue = saleRebateValue / 100 ;
			model.setSaleRebate(new BigDecimal(saleRebateValue));
			
			model.setCreateDate(TimeUtils.getNow());
			model.setModifyDate(TimeUtils.getNow());
			
			Long purchaseCommissionModelId = purchaseCommissionDao.save(model);
			
			//判断是否插入成功，如成功写入Mongo
			if(purchaseCommissionModelId == null) {
				map.put("status", "0");
				map.put("message", "保存失败");
			}else {
				
				//数据写入mongo
				PurchaseCommissionMongo mongo = new PurchaseCommissionMongo() ;
				
				mongo.setCreater(model.getCreater());
				mongo.setCustomerCode(model.getCustomerCode());
				mongo.setCustomerId(model.getCustomerId());
				mongo.setCustomerName(model.getCustomerName());
				mongo.setPurchaseCommissionId(model.getId());
				mongo.setMerchantId(model.getMerchantId());
				mongo.setPurchaseCommission(model.getPurchaseCommission().doubleValue());
				mongo.setSaleRebate(model.getSaleRebate().doubleValue());
				mongo.setSupplierCode(model.getSupplierCode());
				mongo.setSupplierId(model.getSupplierId());
				mongo.setSupplierName(model.getSupplierName());
				mongo.setConfigType(model.getConfigType());
				
				purchaseCommissionMongoDao.insert(mongo);
			}
			
		}
		
		return map;
	}

	@Override
	public PurchaseCommissionDTO listPurchaseCommission(PurchaseCommissionDTO dto) throws Exception {
		return purchaseCommissionDao.getListByPage(dto);
	}

	@SuppressWarnings("rawtypes")
	@Override

	public boolean delPurchaseCommission(List list) throws Exception {
		int rows = purchaseCommissionDao.delete(list); 
		
		boolean flag = false ; 
		
		if(rows > 0) {
			flag = true ;
			
			//删除成功后同时删除Mongo内容
			Map<String,Object> params = null ;
			for (Object id : list) {
				params = new HashMap<String,Object>() ;
				params.put("purchaseCommissionId", Long.valueOf(String.valueOf(id))) ;
				
				purchaseCommissionMongoDao.remove(params);
			}
			
		}
		
		return flag ;
	}

	@Override
	public PurchaseCommissionModel getPurchaseCommission(String id) throws Exception {
		
		return purchaseCommissionDao.searchById(Long.valueOf(id));
	}
	

	@Override

	public Map<String, Object> modifyPurchaseCommission(PurchaseCommissionModel model) throws Exception {
		
		Map<String,Object> map = new  HashMap<>();
		// 1成功 0失败
		map.put("status", "1");
		
		PurchaseCommissionModel oldModel = purchaseCommissionDao.searchById(model.getId()) ;
		
		//判断修改model的供应商及客户是否与原来一致，如不一致需判断是否符合同一个配置类型、同一个客户、同一个供应商仅能维护一条记录
		if(oldModel != null && 
				(!oldModel.getCustomerId().equals(model.getCustomerId())
						|| !oldModel.getSupplierId().equals(model.getSupplierId()))){
			PurchaseCommissionModel searchModel = new PurchaseCommissionModel() ;
			searchModel.setCustomerId(model.getCustomerId());
			searchModel.setSupplierId(model.getSupplierId());
			searchModel.setMerchantId(model.getMerchantId());
			searchModel.setConfigType(model.getConfigType());
			
			searchModel = purchaseCommissionDao.searchByModel(searchModel); 
			
			if(searchModel != null ) {
				map.put("status", "0");
				map.put("message", "同一个配置类型下,同一个客户、同一个供应商仅能维护一条记录");
				
				return map;
			}
		}
		
		CustomerInfoModel customerInfo = customerInfoDao.searchById(model.getCustomerId());
		CustomerInfoModel supplierInfo = customerInfoDao.searchById(model.getSupplierId());
		model.setCustomerCode(customerInfo.getCode());
		model.setCustomerName(customerInfo.getName());
		model.setSupplierCode(supplierInfo.getCode());
		model.setSupplierName(supplierInfo.getName());
		
		//对小数进行除百保存
		double purchaseCommissionValue = model.getPurchaseCommission().doubleValue(); 
		purchaseCommissionValue = purchaseCommissionValue / 100 ;
		model.setPurchaseCommission(new BigDecimal(purchaseCommissionValue));
		
		double saleRebateValue = model.getSaleRebate().doubleValue(); 
		saleRebateValue = saleRebateValue / 100 ;
		model.setSaleRebate(new BigDecimal(saleRebateValue));
		
		model.setModifyDate(TimeUtils.getNow());
		
		int rows = purchaseCommissionDao.modify(model);
		
		if(rows <= 0) {
			map.put("status", "0");
			map.put("message", "修改失败");
		}else {
			
			model = purchaseCommissionDao.searchById(model.getId()) ;
			
			//查询参数
			Map<String, Object> queryParams = new HashMap<String, Object>() ; 
			queryParams.put("purchaseCommissionId", model.getId()) ; 
			
			//保存参数
			Map<String, Object> data = new HashMap<String, Object>() ;
			data.put("purchaseCommissionId", model.getId()) ;
			data.put("creater", model.getCreater()) ;
			data.put("customerCode", model.getCustomerCode()) ;
			data.put("customerId", model.getCustomerId()) ;
			data.put("customerName", model.getCustomerName()) ;
			data.put("merchantId", model.getMerchantId()) ;
			data.put("purchaseCommission", model.getPurchaseCommission().doubleValue()) ;
			data.put("saleRebate", model.getSaleRebate().doubleValue()) ;
			data.put("supplierCode", model.getSupplierCode()) ;
			data.put("supplierId", model.getSupplierId()) ;
			data.put("supplierName", model.getSupplierName()) ;
			data.put("modifier", model.getModifier()) ;
			data.put("configType", model.getConfigType()) ;

			purchaseCommissionMongoDao.update(queryParams, data);
		}

		return map;
	}

}
