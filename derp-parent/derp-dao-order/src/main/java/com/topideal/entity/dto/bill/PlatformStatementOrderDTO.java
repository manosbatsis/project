package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class PlatformStatementOrderDTO extends PageModel implements Serializable {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "平台账单号")
	private String billCode;

	@ApiModelProperty(value = "公司ID")
	private Long merchantId;

	@ApiModelProperty(value = "公司名称")
	private String merchantName;

	@ApiModelProperty(value = "客户ID")
	private Long customerId;

	@ApiModelProperty(value = "客户名称")
	private String customerName;

	@ApiModelProperty(value = "客户类型")
	private String customerType;
	@ApiModelProperty(value = "客户类型中文")
	private String customerTypeLabel;

	@ApiModelProperty(value = "月份")
	private String month;

	@ApiModelProperty(value = "账单金额")
	private BigDecimal billAmount;

	@ApiModelProperty(value = "发票号")
	private String invoiceNo;

	@ApiModelProperty(value = "开票人名称")
	private String invoiceDrawer;

	@ApiModelProperty(value = "开票人ID")
	private Long invoiceDrawerId;

	@ApiModelProperty(value = "开票时间")
	private Timestamp invoiceDate;

	@ApiModelProperty(value = "是否开票")
	private String isInvoice;
	@ApiModelProperty(value = "是否开票中文")
	private String isInvoiceLabel;

	@ApiModelProperty(value = "创建时间")
	private Timestamp createDate;

	@ApiModelProperty(value = "修改时间")
	private Timestamp modifyDate;

	@ApiModelProperty(value = "币种")
	private String currency;
	@ApiModelProperty(value = "币种中文")
	private String currencyLabel;

	@ApiModelProperty(value = "发票关联id")
	private Long invoiceId;

	@ApiModelProperty(value = "账单日期")
	private Timestamp billDate;

	@ApiModelProperty(value = "是否创建应收")
	private String isCreateReceive;
	@ApiModelProperty(value = "是否创建应收中文")
	private String isCreateReceiveLabel;

	@ApiModelProperty(value = "应收结算单号")
	private String receiveCode;

	@ApiModelProperty(value = "事业部ID")
	private Long buId;

	@ApiModelProperty(value = "事业部名称")
	private String buName;

	@ApiModelProperty(value = "店铺编码")
	private String shopCode;

	@ApiModelProperty(value = "店铺名称")
	private String shopName;

	@ApiModelProperty(value = "账单类型")
	private String type;
	@ApiModelProperty(value = "账单类型中文")
	private String typeLabel ;
	@ApiModelProperty(value = "发票地址")
	private String invoicePath;


	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

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
		this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
	}

	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Timestamp getBillDate() {
		return billDate;
	}

	public void setBillDate(Timestamp billDate) {
		this.billDate = billDate;
	}

	public String getIsCreateReceive() {
		return isCreateReceive;
	}

	public void setIsCreateReceive(String isCreateReceive) {
		this.isCreateReceive = isCreateReceive;
		this.isCreateReceiveLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platformStatement_isCreateReceiveList, isCreateReceive) ;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
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

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platformStatement_typeList, type) ;
	}

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

	public String getIsCreateReceiveLabel() {
		return isCreateReceiveLabel;
	}

	public void setIsCreateReceiveLabel(String isCreateReceiveLabel) {
		this.isCreateReceiveLabel = isCreateReceiveLabel;
	}

	public String getInvoicePath() {
		return invoicePath;
	}

	public void setInvoicePath(String invoicePath) {
		this.invoicePath = invoicePath;
	}
}
