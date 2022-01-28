package com.topideal.entity.dto.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class ReceiveBillVerificationDTO extends PageModel implements Serializable {

	@ApiModelProperty("id主键")
	private Long id;
	@ApiModelProperty("应收账单号")
	private String receiveCode;
	@ApiModelProperty("应收账单id")
	private Long receiveId;
	@ApiModelProperty("商家id")
	private Long merchantId;
	@ApiModelProperty("商家名称")
	private String merchantName;
	@ApiModelProperty("客户id")
	private Long customerId;
	@ApiModelProperty("客户名称")
	private String customerName;
	@ApiModelProperty("应收金额")
	private BigDecimal receivePrice;
	@ApiModelProperty("未核销金额")
	private BigDecimal uncollectedPrice;
	@ApiModelProperty("结算币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
	private String currency;
	@ApiModelProperty("账单日期")
	private Timestamp billDate;
	@ApiModelProperty("开票日期")
	private Timestamp invoiceDate;
	@ApiModelProperty(" 客户账期  0:预售货款,1:信用账期-7天,2:信用账期-15天,3:信用账期-30天,4:信用账期-40天,5:信用账期-45天,6:信用账期-50天,7:信用账期-60天,8:信用账期-90天,信用账期-90天以")
	private String accountPeriod;
	@ApiModelProperty("账期逾期天数")
	private Integer accountOverdueDays;
	@ApiModelProperty("是否逾期 0否,1是")
	private String overdueStatus;
	@ApiModelProperty("创建时间")
	private Timestamp createDate;
	@ApiModelProperty("修改时间")
	private Timestamp modifyDate;
	@ApiModelProperty("客户账期")
	private String accountPeriodLabel;
	@ApiModelProperty("结算币种")
	private String currencyLabel;
	@ApiModelProperty("是否逾期 0否,1是")
	private String overdueStatusLabel;
	@ApiModelProperty("账单月份")
	private String billMonth;
	@ApiModelProperty("账单状态 02-待核销 03-部分核销 04-已核销")
	private String billStatus;
	private String billStatusLabel;
	@ApiModelProperty("账单类型 0-tob 1-toc")
	private String billType;
	private String billTypeLabel;

	@ApiModelProperty("入账月份")
	private String creditMonth;
	/**
	 * 事业部id
	 */
	@ApiModelProperty("事业部id")
	private Long buId;
	/**
	 * 事业部名称
	 */
	@ApiModelProperty("事业部名称")
	private String buName;
	/**
	 * 发票号码
	 */
	@ApiModelProperty("发票号码")
	private String invoiceNo;
	/**
	 * 开票状态 0未开票 1-已开票
	 */
	@ApiModelProperty("开票状态 0未开票 1-已开票")
	private String invoiceStatus;
	@ApiModelProperty("开票状态 0未开票 1-已开票")
	private String invoiceStatusLabel;

	@ApiModelProperty(value = "当前登录用户绑定的事业部集合")
	private List<Long> buList;

	public String getBillMonth() {
		return billMonth;
	}

	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}

	public String getAccountPeriodLabel() {
		return accountPeriodLabel;
	}

	public void setAccountPeriodLabel(String accountPeriodLabel) {
		this.accountPeriodLabel = accountPeriodLabel;
	}

	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}

	public String getOverdueStatusLabel() {
		return overdueStatusLabel;
	}

	public void setOverdueStatusLabel(String overdueStatusLabel) {
		this.overdueStatusLabel = overdueStatusLabel;
	}

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
		this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
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
		this.accountPeriodLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerInfo_accountPeriodList, accountPeriod);
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
		this.overdueStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBillVerification_overdueStatusList,
				overdueStatus);
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
		this.invoiceStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBillVerification_invoiceStatusList,
				invoiceStatus);
	}

	public String getInvoiceStatusLabel() {
		return invoiceStatusLabel;
	}

	public void setInvoiceStatusLabel(String invoiceStatusLabel) {
		this.invoiceStatusLabel = invoiceStatusLabel;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
		this.billStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBillVerification_billStatusList, billStatus);
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
		this.billTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBillVerification_billTypeList, billType);
	}

	public String getCreditMonth() {
		return creditMonth;
	}

	public void setCreditMonth(String creditMonth) {
		this.creditMonth = creditMonth;
	}

	public String getBillStatusLabel() {
		return billStatusLabel;
	}

	public void setBillStatusLabel(String billStatusLabel) {
		this.billStatusLabel = billStatusLabel;
	}

	public String getBillTypeLabel() {
		return billTypeLabel;
	}

	public void setBillTypeLabel(String billTypeLabel) {
		this.billTypeLabel = billTypeLabel;
	}

	public List<Long> getBuList() {
		return buList;
	}

	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}
}
