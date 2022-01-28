package com.topideal.entity.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * POP金额自校验
 *
 */
public class POPAmountAutomaticDTO implements Serializable{

	//销售平台订单号
	private String externalCode ;
	//金额
	private BigDecimal payment ;
	//是否异常
	private String isException ;		
	//异常原因
	private String excetionResult ;
	//税费
	private BigDecimal taxFee;
	//运费
	private BigDecimal wayFrtFee;
	//优惠减免金额
	private BigDecimal discount;
	
	public String getIsException() {
		return isException;
	}
	public void setIsException(String isException) {
		this.isException = isException;
	}
	public String getExcetionResult() {
		return excetionResult;
	}
	public void setExcetionResult(String excetionResult) {
		this.excetionResult = excetionResult;
	}
	public String getExternalCode() {
		return externalCode;
	}
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}
	public BigDecimal getPayment() {
		return payment;
	}
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public BigDecimal getTaxFee() {
		return taxFee;
	}

	public void setTaxFee(BigDecimal taxFee) {
		this.taxFee = taxFee;
	}

	public BigDecimal getWayFrtFee() {
		return wayFrtFee;
	}

	public void setWayFrtFee(BigDecimal wayFrtFee) {
		this.wayFrtFee = wayFrtFee;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
}
