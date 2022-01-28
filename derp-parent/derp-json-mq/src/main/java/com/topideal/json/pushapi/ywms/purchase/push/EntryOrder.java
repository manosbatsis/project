package com.topideal.json.pushapi.ywms.purchase.push;

/**
 * 	采购指令下推众邦云仓json--订单信息
 * @author Guobs
 *
 */
public class EntryOrder {
	
	private String totalOrderLines;

    private String entryOrderCode;

    private String ownerCode;

    private String purchaseOrderCode;

    private String warehouseCode;

    private String orderCreateTime;

    private String orderType;

    private String expectStartTime;

    private String expectEndTime;

    private String logisticsCode;

    private String logisticsName;

    private String expressCode;

    private String supplierCode;

    private String supplierName;

    private String operatorCode;

    private String operatorName;

    private String operateTime;

    private SenderInfo senderInfo;

    private ReceiverInfo receiverInfo;

    private String remark;

	public String getTotalOrderLines() {
		return totalOrderLines;
	}

	public void setTotalOrderLines(String totalOrderLines) {
		this.totalOrderLines = totalOrderLines;
	}

	public String getEntryOrderCode() {
		return entryOrderCode;
	}

	public void setEntryOrderCode(String entryOrderCode) {
		this.entryOrderCode = entryOrderCode;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getPurchaseOrderCode() {
		return purchaseOrderCode;
	}

	public void setPurchaseOrderCode(String purchaseOrderCode) {
		this.purchaseOrderCode = purchaseOrderCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getExpectStartTime() {
		return expectStartTime;
	}

	public void setExpectStartTime(String expectStartTime) {
		this.expectStartTime = expectStartTime;
	}

	public String getExpectEndTime() {
		return expectEndTime;
	}

	public void setExpectEndTime(String expectEndTime) {
		this.expectEndTime = expectEndTime;
	}

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public SenderInfo getSenderInfo() {
		return senderInfo;
	}

	public void setSenderInfo(SenderInfo senderInfo) {
		this.senderInfo = senderInfo;
	}

	public ReceiverInfo getReceiverInfo() {
		return receiverInfo;
	}

	public void setReceiverInfo(ReceiverInfo receiverInfo) {
		this.receiverInfo = receiverInfo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
