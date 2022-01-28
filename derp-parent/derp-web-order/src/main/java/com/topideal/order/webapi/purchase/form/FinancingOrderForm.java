package com.topideal.order.webapi.purchase.form;

import java.sql.Timestamp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class FinancingOrderForm {
	
	/**
    * 票据
    */
	@ApiModelProperty(value = "票据", required = true)
    private String token;
    /**
    * 融资单号
    */
	@ApiModelProperty(value = "融资单号", required = false)
    private String code;
    /**
    * 外部申请单号
    */
	@ApiModelProperty(value = "外部申请单号", required = false)
    private String purchaseOrders;
    /**
    * 公司ID
    */
	@ApiModelProperty(value = "公司ID", required = false)
    private Long merchantId;
    /**
    * 公司名
    */
	@ApiModelProperty(value = "公司名", required = false)
    private String merchantName;
    /**
    * 供应商企业内部编码(企业ID) 卓普信接口数据
    */
	@ApiModelProperty(value = "供应商企业内部编码(企业ID) 卓普信接口数据", required = false)
    private String supplierCode;
    /**
    * 供应商名称（供应商名称企业中文名称） 卓普信接口数据
    */
	@ApiModelProperty(value = "供应商名称（供应商名称企业中文名称） 卓普信接口数据", required = false)
    private String supplierName;
    /**
    * 仓库名称 卓普信接口数据
    */
	@ApiModelProperty(value = "仓库名称 卓普信接口数据", required = false)
    private String depotName;
    /**
    * 仓库外部编码ID 卓普信接口数据
    */
	@ApiModelProperty(value = "仓库外部编码ID 卓普信接口数据", required = false)
    private String depotCode;
    /**
    * 币种
    */
	@ApiModelProperty(value = "币种", required = false)
    private String billCurrencCode;
    /**
    * 业务模式： DC001-跨境现金代采业务
    */
	@ApiModelProperty(value = "业务模式： DC001-跨境现金代采业务", required = false)
    private String businessModel;
    /**
    * 资金方：1-卓普信
    */
	@ApiModelProperty(value = "资金方：1-卓普信", required = false)
    private String investorType;
    /**
    * 计息币种
    */
	@ApiModelProperty(value = "计息币种", required = false)
    private String interestCurrency;
    /**
    * 融资天数
    */
	@ApiModelProperty(value = "融资天数", required = false)
    private Integer borrowingDays;
    /**
    * 采购合同号
    */
	@ApiModelProperty(value = "采购合同号", required = false)
    private String poNo;
    /**
    * 付款条款内部唯一标识 卓普信接口数据
    */
	@ApiModelProperty(value = "付款条款内部唯一标识 卓普信接口数据", required = false)
    private String paymentTermId;
    /**
    * 付款条款名称 卓普信接口数据
    */
	@ApiModelProperty(value = "付款条款名称 卓普信接口数据", required = false)
    private String paymentTermName;
    /**
    * 提货地址
    */
	@ApiModelProperty(value = "提货地址", required = false)
    private String warehouseAddress;
    /**
    * 贸易方式 卓普信接口数据
    */
	@ApiModelProperty(value = "贸易方式 卓普信接口数据", required = false)
    private String tradeMode;
	/**
	 * 预期交货日期
	 */
	@ApiModelProperty(value = "预期交货日期字符串", required = false)
	private String expDeliveryDateStr;
    /**
    * 备注
    */
	@ApiModelProperty(value = "备注", required = false)
    private String loanApplyRemark;
    /**
    * 采购总金额
    */
	@ApiModelProperty(value = "采购总金额", required = false)
    private BigDecimal purchaseAmount;
    /**
    * 采购折扣金额
    */
	@ApiModelProperty(value = "采购折扣金额", required = false)
    private BigDecimal purchaseDiscountAmount;
    /**
    * 保证金比例
    */
	@ApiModelProperty(value = "保证金比例", required = false)
    private Double marginLevelRatio;
    /**
    * 应付保证金
    */
	@ApiModelProperty(value = "应付保证金", required = false)
    private BigDecimal marginPayableAmount;

    /**
     * 年化利率
     */
	@ApiModelProperty(value = "年化利率", required = false)
    private BigDecimal annualizedInterestRate;
    
    @ApiModelProperty(value = "表体json字符串", required = false)
    private String items ;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(String purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
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

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getBillCurrencCode() {
		return billCurrencCode;
	}

	public void setBillCurrencCode(String billCurrencCode) {
		this.billCurrencCode = billCurrencCode;
	}

	public String getBusinessModel() {
		return businessModel;
	}

	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}

	public String getInvestorType() {
		return investorType;
	}

	public void setInvestorType(String investorType) {
		this.investorType = investorType;
	}

	public String getInterestCurrency() {
		return interestCurrency;
	}

	public void setInterestCurrency(String interestCurrency) {
		this.interestCurrency = interestCurrency;
	}

	public Integer getBorrowingDays() {
		return borrowingDays;
	}

	public void setBorrowingDays(Integer borrowingDays) {
		this.borrowingDays = borrowingDays;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getPaymentTermId() {
		return paymentTermId;
	}

	public void setPaymentTermId(String paymentTermId) {
		this.paymentTermId = paymentTermId;
	}

	public String getPaymentTermName() {
		return paymentTermName;
	}

	public void setPaymentTermName(String paymentTermName) {
		this.paymentTermName = paymentTermName;
	}

	public String getWarehouseAddress() {
		return warehouseAddress;
	}

	public void setWarehouseAddress(String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}

	public String getTradeMode() {
		return tradeMode;
	}

	public void setTradeMode(String tradeMode) {
		this.tradeMode = tradeMode;
	}

	public String getLoanApplyRemark() {
		return loanApplyRemark;
	}

	public void setLoanApplyRemark(String loanApplyRemark) {
		this.loanApplyRemark = loanApplyRemark;
	}

	public BigDecimal getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(BigDecimal purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}

	public BigDecimal getPurchaseDiscountAmount() {
		return purchaseDiscountAmount;
	}

	public void setPurchaseDiscountAmount(BigDecimal purchaseDiscountAmount) {
		this.purchaseDiscountAmount = purchaseDiscountAmount;
	}

	public Double getMarginLevelRatio() {
		return marginLevelRatio;
	}

	public void setMarginLevelRatio(Double marginLevelRatio) {
		this.marginLevelRatio = marginLevelRatio;
	}

	public BigDecimal getMarginPayableAmount() {
		return marginPayableAmount;
	}

	public void setMarginPayableAmount(BigDecimal marginPayableAmount) {
		this.marginPayableAmount = marginPayableAmount;
	}


	public BigDecimal getAnnualizedInterestRate() {
		return annualizedInterestRate;
	}

	public void setAnnualizedInterestRate(BigDecimal annualizedInterestRate) {
		this.annualizedInterestRate = annualizedInterestRate;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExpDeliveryDateStr() {
		return expDeliveryDateStr;
	}

	public void setExpDeliveryDateStr(String expDeliveryDateStr) {
		this.expDeliveryDateStr = expDeliveryDateStr;
	}
}
