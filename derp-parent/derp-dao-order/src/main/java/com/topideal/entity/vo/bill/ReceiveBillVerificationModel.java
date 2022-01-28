package com.topideal.entity.vo.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class ReceiveBillVerificationModel extends PageModel implements Serializable {

	/**
	 * id主键
	 */
	private Long id;
	/**
	 * 应收账单号
	 */
	private String receiveCode;
	/**
	 * 应收账单id
	 */
	private Long receiveId;
	/**
	 * 商家id
	 */
	private Long merchantId;
	/**
	 * 商家名称
	 */
	private String merchantName;
	/**
	 * 客户id
	 */
	private Long customerId;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 应收金额
	 */
	private BigDecimal receivePrice;
	/**
	 * 未核销金额
	 */
	private BigDecimal uncollectedPrice;
	/**
	 * 结算币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
	 */
	private String currency;
	/**
	 * 账单日期
	 */
	private Timestamp billDate;
	/**
	 * 开票日期
	 */
	private Timestamp invoiceDate;
	/**
	 * 客户账期
	 * 0:预售货款,1:信用账期-7天,2:信用账期-15天,3:信用账期-30天,4:信用账期-40天,5:信用账期-45天,6:信用账期-50天,7:信用账期-60天,8:信用账期-90天,信用账期-90天以
	 */
	private String accountPeriod;
	/**
	 * 账期逾期天数
	 */
	private Integer accountOverdueDays;
	/**
	 * 是否逾期 0否,1是
	 */
	private String overdueStatus;
	/**
	 * 事业部id
	 */
	private Long buId;
	/**
	 * 事业部名称
	 */
	private String buName;
	/**
	 * 创建时间
	 */
	private Timestamp createDate;
	/**
	 * 修改时间
	 */
	private Timestamp modifyDate;
	/**
	 * 发票号码
	 */
	private String invoiceNo;
	/**
	 * 开票状态 0未开票 1-已开票
	 */
	private String invoiceStatus;

	/**
	 * 账单状态 02-待核销 03-部分核销 04-已核销
	 */
	private String billStatus;

	/**
	 * 账单类型 0-tob 1-toc
	 */
	private String billType;

	/**
	 * 入账月份
	 */
	private String creditMonth;


	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* receiveCode get 方法 */
	public String getReceiveCode() {
		return receiveCode;
	}

	/* receiveCode set 方法 */
	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
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

	/* receivePrice get 方法 */
	public BigDecimal getReceivePrice() {
		return receivePrice;
	}

	/* receivePrice set 方法 */
	public void setReceivePrice(BigDecimal receivePrice) {
		this.receivePrice = receivePrice;
	}

	/* uncollectedPrice get 方法 */
	public BigDecimal getUncollectedPrice() {
		return uncollectedPrice;
	}

	/* uncollectedPrice set 方法 */
	public void setUncollectedPrice(BigDecimal uncollectedPrice) {
		this.uncollectedPrice = uncollectedPrice;
	}

	/* currency get 方法 */
	public String getCurrency() {
		return currency;
	}

	/* currency set 方法 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/* billDate get 方法 */
	public Timestamp getBillDate() {
		return billDate;
	}

	/* billDate set 方法 */
	public void setBillDate(Timestamp billDate) {
		this.billDate = billDate;
	}

	/* invoiceDate get 方法 */
	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}

	/* invoiceDate set 方法 */
	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	/* accountPeriod get 方法 */
	public String getAccountPeriod() {
		return accountPeriod;
	}

	/* accountPeriod set 方法 */
	public void setAccountPeriod(String accountPeriod) {
		this.accountPeriod = accountPeriod;
	}

	/* accountOverdueDays get 方法 */
	public Integer getAccountOverdueDays() {
		return accountOverdueDays;
	}

	/* accountOverdueDays set 方法 */
	public void setAccountOverdueDays(Integer accountOverdueDays) {
		this.accountOverdueDays = accountOverdueDays;
	}

	/* overdueStatus get 方法 */
	public String getOverdueStatus() {
		return overdueStatus;
	}

	/* overdueStatus set 方法 */
	public void setOverdueStatus(String overdueStatus) {
		this.overdueStatus = overdueStatus;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* modifyDate get 方法 */
	public Timestamp getModifyDate() {
		return modifyDate;
	}

	/* modifyDate set 方法 */
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Long getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(Long receiveId) {
		this.receiveId = receiveId;
	}

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

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getCreditMonth() {
		return creditMonth;
	}

	public void setCreditMonth(String creditMonth) {
		this.creditMonth = creditMonth;
	}
}
