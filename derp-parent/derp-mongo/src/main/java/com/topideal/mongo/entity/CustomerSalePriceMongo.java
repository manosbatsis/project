package com.topideal.mongo.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 销售价格管理mongo
 */
public class CustomerSalePriceMongo {

	//客户销售价格ID
    private Long customerSalePriceId;
    //标准条形码
    private String commbarcode;
    //商品名称
    private String goodsName;
    //商家ID
    private Long merchantId;
    //商家名称
    private String merchantName;
    //客户ID
    private Long customerId;
    //客户名称
    private String customerName;
 	//客户编码
 	private String customerCode;
    //状态 006删除 001 待审核 003已审核
    private String status;
    //品牌id
    private Long brandId;
    //品牌名称
    private String brandName;   
    //价格失效时间
    private String expiryDate;
    //价格生效时间
    private String effectiveDate;
    // 币种 
    private String currency;
    //销售价格（RMB）
    private BigDecimal salePrice;
    //规格型号
    private String spec;
    // 审核时间 
    private Timestamp auditDate;    
	//事业部名称
	private String buName;
	//事业部id
	private Long buId;
    
	public Long getCustomerSalePriceId() {
		return customerSalePriceId;
	}
	public void setCustomerSalePriceId(Long customerSalePriceId) {
		this.customerSalePriceId = customerSalePriceId;
	}
	public String getCommbarcode() {
		return commbarcode;
	}
	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
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
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public Timestamp getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}
	public String getBuName() {
		return buName;
	}
	public void setBuName(String buName) {
		this.buName = buName;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	
}
