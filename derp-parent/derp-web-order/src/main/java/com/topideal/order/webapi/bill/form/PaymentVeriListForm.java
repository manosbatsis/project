package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PaymentVeriListForm extends PageForm{

	@ApiModelProperty("票据")
	private String token ;
	
	@ApiModelProperty("事业部ID")
	private Long buId; 
	
	@ApiModelProperty("供应商ID")
	private Long supplierId ;
	
	@ApiModelProperty("币种")
	private String currency ;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
}
