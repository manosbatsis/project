package com.topideal.json.pushapi.ywms.purchase.pushback;

public class EntryOrder {
	
	private String totalOrderLines;

    private String entryOrderCode;

    private String ownerCode;

    private String warehouseCode;

    private String entryOrderId;

    private String entryOrderType;

    private String outBizCode;

    private String confirmType;

    private String status;

    private String operateTime;

    private String remark;

    private ExtendProps extendProps;

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

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getEntryOrderId() {
		return entryOrderId;
	}

	public void setEntryOrderId(String entryOrderId) {
		this.entryOrderId = entryOrderId;
	}

	public String getEntryOrderType() {
		return entryOrderType;
	}

	public void setEntryOrderType(String entryOrderType) {
		this.entryOrderType = entryOrderType;
	}

	public String getOutBizCode() {
		return outBizCode;
	}

	public void setOutBizCode(String outBizCode) {
		this.outBizCode = outBizCode;
	}

	public String getConfirmType() {
		return confirmType;
	}

	public void setConfirmType(String confirmType) {
		this.confirmType = confirmType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public ExtendProps getExtendProps() {
		return extendProps;
	}

	public void setExtendProps(ExtendProps extendProps) {
		this.extendProps = extendProps;
	}
    
    

}
