package com.topideal.entity.vo.order;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 销售退货订单
 * 
 * @author lian_
 *
 */
public class SaleReturnOrderModel extends PageModel implements Serializable {

	// 销售退货订单号
	private String code;
	// 合同号
	private String contractNo;
	// 退出仓库id
	private Long outDepotId;
	// 备注
	private String remark;
	// 申报地海关编码
	private String customsNo;
	// 商家名称
	private String merchantName;
	// 商家ID
	private Long merchantId;
	// 客户id
	private Long customerId;
	// 业务场景 账册内货权转移/账册内货权转移调仓
	private String model;
	// id
	private Long id;
	// 创建时间
	private Timestamp createDate;
	// 审核时间
	private Timestamp auditDate;
	// 审核人
	private Long auditor;
	// 申报地国检编码
	private String inspectNo;
	// 客户名称
	private String customerName;
	// 退入仓库名称
	private String inDepotName;
	// 退出仓库名称
	private String outDepotName;
	// 企业退运单号
	private String merchantReturnNo;
	// 删除时间
	private Timestamp deletedDate;
	// 计划退货数量
	private Integer totalReturnNum;
	// 退入仓库id
	private Long inDepotId;
	// 创建人
	private Long creater;
	// 服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务
	private String serveTypes;
	// 状态：0-待审核 1-已审核 2-已删除
	private String status;
	// 发票号
	private String invoiceNo;
	// 收货地址
	private String deliveryAddr;
	// 装货港
	private String portLoading;
	// 包装方式
	private String packType;
	// 二程提单号
	private String blNo;
	// 箱数
	private Integer boxNum;
	// 付款条约
	private String payRules;
	// 提单毛重
	private Double billWeight;
	// 头程提单号
	private String ladingBill;
	// 卸货港编码
	private String portDestNo;
	// 邮箱地址
	private String email;
	// LBX单号
	private String lbxNo;
	// 审核人名称
	private String auditName;
	// 创建人名称
	private String createName;
	// 入仓仓库编码
	private String inDepotCode;
	// 出仓仓库编码
	private String outDepotCode;
	// 卓志编码
	private String topidealCode;
	// 境外发货人
	private String shipper;
	// 唛头
	private String mark;
	// 修改时间
	private Timestamp modifyDate;
	// 是否同关区（0-否，1-是）
	private String isSameArea;
	// 销售退货类型 1-消费者退货 2-代销退货
	private String returnType;
	//云集结算账单号
	private String yunjiAccountNo;
	//币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元  07 英镑
	private String currency;

	// ------扩展字段-----------------------
	private String saleOrderCode;// 销售单号（用于列表查询）

	//海外仓理货单位 00-托盘 01-箱 02-件
	private String tallyingUnit;

	//关联销售单单号
	private String saleOrderRelCode;

	/**
	 * 事业部名称
	 */
	private String buName;
	/**
	 *  事业部id
	 */
	private Long buId;

	/**
	 * PO号
	 */
	private String poNo;
	/**
	 * 退货方式 1-退货退款，2-仅退货
	 */
	private String returnMode;
	/**
	 * 关联销售单id
	 */
	private Long saleOrderId;
	/**
	 * 是否已生成预申报单 1-是,0-否
	 */
	private String isGenerateDeclare;
	/**
	 * 业务模式 1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结
	 */
	private String businessModel;
	/**
	 * 是否已生成tob暂估 1-是,0-否
	 */
	private String isGenerateTemp;
	/**
	 * 平台售后单号
	 */
	private String platformReturnCode;

	/**
	 * 事业部库位类型ID
	 */
	private Long stockLocationTypeId;
	/**
	 * 库位类型
	 */
	private String stockLocationTypeName;


	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getShipper() {
		return shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	/* topidealCode get 方法 */
	public String getTopidealCode() {
		return topidealCode;
	}

	/* topidealCode set 方法 */
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}

	/* outDepotCode get 方法 */
	public String getOutDepotCode() {
		return outDepotCode;
	}

