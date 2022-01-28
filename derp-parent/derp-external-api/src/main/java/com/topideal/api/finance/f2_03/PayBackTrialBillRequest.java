package com.topideal.api.finance.f2_03;

import java.math.BigDecimal;

/**
 * 3.16 还款试算 表体
 * @author 杨创
 * 2021-04-09
 */
public class PayBackTrialBillRequest {
	
	
	private String procurementNo;//融资申请单号  非必填
	private BigDecimal amount;//申请还款本金，现金代采时可为空  浮点数，decimal，2位小数非必填
	public String getProcurementNo() {
		return procurementNo;
	}
	public void setProcurementNo(String procurementNo) {
		this.procurementNo = procurementNo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	
	
	
}
