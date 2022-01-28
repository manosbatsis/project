package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class CustomerQuotaConfigForm extends PageForm{
	
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	
	@ApiModelProperty(value = "主键ID")
    private Long id;
	
	@ApiModelProperty(value = "事业部ID")
    private Long buId;
	
	@ApiModelProperty(value = "客户ID")
    private Long customerId;
	
	@ApiModelProperty(value = "客户额度")
    private BigDecimal customerQuota;
	
	@ApiModelProperty(value = "币种")
    private String currency;
	
	@ApiModelProperty(value = "生效日期")
    private String effectiveDate;
	
	@ApiModelProperty(value = "失效日期")
    private String expirationDate;
	


	@ApiModelProperty(value = "状态 0-待审核 1-已审核")
	private String status;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getCustomerQuota() {
		return customerQuota;
	}

	public void setCustomerQuota(BigDecimal customerQuota) {
		this.customerQuota = customerQuota;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}



	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
