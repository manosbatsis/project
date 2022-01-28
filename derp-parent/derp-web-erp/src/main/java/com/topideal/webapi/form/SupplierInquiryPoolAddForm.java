package com.topideal.webapi.form;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商询价池
 * @author lianchenxing
 *
 */
public class SupplierInquiryPoolAddForm implements Serializable{
     @ApiModelProperty(value = "令牌",required = true)
	 private String token;
     @ApiModelProperty(value = "id")
     private Long id;
     @ApiModelProperty(value = "商品分类名称")
     private String merchandiseCatName;
     @ApiModelProperty(value = "品牌名称")
     private String brandName;
     @ApiModelProperty(value = "供货价")
     private BigDecimal supplyPrice;
     @ApiModelProperty(value = "商品ID")
     private Long goodsId;
     @ApiModelProperty(value = "供应商编码")
     private String customerCode;
     @ApiModelProperty(value = "供应商名称")
     private String customerName;
     @ApiModelProperty(value = "规格型号")
     private String spec;
     @ApiModelProperty(value = "计量单位")
     private String unit;
     @ApiModelProperty(value = "原产国ID",required = true)
     private Long countryId;
     @ApiModelProperty(value = "商品分类ID",required = true)
     private Long merchandiseCatId;
     @ApiModelProperty(value = "品牌ID",required = true)
     private Long brandId;
     @ApiModelProperty(value = "供应商ID")
     private Long customerId;
     @ApiModelProperty(value = "最大起订数")
     private Integer maximum;
     @ApiModelProperty(value = "商品名称")
     private String goodsName;
     @ApiModelProperty(value = "最小起订数")
     private Integer minimum;
     @ApiModelProperty(value = "原产国名称")
     private String countryName;
     @ApiModelProperty(value = "单位ID",required = true)
     private Long unitId;
     @ApiModelProperty(value = "商家ID")
     private Long merchantId;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getMerchandiseCatName() {
		return merchandiseCatName;
	}
	public void setMerchandiseCatName(String merchandiseCatName) {
		this.merchandiseCatName = merchandiseCatName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public BigDecimal getSupplyPrice() {
		return supplyPrice;
	}
	public void setSupplyPrice(BigDecimal supplyPrice) {
		this.supplyPrice = supplyPrice;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public Long getMerchandiseCatId() {
		return merchandiseCatId;
	}
	public void setMerchandiseCatId(Long merchandiseCatId) {
		this.merchandiseCatId = merchandiseCatId;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Integer getMaximum() {
		return maximum;
	}
	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Integer getMinimum() {
		return minimum;
	}
	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

     
     





}

