package com.topideal.entity.dto.purchase;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PurchaseReturnOrderExportDTO {

	/**
	 * id
	 */
	private Long id;
	/**
	 * 采购退货订单号
	 */
	private String code;
	/**
	 * 合同号
	 */
	private String contractNo;
	/**
	 * 退入仓库id
	 */
	private Long inDepotId;
	/**
	 * 退入仓库名称
	 */
	private String inDepotName;
	/**
	 * 退出仓库id
	 */
	private Long outDepotId;
	/**
	 * 退出仓库名称
	 */
	private String outDepotName;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 公司ID
	 */
	private Long merchantId;
	/**
	 * 公司名称
	 */
	private String merchantName;
	/**
	 * 创建时间
	 */
	private Timestamp createDate;
	/**
	 * 创建人
	 */
	private Long creater;
	/**
	 * 删除时间
	 */
	private Timestamp deletedDate;
	/**
	 * 状态：001-待审核 ,003-已审核 ,006-已删除 ,016已出仓 ,007已完结
	 */
	private String status;
	private String statusLabel;
	/**
	 * 审核人
	 */
	private Long auditor;
	/**
	 * 审核时间
	 */
	private Timestamp auditDate;
	/**
	 * 客户id
	 */
	private Long supplierId;
	/**
	 * 客户名称
	 */
	private String supplierName;
	/**
	 * 计划退货数量
	 */
	private Integer totalReturnNum;
	/**
	 * 创建人名称
	 */
	private String createName;
	/**
	 * 审核人名称
	 */
	private String auditName;
	/**
	 * 入仓仓库编码
	 */
	private String inDepotCode;
	/**
	 * 出仓仓库编码
	 */
	private String outDepotCode;
	/**
	 * 唛头
	 */
	private String mark;
	/**
	 * 修改时间
	 */
	private Timestamp modifyDate;
	/**
	 * 是否同关区（0-否，1-是）
	 */
	private String isSameArea;
	private String isSameAreaLabel;
	/**
	 * 币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元 07 英镑
	 */
	private String currency;
	private String currencyLabel;

	/**
	 * 海外仓理货单位 00-托盘 01-箱 02-件
	 */
	private String tallyingUnit;
	private String tallyingUnitLabel;
	/**
	 * 关联销售单单号
	 */
	private String purchaseOrderRelCode;
	/**
	 * 事业部名称
	 */
	private String buName;
	/**
	 * 事业部id
	 */
	private Long buId;
	/**
	 * po号
	 */
	private String poNo;
	/**
	 * 提货方式 1-邮寄 2-自提
	 */
	private String deliveryMethod;
	private String deliveryMethodLabel;
	/**
	 * 收货地址
	 */
	private String deliveryAddr;
	/**
	 * 国家
	 */
	private String country;
	/**
	 * 收货人名称
	 */
	private String deliveryName;
	/**
	 * 目的地
	 */
	private String destinationName;
	private String destinationNameLabel;

	/**
	 * 采购退货ID
	 */
	private Long orderId;
	/**
	 * 采购订单ID
	 */
	private Long purchaseOrderId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品货号
	 */
	private String goodsNo;
	/**
	 * 条形码
	 */
	private String barcode;
	/**
	 * 采购退货数量
	 */
	private Integer returnNum;
	/**
	 * 采购退货单价
	 */
	private BigDecimal returnPrice;

	/**
	 * 申报单价
	 */
	private BigDecimal declarePrice;

	/**
	 * 采购退货总金额
	 */
	private BigDecimal returnAmount;
	/**
	 * 箱号
	 */
	private String boxNo;
	/**
	 * 子合同号
	 */
	private String subcontractNo;
	/**
	 * 品牌类型
	 */
	private String brandName;
	/**
	 * 备注
	 */
	private String subremark;

	private String destinationAddress;

	/**
	 * 采购退货含税单价
	 */
	private BigDecimal taxReturnPrice;
	/**
	 * 采购退货含税总金额
	 */
	private BigDecimal taxReturnAmount;
	/**
	 * 税额
	 */
	private BigDecimal tax;
	/**
	 * 税率
	 */
	private Double taxRate;

	private Long stockLocationTypeId;

	private String stockLocationTypeName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Long getInDepotId() {
		return inDepotId;
	}

	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}

	public String getInDepotName() {
		return inDepotName;
	}

	public void setInDepotName(String inDepotName) {
		this.inDepotName = inDepotName;
	}

	public Long getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	public String getOutDepotName() {
		return outDepotName;
	}

	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Timestamp getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Timestamp deletedDate) {
		this.deletedDate = deletedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseReturnOrder_statusList, status);
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public Long getAuditor() {
		return auditor;
	}

	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}

	public Timestamp getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getTotalReturnNum() {
		return totalReturnNum;
	}

	public void setTotalReturnNum(Integer totalReturnNum) {
		this.totalReturnNum = totalReturnNum;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public String getInDepotCode() {
		return inDepotCode;
	}

	public void setInDepotCode(String inDepotCode) {
		this.inDepotCode = inDepotCode;
	}

	public String getOutDepotCode() {
		return outDepotCode;
	}

	public void setOutDepotCode(String outDepotCode) {
		this.outDepotCode = outDepotCode;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getIsSameArea() {
		return isSameArea;
	}

	public void setIsSameArea(String isSameArea) {
		this.isSameArea = isSameArea;
		this.isSameAreaLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseReturnOrder_isSameAreaList, isSameArea);
	}

	public String getIsSameAreaLabel() {
		return isSameAreaLabel;
	}

	public void setIsSameAreaLabel(String isSameAreaLabel) {
		this.isSameAreaLabel = isSameAreaLabel;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
		this.currencyLabel = DERP_ORDER.getLabelByKey(DERP.currencyCodeList, currency);
	}

	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
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

	public String getPurchaseOrderRelCode() {
		return purchaseOrderRelCode;
	}

	public void setPurchaseOrderRelCode(String purchaseOrderRelCode) {
		this.purchaseOrderRelCode = purchaseOrderRelCode;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
		this.deliveryMethodLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseReturnOrder_mailModeList,
				deliveryMethod);
	}

	public String getDeliveryMethodLabel() {
		return deliveryMethodLabel;
	}

	public void setDeliveryMethodLabel(String deliveryMethodLabel) {
		this.deliveryMethodLabel = deliveryMethodLabel;
	}

	public String getDeliveryAddr() {
		return deliveryAddr;
	}

	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;

	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
		this.destinationNameLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseReturnOrder_destinationList,
				destinationName);
	}

	public String getDestinationNameLabel() {
		return destinationNameLabel;
	}

	public void setDestinationNameLabel(String destinationNameLabel) {
		this.destinationNameLabel = destinationNameLabel;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(Long purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(Integer returnNum) {
		this.returnNum = returnNum;
	}

	public BigDecimal getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(BigDecimal returnPrice) {
		this.returnPrice = returnPrice;
	}

	public BigDecimal getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(BigDecimal returnAmount) {
		this.returnAmount = returnAmount;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getSubcontractNo() {
		return subcontractNo;
	}

	public void setSubcontractNo(String subcontractNo) {
		this.subcontractNo = subcontractNo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getSubremark() {
		return subremark;
	}

	public void setSubremark(String subremark) {
		this.subremark = subremark;
	}

	public BigDecimal getDeclarePrice() {
		return declarePrice;
	}

	public void setDeclarePrice(BigDecimal declarePrice) {
		this.declarePrice = declarePrice;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public BigDecimal getTaxReturnPrice() {
		return taxReturnPrice;
	}

	public void setTaxReturnPrice(BigDecimal taxReturnPrice) {
		this.taxReturnPrice = taxReturnPrice;
	}

	public BigDecimal getTaxReturnAmount() {
		return taxReturnAmount;
	}

	public void setTaxReturnAmount(BigDecimal taxReturnAmount) {
		this.taxReturnAmount = taxReturnAmount;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Long getStockLocationTypeId() {
		return stockLocationTypeId;
	}

	public void setStockLocationTypeId(Long stockLocationTypeId) {
		this.stockLocationTypeId = stockLocationTypeId;
	}
}
