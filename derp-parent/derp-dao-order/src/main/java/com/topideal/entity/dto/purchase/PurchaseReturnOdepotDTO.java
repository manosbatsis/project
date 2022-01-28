package com.topideal.entity.dto.purchase;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotItemModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class PurchaseReturnOdepotDTO extends PageModel implements Serializable {

	/**
	 * id
	 */
	@ApiModelProperty(value = "记录ID", required = false)
	private Long id;
	/**
	 * 采购退货出库订单号
	 */
	@ApiModelProperty(value = "采购退货出库订单号", required = false)
	private String code;
	/**
	 * 采购退订单号
	 */
	@ApiModelProperty(value = "采购退订单号", required = false)
	private String purchaseReturnCode;
	/**
	 * 退出仓库id
	 */
	@ApiModelProperty(value = "退出仓库id", required = false)
	private Long outDepotId;
	/**
	 * 退出仓库名称
	 */
	@ApiModelProperty(value = "退出仓库名称", required = false)
	private String outDepotName;
	/**
	 * 出库时间
	 */
	@ApiModelProperty(value = "出库时间", required = false)
	private Timestamp returnOutDate;
	/**
	 * 公司ID
	 */
	@ApiModelProperty(value = "公司ID", required = false)
	private Long merchantId;
	/**
	 * 公司名称
	 */
	@ApiModelProperty(value = "公司名称", required = false)
	private String merchantName;
	/**
	 * 状态：001-待审核 ,003-已审核 ,015:待出仓,016:已出仓,027:出库中 ,007已完结
	 */
	@ApiModelProperty(value = "状态：001-待审核 ,003-已审核 ,015:待出仓,016:已出仓,027:出库中 ,007已完结", required = false)
	private String status;
	@ApiModelProperty(value = "状态中文：001-待审核 ,003-已审核 ,015:待出仓,016:已出仓,027:出库中 ,007已完结", required = false)
	private String statusLabel;
	/**
	 * 审核时间
	 */
	@ApiModelProperty(value = "审核时间", required = false)
	private Timestamp auditDate;
	/**
	 * 客户id
	 */
	@ApiModelProperty(value = "供应商id", required = false)
	private Long supplierId;
	/**
	 * 客户名称
	 */
	@ApiModelProperty(value = "供应商名称", required = false)
	private String supplierName;
	/**
	 * 币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元 07 英镑
	 */
	@ApiModelProperty(value = "币种", required = false)
	private String currency;
	@ApiModelProperty(value = "币种中文", required = false)
	private String currencyLabel;
	/**
	 * 事业部名称
	 */
	@ApiModelProperty(value = "事业部名称", required = false)
	private String buName;
	/**
	 * 事业部id
	 */
	@ApiModelProperty(value = "事业部id", required = false)
	private Long buId;

	@ApiModelProperty(hidden = true)
	private List<Long> buIds;
	/**
	 * po号
	 */
	@ApiModelProperty(value = "po号", required = false)
	private String poNo;
	/**
	 * 物流企业名称
	 */
	@ApiModelProperty(value = "物流企业名称", required = false)
	private String logisticsName;
	/**
	 * 进口模式10：BBC;20：BC;30：保留; 40：CC
	 */
	@ApiModelProperty(value = "进口模式10：BBC;20：BC;30：保留; 40：CC", required = false)
	private String importMode;
	@ApiModelProperty(value = "进口模式中文10：BBC;20：BC;30：保留; 40：CC", required = false)
	private String importModeLabel;
	/**
	 * LBX单号
	 */
	@ApiModelProperty(value = "LBX单号", required = false)
	private String lbxNo;
	/**
	 * 提单号
	 */
	@ApiModelProperty(value = "提单号", required = false)
	private String blNo;
	/**
	 * 运单号
	 */
	@ApiModelProperty(value = "运单号", required = false)
	private String wayBillNo;
	/**
	 * 审核人
	 */
	@ApiModelProperty(value = "审核人", required = false)
	private Long auditor;
	/**
	 * 审核人名称
	 */
	@ApiModelProperty(value = "审核人名称", required = false)
	private String auditName;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人", required = false)
	private Long creater;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", required = false)
	private Timestamp createDate;
	/**
	 * 创建人名称
	 */
	@ApiModelProperty(value = "创建人名称", required = false)
	private String createName;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间", required = false)
	private Timestamp modifyDate;

	/**
	 * 出库时间
	 */
	@ApiModelProperty(value = "出库时间开始", required = false)
	private String returnOutStartDate;
	@ApiModelProperty(value = "出库时间结束", required = false)
	private String returnOutEndDate;

	// 理货单位
	@ApiModelProperty(value = "理货单位", required = false)
	private String tallyingUnit;
	@ApiModelProperty(value = "理货单位中文", required = false)
	private String tallyingUnitLabel;

	// 采购退货ID
	@ApiModelProperty(value = "采购退货订单ID", required = false)
	private Long purchaseReturnId;

	/**
	 * 外部订单号
	 */
	@ApiModelProperty(value = "外部订单号", required = false)
	private String extraCode;

	@ApiModelProperty(value = "表体信息", required = false)
	private List<PurchaseReturnOdepotItemModel> itemList ;
	
	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* purchaseReturnCode get 方法 */
	public String getPurchaseReturnCode() {
		return purchaseReturnCode;
	}

	/* purchaseReturnCode set 方法 */
	public void setPurchaseReturnCode(String purchaseReturnCode) {
		this.purchaseReturnCode = purchaseReturnCode;
	}

	/* outDepotId get 方法 */
	public Long getOutDepotId() {
		return outDepotId;
	}

	/* outDepotId set 方法 */
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	/* outDepotName get 方法 */
	public String getOutDepotName() {
		return outDepotName;
	}

	/* outDepotName set 方法 */
	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}

	/* returnOutDate get 方法 */
	public Timestamp getReturnOutDate() {
		return returnOutDate;
	}

	/* returnOutDate set 方法 */
	public void setReturnOutDate(Timestamp returnOutDate) {
		this.returnOutDate = returnOutDate;
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

	/* status get 方法 */
	public String getStatus() {
		return status;
	}

	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
		this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseReturnOrderOdepot_statusList, status);
	}

	/* auditDate get 方法 */
	public Timestamp getAuditDate() {
		return auditDate;
	}

	/* auditDate set 方法 */
	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	/* supplierId get 方法 */
	public Long getSupplierId() {
		return supplierId;
	}

	/* supplierId set 方法 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	/* supplierName get 方法 */
	public String getSupplierName() {
		return supplierName;
	}

	/* supplierName set 方法 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	/* currency get 方法 */
	public String getCurrency() {
		return currency;
	}

	/* currency set 方法 */
	public void setCurrency(String currency) {
		this.currency = currency;
		this.currencyLabel = DERP_ORDER.getLabelByKey(DERP.currencyCodeList, currency);
	}

	/* buName get 方法 */
	public String getBuName() {
		return buName;
	}

	/* buName set 方法 */
	public void setBuName(String buName) {
		this.buName = buName;
	}

	/* buId get 方法 */
	public Long getBuId() {
		return buId;
	}

	/* buId set 方法 */
	public void setBuId(Long buId) {
		this.buId = buId;
	}

	/* poNo get 方法 */
	public String getPoNo() {
		return poNo;
	}

	/* poNo set 方法 */
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	/* logisticsName get 方法 */
	public String getLogisticsName() {
		return logisticsName;
	}

	/* logisticsName set 方法 */
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	/* importMode get 方法 */
	public String getImportMode() {
		return importMode;
	}

	/* importMode set 方法 */
	public void setImportMode(String importMode) {
		this.importMode = importMode;
		this.importModeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_importModeList, importMode);
	}

	/* lbxNo get 方法 */
	public String getLbxNo() {
		return lbxNo;
	}

	/* lbxNo set 方法 */
	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	/* blNo get 方法 */
	public String getBlNo() {
		return blNo;
	}

	/* blNo set 方法 */
	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	/* wayBillNo get 方法 */
	public String getWayBillNo() {
		return wayBillNo;
	}

	/* wayBillNo set 方法 */
	public void setWayBillNo(String wayBillNo) {
		this.wayBillNo = wayBillNo;
	}

	/* auditor get 方法 */
	public Long getAuditor() {
		return auditor;
	}

	/* auditor set 方法 */
	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}

	/* auditName get 方法 */
	public String getAuditName() {
		return auditName;
	}

	/* auditName set 方法 */
	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* createName get 方法 */
	public String getCreateName() {
		return createName;
	}

	/* createName set 方法 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/* modifyDate get 方法 */
	public Timestamp getModifyDate() {
		return modifyDate;
	}

	/* modifyDate set 方法 */
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}

	public String getImportModeLabel() {
		return importModeLabel;
	}

	public void setImportModeLabel(String importModeLabel) {
		this.importModeLabel = importModeLabel;
	}

	public String getReturnOutStartDate() {
		return returnOutStartDate;
	}

	public void setReturnOutStartDate(String returnOutStartDate) {
		this.returnOutStartDate = returnOutStartDate;
	}

	public String getReturnOutEndDate() {
		return returnOutEndDate;
	}

	public void setReturnOutEndDate(String returnOutEndDate) {
		this.returnOutEndDate = returnOutEndDate;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
		this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
	}

	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}

	public Long getPurchaseReturnId() {
		return purchaseReturnId;
	}

	public void setPurchaseReturnId(Long purchaseReturnId) {
		this.purchaseReturnId = purchaseReturnId;
	}

	public List<Long> getBuIds() {
		return buIds;
	}

	public void setBuIds(List<Long> buIds) {
		this.buIds = buIds;
	}

	public String getExtraCode() {
		return extraCode;
	}

	public void setExtraCode(String extraCode) {
		this.extraCode = extraCode;
	}

	public List<PurchaseReturnOdepotItemModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<PurchaseReturnOdepotItemModel> itemList) {
		this.itemList = itemList;
	}

}
