package com.topideal.entity.dto.bill;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class PlatformStatementOrderExportDTO extends PageModel implements Serializable {

	private String ids;
	/**
	 * 平台账单号
	 */
	private String billCode;
	/**
	 * 公司ID
	 */
	private Long merchantId;
	/**
	 * 公司名
	 */
	private String merchantName;
	/**
	 * 客户ID
	 */
	private Long customerId;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 客户类型 1-云集 2-唯品
	 */
	private String customerType;
	private String customerTypeLabel;
	/**
	 * 月份
	 */
	private String month;
	/**
	 * 账单金额
	 */
	private BigDecimal billAmount;
	/**
	 * 发票号
	 */
	private String invoiceNo;
	/**
	 * 开票人
	 */
	private String invoiceDrawer;
	/**
	 * 开票人ID
	 */
	private Long invoiceDrawerId;
	/**
	 * 开票时间
	 */
	private Timestamp invoiceDate;
	/**
	 * 是否开票
	 */
	private String isInvoice;
	private String isInvoiceLabel;
	
	/**
    * 币种
    */
    private String currency;
    
    /**
     * 类型1-销售、2-客退、3-国检、4-盘亏、5-报废、6-盘盈、7-客退补贴、8-活动折扣、9-补偿折扣
     */
     private String type;
     private String typeLabel;
     
     /**
     * po号
     */
     private String poNo;
     /**
     * 条形码
     */
     private String barcode;
     /**
     * 商品名
     */
     private String goodsName;
     /**
     * 结算数量
     */
     private Integer settlementNum;
     /**
     * 结算金额
     */
     private BigDecimal settlementAmount;
     /**
      * 结算金额（RMB）
      */
     private BigDecimal settlementAmountRmb;
     /**
      * 汇率
      */
     private BigDecimal rate;
     /**
      * 费项配置表名称
      */
     private String settlementConfigName;
     
     private String receiveCode ;

	/* billCode get 方法 */
	public String getBillCode() {
		return billCode;
	}

	/* billCode set 方法 */
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/* customerId get 方法 */
	public Long getCustomerId() {
		return customerId;
	}

	/* customerId set 方法 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/* customerName get 方法 */
	public String getCustomerName() {
		return customerName;
	}

	/* customerName set 方法 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/* month get 方法 */
	public String getMonth() {
		return month;
	}

	/* month set 方法 */
	public void setMonth(String month) {
		this.month = month;
	}

	/* billAmount get 方法 */
	public BigDecimal getBillAmount() {
		return billAmount;
	}

	/* billAmount set 方法 */
	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	/* invoiceNo get 方法 */
	public String getInvoiceNo() {
		return invoiceNo;
	}

	/* invoiceNo set 方法 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	/* invoiceDrawer get 方法 */
	public String getInvoiceDrawer() {
		return invoiceDrawer;
	}

	/* invoiceDrawer set 方法 */
	public void setInvoiceDrawer(String invoiceDrawer) {
		this.invoiceDrawer = invoiceDrawer;
	}

	/* invoiceDrawerId get 方法 */
	public Long getInvoiceDrawerId() {
		return invoiceDrawerId;
	}

	/* invoiceDrawerId set 方法 */
	public void setInvoiceDrawerId(Long invoiceDrawerId) {
		this.invoiceDrawerId = invoiceDrawerId;
	}

	/* invoiceDate get 方法 */
	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}

	/* invoiceDate set 方法 */
	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	/* isInvoice get 方法 */
	public String getIsInvoice() {
		return isInvoice;
	}

	/* isInvoice set 方法 */
	public void setIsInvoice(String isInvoice) {
		this.isInvoice = isInvoice;
		this.isInvoiceLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platformStatement_isInvoiceList, isInvoice);
	}

	public String getIsInvoiceLabel() {
		return isInvoiceLabel;
	}

	public void setIsInvoiceLabel(String isInvoiceLabel) {
		this.isInvoiceLabel = isInvoiceLabel;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
		this.customerTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platformStatement_customerTypeList, customerType);
	}

	public String getCustomerTypeLabel() {
		return customerTypeLabel;
	}

	public void setCustomerTypeLabel(String customerTypeLabel) {
		this.customerTypeLabel = customerTypeLabel;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platformStatement_itemTypeList, type) ;
	}

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getSettlementNum() {
		return settlementNum;
	}

	public void setSettlementNum(Integer settlementNum) {
		this.settlementNum = settlementNum;
	}

	public BigDecimal getSettlementAmount() {
		return settlementAmount;
	}

	public void setSettlementAmount(BigDecimal settlementAmount) {
		this.settlementAmount = settlementAmount;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public BigDecimal getSettlementAmountRmb() {
		return settlementAmountRmb;
	}

	public void setSettlementAmountRmb(BigDecimal settlementAmountRmb) {
		this.settlementAmountRmb = settlementAmountRmb;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getSettlementConfigName() {
		return settlementConfigName;
	}

	public void setSettlementConfigName(String settlementConfigName) {
		this.settlementConfigName = settlementConfigName;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

	
}
