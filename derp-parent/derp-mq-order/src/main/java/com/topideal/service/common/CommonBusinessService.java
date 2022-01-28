package com.topideal.service.common;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;

/***
 * 公共业务service
 * @author gy
 *
 */
public interface CommonBusinessService {

	/**
	 * 采购入库自动勾稽
	 * @param purchaseWarehouseCode
	 * @throws Exception 
	 */
	//void saveAutoPurchaseAnalysis(String purchaseWarehouseCode) throws Exception ;

	/**
	 * 采购汇率回填
	 * @param purchaseOrder
	 * @param date
	 * @throws SQLException
	 */
	void saveRate(PurchaseOrderModel purchaseOrder, Timestamp date) throws SQLException;
	
	/**
	 * 采购修改勾稽状态
	 * @param purchaseOrder
	 * @param date
	 * @throws SQLException
	 */
	void modifyCorrelationstatus(PurchaseWarehouseModel purchaseWarehouseModel) throws SQLException;

	/**
	 * 采购勾稽已完结
	 * @param model
	 * @throws SQLException
	 */
	//void modifyAnilysisEnd(PurchaseOrderModel model) throws SQLException;

	/**
	 * 发送金额调整邮件
	 * @param purchaseId
	 * @param warehouseId
	 * @throws SQLException 
	 */
	void sendAmountComfirmMail(Long purchaseId, Long warehouseId) throws SQLException;
}
