package com.topideal.api.finance.f2_02;

import java.math.BigDecimal;

/**
 * 3.7企业还款赎回接收接口    表体申请单信息，可循环，商品赎回时可空
 * @author 杨创
 * 2021-04-09
 */
public class PayBackRedeemApplyItemRequest {
	private String procurementNo;//融资申请单号 必填
	private BigDecimal amount;//申请还款本金  浮点数，decimal，2位小数  必填 
	private BigDecimal refundInterest;//归还利息（线上还款必填）  decimal，2位小数  非必填 
	private BigDecimal refundAmount;//还款总金额（线上还款必填） 浮点数，decimal，2位小数  非必填
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
	public BigDecimal getRefundInterest() {
		return refundInterest;
	}
	public void setRefundInterest(BigDecimal refundInterest) {
		this.refundInterest = refundInterest;
	}
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	
	


	
	
	
}
