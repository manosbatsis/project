package com.topideal.api.nc.nc11;

/**
 * 账单状态查询接口
 */
public class BillStatusQueryRoot {

	private String confirmBillId;
	private String sourceCode;
	private String billType;

	public void setConfirmBillId(String confirmBillId) {
		this.confirmBillId = confirmBillId;
	}

	public String getConfirmBillId() {
		return confirmBillId;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getBillType() {
		return billType;
	}

}