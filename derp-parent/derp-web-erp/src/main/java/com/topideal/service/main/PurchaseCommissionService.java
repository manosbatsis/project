package com.topideal.service.main;

import com.topideal.entity.dto.main.PurchaseCommissionDTO;
import com.topideal.entity.vo.main.PurchaseCommissionModel;

import java.util.List;
import java.util.Map;

public interface PurchaseCommissionService {
	
	Map<String,Object> savePurchaseCommission(PurchaseCommissionModel model) throws Exception;

	PurchaseCommissionDTO listPurchaseCommission(PurchaseCommissionDTO model) throws Exception;

	boolean delPurchaseCommission(List list) throws Exception;

	PurchaseCommissionModel getPurchaseCommission(String id) throws Exception;

	Map<String, Object> modifyPurchaseCommission(PurchaseCommissionModel model) throws Exception;

}
