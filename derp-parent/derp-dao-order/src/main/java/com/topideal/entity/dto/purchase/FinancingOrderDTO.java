package com.topideal.entity.dto.purchase;

import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class FinancingOrderDTO {

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
    * 币种
    */
	@ApiModelProperty(value = "币种", required = false)
    private String billCurrencCodeLabel;
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
	@ApiModelProperty(value = "计息币种中文", required = false)
    private String interestCurrencyLabel;
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
	@ApiModelProperty(value = "预期交货日期", required = false)
    private Timestamp expDeliveryDate;
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
    * 融资时间
    */
	@ApiModelProperty(value = "融资时间", required = false)
    private Timestamp financingStatusDate;
    
	@ApiModelProperty(value = "表体", required = false)
    private List<FinancingOrderItemDTO> itemList ;
    
    @ApiModelProperty(value = "表体json字符串", required = false)
    private String items ;

    @ApiModelProperty(value = "已选商品ID", required = false)
	private String unNeedIds ;

    /**融资单采购发票导出字段部分*/
    
 	@ApiModelProperty(value = "采购总金额中文大写", required = false)
     private String exportPurchaseAmountCNStr;
    
 	@ApiModelProperty(value = "导出甲方供应商名", hidden = true)
	private String exportJFSupplierName ;

	@ApiModelProperty(value = "导出甲方供应商英文名", hidden = true)
	private String exportJFSupplierEnName ;
 	
	@ApiModelProperty(value = "导出供应商名", hidden = true)
	private String exportSupplierName ;

	@ApiModelProperty(value = "导出供应商英文名", hidden = true)
	private String exportSupplierEnName ;

	@ApiModelProperty(value = "导出供应商银行账号", hidden = true)
	private String exportSupplierBankAccount ;

	@ApiModelProperty(value = "导出供应商银行账户", hidden = true)
	private String exportSupplierBeneficiaryName;

	@ApiModelProperty(value = "导出供应商开户行地址", hidden = true)
	private String exportSupplierBankAddress;

	@ApiModelProperty(value = "导出供应商开户行", hidden = true)
	private String exportSupplierDepositBank;

	@ApiModelProperty(value = "导出供应商Swift Code", hidden = true)
	private String exportSupplierSwiftCode;

	@ApiModelProperty(value = "导出促销费序号", hidden = true)
	private String exportAmountIndex;

	@ApiModelProperty(value = "导出采购日期", hidden = true)
	private String exportPurchaseDate;

	@ApiModelProperty(value = "导出总数量", hidden = true)
	private String exportPurchaseNum;

	@ApiModelProperty(value = "导出总金额", hidden = true)
	private String exportPurchaseAmount;
	
	@ApiModelProperty(value = "运输方式", hidden = true)
	private String exportTransportation;
	
	@ApiModelProperty(value = "运输方式英文", hidden = true)
	private String exportTransportationEn;
	
	@ApiModelProperty(value = "始发地（港）", hidden = true)
	private String exportLoadingPort;
	
	@ApiModelProperty(value = "始发地（港）英文", hidden = true)
	private String exportLoadingPortEn;
	
	@ApiModelProperty(value = "目的地（港）", hidden = true)
	private String exportDestination;
	
	@ApiModelProperty(value = "目的地（港）英文", hidden = true)
	private String exportDestinationEn;
	
	@ApiModelProperty(value = "交货日期", hidden = true)
	private String exportDeliveryDate;
	
	@ApiModelProperty(value = "交货日期英文", hidden = true)
	private String exportDeliveryDateEn;
	
	@ApiModelProperty(value = "创建时间", hidden = true)
	private String exportCreateDateStr;

	/**融资单采购发票导出字段部分*/

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
		this.billCurrencCodeLabel = DERP.getLabelByKey(DERP.currencyCodeList, billCurrencCode) ;
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
		this.interestCurrencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, interestCurrency) ;
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

	public Timestamp getExpDeliveryDate() {
		return expDeliveryDate;
	}

	public void setExpDeliveryDate(Timestamp expDeliveryDate) {
		this.expDeliveryDate = expDeliveryDate;
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

	public Timestamp getFinancingStatusDate() {
		return financingStatusDate;
	}

	public void setFinancingStatusDate(Timestamp financingStatusDate) {
		this.financingStatusDate = financingStatusDate;
	}

	public List<FinancingOrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<FinancingOrderItemDTO> itemList) {
		this.itemList = itemList;
	}

	public String getInterestCurrencyLabel() {
		return interestCurrencyLabel;
	}

	public void setInterestCurrencyLabel(String interestCurrencyLabel) {
		this.interestCurrencyLabel = interestCurrencyLabel;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getUnNeedIds() {
		return unNeedIds;
	}

	public void setUnNeedIds(String unNeedIds) {
		this.unNeedIds = unNeedIds;
	}

	public String getExportSupplierName() {
		return exportSupplierName;
	}

	public void setExportSupplierName(String exportSupplierName) {
		this.exportSupplierName = exportSupplierName;
	}

	public String getExportSupplierEnName() {
		return exportSupplierEnName;
	}

	public void setExportSupplierEnName(String exportSupplierEnName) {
		this.exportSupplierEnName = exportSupplierEnName;
	}

	public String getExportSupplierBankAccount() {
		return exportSupplierBankAccount;
	}

	public void setExportSupplierBankAccount(String exportSupplierBankAccount) {
		this.exportSupplierBankAccount = exportSupplierBankAccount;
	}

	public String getExportSupplierBeneficiaryName() {
		return exportSupplierBeneficiaryName;
	}

	public void setExportSupplierBeneficiaryName(String exportSupplierBeneficiaryName) {
		this.exportSupplierBeneficiaryName = exportSupplierBeneficiaryName;
	}

	public String getExportSupplierBankAddress() {
		return exportSupplierBankAddress;
	}

	public void setExportSupplierBankAddress(String exportSupplierBankAddress) {
		this.exportSupplierBankAddress = exportSupplierBankAddress;
	}

	public String getExportSupplierSwiftCode() {
		return exportSupplierSwiftCode;
	}

	public void setExportSupplierSwiftCode(String exportSupplierSwiftCode) {
		this.exportSupplierSwiftCode = exportSupplierSwiftCode;
	}

	public String getExportAmountIndex() {
		return exportAmountIndex;
	}

	public void setExportAmountIndex(String exportAmountIndex) {
		this.exportAmountIndex = exportAmountIndex;
	}

	public String getExportPurchaseDate() {
		return exportPurchaseDate;
	}

	public void setExportPurchaseDate(String exportPurchaseDate) {
		this.exportPurchaseDate = exportPurchaseDate;
	}

	public String getExportPurchaseNum() {
		return exportPurchaseNum;
	}

	public void setExportPurchaseNum(String exportPurchaseNum) {
		this.exportPurchaseNum = exportPurchaseNum;
	}

	public String getExportPurchaseAmount() {
		return exportPurchaseAmount;
	}

	public void setExportPurchaseAmount(String exportPurchaseAmount) {
		this.exportPurchaseAmount = exportPurchaseAmount;
	}

	public String getExportSupplierDepositBank() {
		return exportSupplierDepositBank;
	}

	public void setExportSupplierDepositBank(String exportSupplierDepositBank) {
		this.exportSupplierDepositBank = exportSupplierDepositBank;
	}

	public String getExportTransportation() {
		return exportTransportation;
	}

	public void setExportTransportation(String exportTransportation) {
		this.exportTransportation = exportTransportation;
	}

	public String getExportTransportationEn() {
		return exportTransportationEn;
	}

	public void setExportTransportationEn(String exportTransportationEn) {
		this.exportTransportationEn = exportTransportationEn;
	}

	public String getExportLoadingPort() {
		return exportLoadingPort;
	}

	public void setExportLoadingPort(String exportLoadingPort) {
		this.exportLoadingPort = exportLoadingPort;
	}

	public String getExportLoadingPortEn() {
		return exportLoadingPortEn;
	}

	public void setExportLoadingPortEn(String exportLoadingPortEn) {
		this.exportLoadingPortEn = exportLoadingPortEn;
	}

	public String getExportDestination() {
		return exportDestination;
	}

	public void setExportDestination(String exportDestination) {
		this.exportDestination = exportDestination;
	}

	public String getExportDestinationEn() {
		return exportDestinationEn;
	}

	public void setExportDestinationEn(String exportDestinationEn) {
		this.exportDestinationEn = exportDestinationEn;
	}

	public String getExportDeliveryDate() {
		return exportDeliveryDate;
	}

	public void setExportDeliveryDate(String exportDeliveryDate) {
		this.exportDeliveryDate = exportDeliveryDate;
	}

	public String getExportDeliveryDateEn() {
		return exportDeliveryDateEn;
	}

	public void setExportDeliveryDateEn(String exportDeliveryDateEn) {
		this.exportDeliveryDateEn = exportDeliveryDateEn;
	}

	public String getExportPurchaseAmountCNStr() {
		return exportPurchaseAmountCNStr;
	}

	public void setExportPurchaseAmountCNStr(String exportPurchaseAmountCNStr) {
		this.exportPurchaseAmountCNStr = exportPurchaseAmountCNStr;
	}

	public String getExportCreateDateStr() {
		return exportCreateDateStr;
	}

	public void setExportCreateDateStr(String exportCreateDateStr) {
		this.exportCreateDateStr = exportCreateDateStr;
	}

	public String getExportJFSupplierName() {
		return exportJFSupplierName;
	}

	public void setExportJFSupplierName(String exportJFSupplierName) {
		this.exportJFSupplierName = exportJFSupplierName;
	}

	public String getExportJFSupplierEnName() {
		return exportJFSupplierEnName;
	}

	public void setExportJFSupplierEnName(String exportJFSupplierEnName) {
		this.exportJFSupplierEnName = exportJFSupplierEnName;
	}

	public String getBillCurrencCodeLabel() {
		return billCurrencCodeLabel;
	}

	public void setBillCurrencCodeLabel(String billCurrencCodeLabel) {
		this.billCurrencCodeLabel = billCurrencCodeLabel;
	}
	
	
}
