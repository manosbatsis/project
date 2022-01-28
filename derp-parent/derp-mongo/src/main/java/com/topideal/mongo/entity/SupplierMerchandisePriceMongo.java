package com.topideal.mongo.entity;

import java.math.BigDecimal;

public class SupplierMerchandisePriceMongo{

    /**
    * 主键ID
    */
    private Long supplierMerchandisePriceId;
    /**
    * 公司ID
    */
    private Long merchantId;
    /**
    * 公司名称
    */
    private String merchantName;
    /**
    * 供应商id
    */
    private Long customerId;
    /**
    * 供应商编号
    */
    private String customerCode;
    /**
    * 供应商名称
    */
    private String customerName;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 品牌id
    */
    private Long brandId;
    /**
    * 品牌名称
    */
    private String brandName;
    /**
    * 规格型号
    */
    private String spec;
    /**
    * 币种
    */
    private String currency;
    /**
    * 供货价
    */
    private BigDecimal supplyPrice;
    /**
    * 订单状态:001:待审核,003:已审核
    */
    private String state;

    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建人名称
    */
    private String createName;

    /**
    * 审核人
    */
    private Long auditor;
    /**
    * 审核人名称
    */
    private String auditName;

    /**
     * 修改人
     */
    private Long modifier;
    /**
     * 修改人名称
     */
    private String modifierName;

    /**
    * 报价生效时间
    */
    private String effectiveDate;
    /**
    * 报价失效时间
    */
    private String expiryDate;

    /**
     * 审核时间
     */
    private String  auditDate;

    private Long buId;

	private String buName;
	/**
	 * 库位类型ID
	 */
	private Long stockLocationTypeId;
	/**
	 * 库位类型名称
	 */
	private String stockLocationTypeName;

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Long getSupplierMerchandisePriceId() {
		return supplierMerchandisePriceId;
	}

	public void setSupplierMerchandisePriceId(Long supplierMerchandisePriceId) {
		this.supplierMerchandisePriceId = supplierMerchandisePriceId;
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

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(BigDecimal supplyPrice) {
		this.supplyPrice = supplyPrice;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Long getAuditor() {
		return auditor;
	}

	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
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

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	public Long getStockLocationTypeId() {
		return stockLocationTypeId;
	}

	public void setStockLocationTypeId(Long stockLocationTypeId) {
		this.stockLocationTypeId = stockLocationTypeId;
	}

	public String getStockLocationTypeName() {
		return stockLocationTypeName;
	}

	public void setStockLocationTypeName(String stockLocationTypeName) {
		this.stockLocationTypeName = stockLocationTypeName;
	}
}
