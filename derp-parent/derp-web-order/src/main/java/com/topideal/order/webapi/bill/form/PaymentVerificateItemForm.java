package com.topideal.order.webapi.bill.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class PaymentVerificateItemForm {

	@ApiModelProperty("票据")
    private String token;
	@ApiModelProperty("应付账单ID")
	private Long paymentId;
	
    /**
    * 付款时间
    */
	@ApiModelProperty("付款时间")
    private String paymentDateStr;
    /**
    * 付款流水单号
    */
	@ApiModelProperty("付款流水单号")
    private String serialNo;
    /**
    * 本次核销金额
    */
	@ApiModelProperty("本次核销金额")
    private BigDecimal currentVerificateAmount;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPaymentDateStr() {
		return paymentDateStr;
	}
	public void setPaymentDateStr(String paymentDateStr) {
		this.paymentDateStr = paymentDateStr;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public BigDecimal getCurrentVerificateAmount() {
		return currentVerificateAmount;
	}
	public void setCurrentVerificateAmount(BigDecimal currentVerificateAmount) {
		this.currentVerificateAmount = currentVerificateAmount;
	}
	public Long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}



}