	/* outDepotCode set 方法 */
	public void setOutDepotCode(String outDepotCode) {
		this.outDepotCode = outDepotCode;
	}

	/* inDepotCode get 方法 */
	public String getInDepotCode() {
		return inDepotCode;
	}

	/* inDepotCode set 方法 */
	public void setInDepotCode(String inDepotCode) {
		this.inDepotCode = inDepotCode;
	}

	/* createName get 方法 */
	public String getCreateName() {
		return createName;
	}

	/* createName set 方法 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/* auditName get 方法 */
	public String getAuditName() {
		return auditName;
	}

	/* auditName set 方法 */
	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	/* lbxNo get 方法 */
	public String getLbxNo() {
		return lbxNo;
	}

	/* lbxNo set 方法 */
	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	/* email get 方法 */
	public String getEmail() {
		return email;
	}

	/* email set 方法 */
	public void setEmail(String email) {
		this.email = email;
	}

	/* portDestNo get 方法 */
	public String getPortDestNo() {
		return portDestNo;
	}

	/* portDestNo set 方法 */
	public void setPortDestNo(String portDestNo) {
		this.portDestNo = portDestNo;
	}

	/* ladingBill get 方法 */
	public String getLadingBill() {
		return ladingBill;
	}

	/* ladingBill set 方法 */
	public void setLadingBill(String ladingBill) {
		this.ladingBill = ladingBill;
	}

	/* billWeight get 方法 */
	public Double getBillWeight() {
		return billWeight;
	}

	/* billWeight set 方法 */
	public void setBillWeight(Double billWeight) {
		this.billWeight = billWeight;
	}

	/* payRules get 方法 */
	public String getPayRules() {
		return payRules;
	}

	/* payRules set 方法 */
	public void setPayRules(String payRules) {
		this.payRules = payRules;
	}

	/* boxNum get 方法 */
	public Integer getBoxNum() {
		return boxNum;
	}

