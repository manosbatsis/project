package com.topideal.webapi.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
public class CustomerSalePriceEditForm implements Serializable {
	
    @ApiModelProperty(value="token",required = true)
    private String token;
    @ApiModelProperty(value="id",required = true)
    private Long id;

    @ApiModelProperty(value="价格生效时间")
    private String effectiveDate;

    @ApiModelProperty(value="价格生效时间")
    private String expiryDate;

    @ApiModelProperty(value="币种")
    private String currency;

    @ApiModelProperty(value="销售价格")
    private BigDecimal salePrice;

    @ApiModelProperty(value="备注")
    private String remark;

    @ApiModelProperty(value="编码")
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
