package com.topideal.entity.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.math.BigDecimal;

@ApiModel
public class PaymentPrintSummaryDTO {

	@ApiModelProperty("摘要")
    private String abstractName;
	/**
    * 母品牌
    */
	@ApiModelProperty("母品牌")
    private String superiorParentBrandName;
	
	@ApiModelProperty("NC收支费项")
    private String ncName;
	
	@ApiModelProperty("序号")
    private String index;
	
	@ApiModelProperty("金额")
    private BigDecimal amount;

	public String getAbstractName() {
		return abstractName;
	}

	public void setAbstractName(String abstractName) {
		this.abstractName = abstractName;
	}

	public String getSuperiorParentBrandName() {
		return superiorParentBrandName;
	}

	public void setSuperiorParentBrandName(String superiorParentBrandName) {
		this.superiorParentBrandName = superiorParentBrandName;
	}

	public String getNcName() {
		return ncName;
	}

	public void setNcName(String ncName) {
		this.ncName = ncName;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
