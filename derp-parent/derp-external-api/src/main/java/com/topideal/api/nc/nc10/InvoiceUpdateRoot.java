package com.topideal.api.nc.nc10;

import java.util.List;

/**
 * 发票更新接口
 */
public class InvoiceUpdateRoot {

	private String sourceCode;
	private String confirmBillId;
	private List<Details> details;

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setConfirmBillId(String confirmBillId) {
		this.confirmBillId = confirmBillId;
	}

	public String getConfirmBillId() {
		return confirmBillId;
	}

	public void setDetails(List<Details> details) {
		this.details = details;
	}

	public List<Details> getDetails() {
		return details;
	}

}