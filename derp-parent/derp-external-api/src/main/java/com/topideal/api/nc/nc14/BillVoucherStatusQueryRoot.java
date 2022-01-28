package com.topideal.api.nc.nc14;

/**
 * 凭证查询接口
 */
public class BillVoucherStatusQueryRoot {

	private String sourceCode;
	private String corCcode;
	private String billId;
	private String type;
	private String startTime;
	private String endTime;

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getCorCcode() {
		return corCcode;
	}

	public void setCorCcode(String corCcode) {
		this.corCcode = corCcode;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}