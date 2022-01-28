package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.dto.sale.SaleOrderItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class SaleOrderAddForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	/**
	 * 单据标识  1-预售转销 2-非预售转销
	 */
	@ApiModelProperty(value = "单据标识  1-预售转销 2-非预售转销")
	private String orderType;
	// 商家ID
	@ApiModelProperty(value = "商家ID")
	private Long merchantId;
	// 商家名称
	@ApiModelProperty(value = "商家名称")
	private String merchantName;
	@ApiModelProperty(value = "操作 0-保存，1-提交，2-审核")
	private String operate;
	@ApiModelProperty(value = "单据id")
	private Long id;
	@ApiModelProperty(value = "客户id")
	private Long customerId;
	@ApiModelProperty(value = "审核结果 0-通过，1-驳回")
	private String auditResult;
	@ApiModelProperty(value = "事业部id")
	private Long buId;
	// 调出仓库ID
	@ApiModelProperty(value = "出库仓库ID")
	private Long outDepotId;
	// 入仓仓库id
	@ApiModelProperty(value = "入仓仓库id")
	private Long inDepotId;
	// LBX单号
	@ApiModelProperty(value = "LBX单号")
	private String lbxNo;
	// 业务模式  1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结
	@ApiModelProperty(value = "业务模式  1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结")
	private String businessModel;
	// 理货单位 00-托盘 01-箱 02-件
	@ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件")
	private String tallyingUnit;
	//币种
	@ApiModelProperty(value = "币种")
	private String currency;
	@ApiModelProperty(value = "是否同关区（0-否，1-是）")
	private String isSameArea;
	@ApiModelProperty(value = "运输方式  海运、空运、陆运、港到仓拖车、中欧铁路、其他")
	private String transport;
	@ApiModelProperty(value = "po单号")
	private String poNo;
	@ApiModelProperty(value = "关联预售单单号")
	private String preSaleOrderRelCode;
	@ApiModelProperty(value = "付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款")
	private String paymentTerms;
	@ApiModelProperty(value = "贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW")
	private String tradeTerms;
	@ApiModelProperty(value = "审批单号")
	private String approvalNo;
	// 备注
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "平台采购订单号")
	private String platformPurchaseIds;

	@ApiModelProperty(value = "库位类型Id")
	private Long  stockLocationTypeId;
	// 商品信息-表体
	private List<SaleOrderItemDTO> itemList;

	@ApiModelProperty(value = "驳回原因")
	private String  rejectReason;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
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

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public Long getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	public Long getInDepotId() {
		return inDepotId;
	}

	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}

	public String getLbxNo() {
		return lbxNo;
	}

	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	public String getBusinessModel() {
		return businessModel;
	}

	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIsSameArea() {
		return isSameArea;
	}

	public void setIsSameArea(String isSameArea) {
		this.isSameArea = isSameArea;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getPreSaleOrderRelCode() {
		return preSaleOrderRelCode;
	}

	public void setPreSaleOrderRelCode(String preSaleOrderRelCode) {
		this.preSaleOrderRelCode = preSaleOrderRelCode;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getTradeTerms() {
		return tradeTerms;
	}

	public void setTradeTerms(String tradeTerms) {
		this.tradeTerms = tradeTerms;
	}

	public String getApprovalNo() {
		return approvalNo;
	}

	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPlatformPurchaseIds() {
		return platformPurchaseIds;
	}

	public void setPlatformPurchaseIds(String platformPurchaseIds) {
		this.platformPurchaseIds = platformPurchaseIds;
	}

	public List<SaleOrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleOrderItemDTO> itemList) {
		this.itemList = itemList;
	}

	public Long getStockLocationTypeId() {
		return stockLocationTypeId;
	}

	public void setStockLocationTypeId(Long stockLocationTypeId) {
		this.stockLocationTypeId = stockLocationTypeId;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
}

