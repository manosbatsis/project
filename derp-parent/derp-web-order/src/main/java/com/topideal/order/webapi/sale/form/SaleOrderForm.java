package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 销售订单
 * @author wenyan
 *
 */
@ApiModel
public class SaleOrderForm  extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "销售订单id")
	private String id;
	@ApiModelProperty(value = "销售订单编号")
	private String code;
	@ApiModelProperty(value = "调出仓库ID")
	private String outDepotId;
	@ApiModelProperty(value = "业务模式 1 购销 2 代销")
	private String businessModel;
	@ApiModelProperty(value = "po单号")
	private String poNo;
	@ApiModelProperty(value = "客户ID(供应商)")
	private String customerId;
	@ApiModelProperty(value = "订单日期时间开始")
	private String createDateStartDate;
	@ApiModelProperty(value = "订单日期时间结束")
	private String createDateEndDate;
	@ApiModelProperty(value = "事业部id")
	private String buId;
	@ApiModelProperty(value = "单据标识  1-预售转销 2-非预售转销")
	private String orderType;
	@ApiModelProperty(value = "金额调整状态")
    private String amountStatus;
	@ApiModelProperty(value = "金额确认状态 0-未确认，1-确认通过，2-确认不通过")
	private String amountConfirmStatus;
	@ApiModelProperty(value = "平台采购单id集合")
	private String platformPurchaseIds;
	@ApiModelProperty(value = "平台采购单 转销售货号__销售数量__po号__单价 拼接而成")
	private String platformSalesNum;
	@ApiModelProperty(value = "单据状态集合")
	private String stateList;
	@ApiModelProperty(value = "pageSource")
	private String pageSource;
	@ApiModelProperty(value = "商家ID")
	private String merchantId;
	@ApiModelProperty(value = "币种")
	private String currency;
	@ApiModelProperty(value = "上架日期时间开始")
	private String shelfDateStartDate;
	@ApiModelProperty(value = "上架日期时间结束")
	private String shelfDateEndDate;
	@ApiModelProperty(value = "主键集合ids")
	private String ids;
	@ApiModelProperty(value = "调入仓库ID")
	private String inDepotId;
	@ApiModelProperty(value = "创建人名称")
	private String createName;
	@ApiModelProperty(value = "红冲状态: 001-待红冲 002-红冲中 003-已红冲")
	private String writeOffStatus;

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(String outDepotId) {
		this.outDepotId = outDepotId;
	}
	public String getBusinessModel() {
		return businessModel;
	}
	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCreateDateStartDate() {
		return createDateStartDate;
	}
	public void setCreateDateStartDate(String createDateStartDate) {
		this.createDateStartDate = createDateStartDate;
	}
	public String getCreateDateEndDate() {
		return createDateEndDate;
	}
	public void setCreateDateEndDate(String createDateEndDate) {
		this.createDateEndDate = createDateEndDate;
	}
	public String getBuId() {
		return buId;
	}
	public void setBuId(String buId) {
		this.buId = buId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getAmountStatus() {
		return amountStatus;
	}
	public void setAmountStatus(String amountStatus) {
		this.amountStatus = amountStatus;
	}
	public String getAmountConfirmStatus() {
		return amountConfirmStatus;
	}
	public void setAmountConfirmStatus(String amountConfirmStatus) {
		this.amountConfirmStatus = amountConfirmStatus;
	}
	public String getPlatformPurchaseIds() {
		return platformPurchaseIds;
	}
	public void setPlatformPurchaseIds(String platformPurchaseIds) {
		this.platformPurchaseIds = platformPurchaseIds;
	}
	public String getPlatformSalesNum() {
		return platformSalesNum;
	}
	public void setPlatformSalesNum(String platformSalesNum) {
		this.platformSalesNum = platformSalesNum;
	}
	public String getStateList() {
		return stateList;
	}
	public void setStateList(String stateList) {
		this.stateList = stateList;
	}
	public String getPageSource() {
		return pageSource;
	}
	public void setPageSource(String pageSource) {
		this.pageSource = pageSource;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getShelfDateStartDate() {
		return shelfDateStartDate;
	}
	public void setShelfDateStartDate(String shelfDateStartDate) {
		this.shelfDateStartDate = shelfDateStartDate;
	}
	public String getShelfDateEndDate() {
		return shelfDateEndDate;
	}
	public void setShelfDateEndDate(String shelfDateEndDate) {
		this.shelfDateEndDate = shelfDateEndDate;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getInDepotId() {
		return inDepotId;
	}
	public void setInDepotId(String inDepotId) {
		this.inDepotId = inDepotId;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getWriteOffStatus() {
		return writeOffStatus;
	}

	public void setWriteOffStatus(String writeOffStatus) {
		this.writeOffStatus = writeOffStatus;
	}
}

