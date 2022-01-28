package com.topideal.dao.purchase;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.PurchaseInvoiceItemDTO;
import com.topideal.entity.vo.purchase.PurchaseInvoiceItemModel;


public interface PurchaseInvoiceItemDao extends BaseDao<PurchaseInvoiceItemModel>{

	PurchaseInvoiceItemModel getInvoiceNum(Map<String, Object> queryMap);


	/**
	 * 查询表体信息
	 * @param purchaseInvoiceItemModel
	 * @return
	 */
	List<PurchaseInvoiceItemDTO> getPurchaseInvoiceItemModel(PurchaseInvoiceItemDTO purchaseInvoiceItemModel);


	/**
	 * 根据发票id集合删除发票表体
	 * @param invoiceIds
	 */
	void batchDelByInvoiceIds(List<Long> invoiceIds);






}