	/* boxNum set 方法 */
	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}

	/* blNo get 方法 */
	public String getBlNo() {
		return blNo;
	}

	/* blNo set 方法 */
	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	/* packType get 方法 */
	public String getPackType() {
		return packType;
	}

	/* packType set 方法 */
	public void setPackType(String packType) {
		this.packType = packType;
	}

	/* portLoading get 方法 */
	public String getPortLoading() {
		return portLoading;
	}

	/* portLoading set 方法 */
	public void setPortLoading(String portLoading) {
		this.portLoading = portLoading;
	}

	/* deliveryAddr get 方法 */
	public String getDeliveryAddr() {
		return deliveryAddr;
	}

	/* deliveryAddr set 方法 */
	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}

	/* invoiceNo get 方法 */
	public String getInvoiceNo() {
		return invoiceNo;
	}

	/* invoiceNo set 方法 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* contractNo get 方法 */
	public String getContractNo() {
		return contractNo;
	}

	/* contractNo set 方法 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	/* outDepotId get 方法 */
	public Long getOutDepotId() {
		return outDepotId;
	}

	/* outDepotId set 方法 */
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	/* remark get 方法 */
	public String getRemark() {
		return remark;
	}

	/* remark set 方法 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/* customsNo get 方法 */
	public String getCustomsNo() {
		return customsNo;
	}

	/* customsNo set 方法 */
	public void setCustomsNo(String customsNo) {
		this.customsNo = customsNo;
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/* customerId get 方法 */
	public Long getCustomerId() {
		return customerId;
	}

	/* customerId set 方法 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/* model get 方法 */
	public String getModel() {
		return model;
	}

	/* model set 方法 */
	public void setModel(String model) {
		this.model = model;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* auditDate get 方法 */
	public Timestamp getAuditDate() {
		return auditDate;
	}

	/* auditDate set 方法 */
	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	/* auditor get 方法 */
	public Long getAuditor() {
		return auditor;
	}

	/* auditor set 方法 */
	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}

	/* inspectNo get 方法 */
	public String getInspectNo() {
		return inspectNo;
	}

	/* inspectNo set 方法 */
	public void setInspectNo(String inspectNo) {
		this.inspectNo = inspectNo;
	}

	/* customerName get 方法 */
	public String getCustomerName() {
		return customerName;
	}

	/* customerName set 方法 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/* inDepotName get 方法 */
	public String getInDepotName() {
		return inDepotName;
	}

	/* inDepotName set 方法 */
	public void setInDepotName(String inDepotName) {
		this.inDepotName = inDepotName;
	}

	/* outDepotName get 方法 */
	public String getOutDepotName() {
		return outDepotName;
	}

	/* outDepotName set 方法 */
	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}

	/* merchantReturnNo get 方法 */
	public String getMerchantReturnNo() {
		return merchantReturnNo;
	}

	/* merchantReturnNo set 方法 */
	public void setMerchantReturnNo(String merchantReturnNo) {
		this.merchantReturnNo = merchantReturnNo;
	}

	/* deletedDate get 方法 */
	public Timestamp getDeletedDate() {
		return deletedDate;
	}

	/* deletedDate set 方法 */
	public void setDeletedDate(Timestamp deletedDate) {
		this.deletedDate = deletedDate;
	}

	/* totalReturnNum get 方法 */
	public Integer getTotalReturnNum() {
		return totalReturnNum;
	}

	/* totalReturnNum set 方法 */
	public void setTotalReturnNum(Integer totalReturnNum) {
		this.totalReturnNum = totalReturnNum;
	}

	/* inDepotId get 方法 */
	public Long getInDepotId() {
		return inDepotId;
	}

	/* inDepotId set 方法 */
	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* serveTypes get 方法 */
	public String getServeTypes() {
		return serveTypes;
	}

	/* serveTypes set 方法 */
	public void setServeTypes(String serveTypes) {
		this.serveTypes = serveTypes;
	}

	/* status get 方法 */
	public String getStatus() {
		return status;
	}

	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
	}

	public String getSaleOrderCode() {
		return saleOrderCode;
	}

	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}

	public String getIsSameArea() {
		return isSameArea;
	}

	public void setIsSameArea(String isSameArea) {
		this.isSameArea = isSameArea;
	}

	public String getYunjiAccountNo() {
		return yunjiAccountNo;
	}

	public void setYunjiAccountNo(String yunjiAccountNo) {
		this.yunjiAccountNo = yunjiAccountNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	public String getSaleOrderRelCode() {
		return saleOrderRelCode;
	}

	public void setSaleOrderRelCode(String saleOrderRelCode) {
		this.saleOrderRelCode = saleOrderRelCode;
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

	public String getReturnMode() {
		return returnMode;
	}

	public void setReturnMode(String returnMode) {
		this.returnMode = returnMode;
	}

	public Long getSaleOrderId() {
		return saleOrderId;
	}

	public void setSaleOrderId(Long saleOrderId) {
		this.saleOrderId = saleOrderId;
	}

	public String getIsGenerateDeclare() {
		return isGenerateDeclare;
	}

	public void setIsGenerateDeclare(String isGenerateDeclare) {
		this.isGenerateDeclare = isGenerateDeclare;
	}

	public String getBusinessModel() {
		return businessModel;
	}

	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}

	public String getIsGenerateTemp() {
		return isGenerateTemp;
	}

	public void setIsGenerateTemp(String isGenerateTemp) {
		this.isGenerateTemp = isGenerateTemp;
	}

	public String getPlatformReturnCode() {
		return platformReturnCode;
	}

	public void setPlatformReturnCode(String platformReturnCode) {
		this.platformReturnCode = platformReturnCode;
	}

	public Long getStockLocationTypeId() {
		return stockLocationTypeId;
	}

	public void setStockLocationTypeId(Long stockLocationTypeId) {
		this.stockLocationTypeId = stockLocationTypeId;
	}

	public String getStockLocationTypeName() {
		return stockLocationTypeName;
	}

	public void setStockLocationTypeName(String stockLocationTypeName) {
		this.stockLocationTypeName = stockLocationTypeName;
	}
}
