package com.topideal.entity.vo.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 销售订单
 * 
 * @author lian_
 *
 */
public class SaleOrderModel extends PageModel implements Serializable {

	// LBX单号
	private String lbxNo;
	// 销售订单编号
	private String code;
	// 修改时间
	private Timestamp modifyDate;
	// 调出仓库ID
	private Long outDepotId;
	// 业务模式  1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结
	private String businessModel;
	// 修改人
	private Long modifier;
	// 审核人
	private Long auditor;
	// 客户名称
	private String customerName;
	// 商家名称
	private String merchantName;
	// 参照订单
	private String referToOrder;
	// 调出仓库名称
	private String outDepotName;
	// po单号
	private String poNo;
	// 商家ID
	private Long merchantId;
	// 客户ID(供应商)
	private Long customerId;
	// 创建人
	private Long creater;
	// 业务场影 账册内调拨 枚举
	private String model;
	// id
	private Long id;
	// 订单状态:001:待审核,002:审核中,003:已审核,006:已删除,007:已完结,018:已出库,027:出库中,025：已签收 026：已上架 031-部分上架
	private String state;
	// 交货日期
	private Timestamp deliveryDate;
	// 创建时间
	private Timestamp createDate;
	// 审核时间
	private Timestamp auditDate;
	// 备注
	private String remark;
	// 销售订单总数量
	private Integer totalNum;
	// 销售订单总金额
	private BigDecimal totalAmount;
	// 入仓仓库id
	private Long inDepotId;
	// 入仓仓库
	private String inDepotName;
	// 审核人名称
	private String auditName;
	// 创建人名称
	private String createName;
	// 服务类型
	private String serveTypes;
	// 合同号
	private String contractNo;
	// 申报地海关编码
	private String customsNo;
	// 申报地国检编码
	private String inspectNo;
	// 出仓仓库编码
	private String outDepotCode;
	// 入仓仓库编码
	private String inDepotCode;
	// 出库数量
	private Integer outDepotNum;
	// 卓志编码
	private String topidealCode;
	// 是否同关区（0-否，1-是）
	private String isSameArea;
	// 完结时间
	private Timestamp endDate;
	// 理货单位 00-托盘 01-箱 02-件
	private String tallyingUnit;
	// 国家
	private String country;
	// 目的地
	private String destination;
	// 收件人名称
	private String receiverName;
	// 收件人地址
	private String receiverAddress;
	// 收件人地址
	private String mailMode;
	// 订单来源 1：人工录入；2：系统自动生成
	private String orderSource;
	// 归属月份
	private String ownMonth;
	// 原销出仓仓库ID
	private Long originalOutDepotId;
	// 原销出仓仓库名字
	private String originalOutDepotName;
	// 原销出仓仓库code
	private String originalOutDepotCode;

	// 商品信息-表体
	private List<SaleOrderItemModel> itemList;

	//供应商ID
	private Long supplierId;

	//供应商名称
	private String supplierName;

	//付款主体
	private String paySubject;

	// po单时间
	private Timestamp poDate;

	//币种
	private String currency;

	//修改人用户名
	private String modifierUsername;

	// 签收时间
	private Timestamp receiveDate;

	//签收人名称
	private String receiveName;

	//签收人
	private Long receiver;

	//事业部名称
	private String buName;

	/**
	 *  事业部id
	 */
	private Long buId;

	/**
	 * 单据标识  1-预售转销 2-非预售转销
	 */
	private String orderType;
	/**
	 * 关联预售单单号
	 */
	private String preSaleOrderRelCode;

	/**
	 * 提单毛重
	 */
	private Double billWeight;
	/**
	 * 运输方式  海运、空运、陆运、港到仓拖车、中欧铁路、其他
	 */
	private String transport;
	/**
	 * 标准箱TEU
	 */
	private String teu;
	/**
	 * 车次
	 */
	private String trainno;
	/**
	 * 承运商
	 */
	private String carrier;
	/**
	 * 托数
	 */
	private Integer torusNumber;
	/**
	 * 出库地点
	 */
	private String outdepotAddr;

	//采购链路生成标识
	private String linkUniueCode;
	
	/**
    * 金额调整状态
    */
    private String amountStatus;
	//调整人
	private Long adjuster;
	//调整时间
	private Timestamp adjustDate;
	//调整人用户名
	private String adjusterUsername;
	/**
	 * 金额确认状态 0-未确认，1-确认通过，2-确认不通过
	 */
	private String amountConfirmStatus;
	/**
	 * 金额确认用户id
	 */
	private Long amountConfirmer;
	/**
	 * 金额确认用户名称
	 */
	private String amountConfirmUsername;
	/**
	 * 金额确认时间
	 */
	private Timestamp amountConfirmDate;
	/**
	 * 付款条约
	 */
	private String payRules;
	/**
	 * 发票单号
	 */
	private String invoiceNo;
	/**
	 * 卸货港
	 */
	private String portDes;
	/**
	 * 包装
	 */
	private String pack;
	/**
	 * 箱数
	 */
	private Integer boxNum;
	/**
	 * 托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱
	 */
	private String torrPackType;
	/**
	 * 出库关区id
	 */
	private Long outCustomsId;
	/**
	 * 出库关区编码
	 */
	private String outCustomsCode;
	/**
	 * 出库关区名称
	 */
	private String outPlatformCustoms;
	/**
	 * 入库关区id
	 */
	private Long inCustomsId;
	/**
	 * 入库关区编码
	 */
	private String inCustomsCode;
	/**
	 * 入库关区名称
	 */
	private String inPlatformCustoms;
	/**
	 * 提交人
	 */
	private Long submiter;
	/**
	 * 提交人名称
	 */
	private String submiterName;
	/**
	 * 提交时间
	 */
	private Timestamp submitDate;
	/**
	 * 融资状态 0-未申请，1-已申请，2-已赎回
	 */
	private String financeStatus;
	/**
    * 是否已生成预申报单 1-是,0-否
    */
    private String isGenerateDeclare;
    /**
     * 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款
     */
     private String paymentTerms;
     /**
     * 贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW
     */
     private String tradeTerms;

	/**
	 * 审批单号
	 */
	private String approvalNo;

	//红冲状态: 001-待红冲 002-红冲中 003-已红冲
	private String writeOffStatus;

	//红冲日期
	private Timestamp writeOffDate;

	/**
	 * 事业部库位类型ID
	 */
	private Long stockLocationTypeId;

	/**
	 * 库位类型
	 */
	private String stockLocationTypeName;

	public String getApprovalNo() {
		return approvalNo;
	}

	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}

	public String getOwnMonth() {
		return ownMonth;
	}

	public void setOwnMonth(String ownMonth) {
		this.ownMonth = ownMonth;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getMailMode() {
		return mailMode;
	}

	public void setMailMode(String mailMode) {
		this.mailMode = mailMode;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	/* endDate get 方法 */
	public Timestamp getEndDate() {
		return endDate;
	}

	/* endDate set 方法 */
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	/* isSameArea get 方法 */
	public String getIsSameArea() {
		return isSameArea;
	}

	/* isSameArea set 方法 */
	public void setIsSameArea(String isSameArea) {
		this.isSameArea = isSameArea;
	}

	/* topidealCode get 方法 */
	public String getTopidealCode() {
		return topidealCode;
	}

	/* topidealCode set 方法 */
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}

	/* inDepotCode get 方法 */
	public String getInDepotCode() {
		return inDepotCode;
	}

	/* inDepotCode set 方法 */
	public void setInDepotCode(String inDepotCode) {
		this.inDepotCode = inDepotCode;
	}

	/* outDepotCode get 方法 */
	public String getOutDepotCode() {
		return outDepotCode;
	}

	public Long getOriginalOutDepotId() {
		return originalOutDepotId;
	}

	public void setOriginalOutDepotId(Long originalOutDepotId) {
		this.originalOutDepotId = originalOutDepotId;
	}

	public String getOriginalOutDepotName() {
		return originalOutDepotName;
	}

	public void setOriginalOutDepotName(String originalOutDepotName) {
		this.originalOutDepotName = originalOutDepotName;
	}

	public String getOriginalOutDepotCode() {
		return originalOutDepotCode;
	}

	public void setOriginalOutDepotCode(String originalOutDepotCode) {
		this.originalOutDepotCode = originalOutDepotCode;
	}

	/* outDepotCode set 方法 */
	public void setOutDepotCode(String outDepotCode) {
		this.outDepotCode = outDepotCode;
	}

	/* inspectNo get 方法 */
	public String getInspectNo() {
		return inspectNo;
	}

	/* inspectNo set 方法 */
	public void setInspectNo(String inspectNo) {
		this.inspectNo = inspectNo;
	}

	/* customsNo get 方法 */
	public String getCustomsNo() {
		return customsNo;
	}

	/* customsNo set 方法 */
	public void setCustomsNo(String customsNo) {
		this.customsNo = customsNo;
	}

	/* contractNo get 方法 */
	public String getContractNo() {
		return contractNo;
	}

	/* contractNo set 方法 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	/* serverTypes get 方法 */
	public String getServeTypes() {
		return serveTypes;
	}

	/* serverTypes set 方法 */
	public void setServeTypes(String serveTypes) {
		this.serveTypes = serveTypes;
	}

	/* auditName get 方法 */
	public String getAuditName() {
		return auditName;
	}

	/* auditName set 方法 */
	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	/* createName get 方法 */
	public String getCreateName() {
		return createName;
	}

	/* createName set 方法 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/* inDepotId get 方法 */
	public Long getInDepotId() {
		return inDepotId;
	}

	/* inDepotId set 方法 */
	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}

	/* inDepotName get 方法 */
	public String getInDepotName() {
		return inDepotName;
	}

	/* inDepotName set 方法 */
	public void setInDepotName(String inDepotName) {
		this.inDepotName = inDepotName;
	}

	/* remark get 方法 */
	public String getRemark() {
		return remark;
	}

	/* remark set 方法 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/* lbxNo get 方法 */
	public String getLbxNo() {
		return lbxNo;
	}

	/* lbxNo set 方法 */
	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* modifyDate get 方法 */
	public Timestamp getModifyDate() {
		return modifyDate;
	}

	/* modifyDate set 方法 */
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	/* outDepotId get 方法 */
	public Long getOutDepotId() {
		return outDepotId;
	}

	/* outDepotId set 方法 */
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	/* businessModel get 方法 */
	public String getBusinessModel() {
		return businessModel;
	}

	/* businessModel set 方法 */
	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}

	/* modifier get 方法 */
	public Long getModifier() {
		return modifier;
	}

	/* modifier set 方法 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	/* auditor get 方法 */
	public Long getAuditor() {
		return auditor;
	}

	/* auditor set 方法 */
	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}

	/* customerName get 方法 */
	public String getCustomerName() {
		return customerName;
	}

	/* customerName set 方法 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/* referToOrder get 方法 */
	public String getReferToOrder() {
		return referToOrder;
	}

	/* referToOrder set 方法 */
	public void setReferToOrder(String referToOrder) {
		this.referToOrder = referToOrder;
	}

	/* outDepotName get 方法 */
	public String getOutDepotName() {
		return outDepotName;
	}

	/* outDepotName set 方法 */
	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}

	/* poNo get 方法 */
	public String getPoNo() {
		return poNo;
	}

	/* poNo set 方法 */
	public void setPoNo(String poNo) {
		this.poNo = poNo;
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

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
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

	/* state get 方法 */
	public String getState() {
		return state;
	}

	/* state set 方法 */
	public void setState(String state) {
		this.state = state;
	}

	/* deliveryDate get 方法 */
	public Timestamp getDeliveryDate() {
		return deliveryDate;
	}

	/* deliveryDate set 方法 */
	public void setDeliveryDate(Timestamp deliveryDate) {
		this.deliveryDate = deliveryDate;
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

	public List<SaleOrderItemModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleOrderItemModel> itemList) {
		this.itemList = itemList;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getOutDepotNum() {
		return outDepotNum;
	}

	public void setOutDepotNum(Integer outDepotNum) {
		this.outDepotNum = outDepotNum;
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

	public String getPaySubject() {
		return paySubject;
	}

	public void setPaySubject(String paySubject) {
		this.paySubject = paySubject;
	}

	public Timestamp getPoDate() {
		return poDate;
	}

	public void setPoDate(Timestamp poDate) {
		this.poDate = poDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getModifierUsername() {
		return modifierUsername;
	}

	public void setModifierUsername(String modifierUsername) {
		this.modifierUsername = modifierUsername;
	}

	public Timestamp getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Timestamp receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public Long getReceiver() {
		return receiver;
	}

	public void setReceiver(Long receiver) {
		this.receiver = receiver;
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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getPreSaleOrderRelCode() {
		return preSaleOrderRelCode;
	}

	public void setPreSaleOrderRelCode(String preSaleOrderRelCode) {
		this.preSaleOrderRelCode = preSaleOrderRelCode;
	}

	public Double getBillWeight() {
		return billWeight;
	}

	public void setBillWeight(Double billWeight) {
		this.billWeight = billWeight;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getTeu() {
		return teu;
	}

	public void setTeu(String teu) {
		this.teu = teu;
	}

	public String getTrainno() {
		return trainno;
	}

	public void setTrainno(String trainno) {
		this.trainno = trainno;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public Integer getTorusNumber() {
		return torusNumber;
	}

	public void setTorusNumber(Integer torusNumber) {
		this.torusNumber = torusNumber;
	}

	public String getOutdepotAddr() {
		return outdepotAddr;
	}

	public void setOutdepotAddr(String outdepotAddr) {
		this.outdepotAddr = outdepotAddr;
	}

	public String getLinkUniueCode() {
		return linkUniueCode;
	}

	public void setLinkUniueCode(String linkUniueCode) {
		this.linkUniueCode = linkUniueCode;
	}

	public String getAmountStatus() {
		return amountStatus;
	}

	public void setAmountStatus(String amountStatus) {
		this.amountStatus = amountStatus;
	}

	public Long getAdjuster() {
		return adjuster;
	}

	public void setAdjuster(Long adjuster) {
		this.adjuster = adjuster;
	}

	public Timestamp getAdjustDate() {
		return adjustDate;
	}

	public void setAdjustDate(Timestamp adjustDate) {
		this.adjustDate = adjustDate;
	}

	public String getAdjusterUsername() {
		return adjusterUsername;
	}

	public void setAdjusterUsername(String adjusterUsername) {
		this.adjusterUsername = adjusterUsername;
	}

	public String getAmountConfirmStatus() {
		return amountConfirmStatus;
	}

	public void setAmountConfirmStatus(String amountConfirmStatus) {
		this.amountConfirmStatus = amountConfirmStatus;
	}

	public Long getAmountConfirmer() {
		return amountConfirmer;
	}

	public void setAmountConfirmer(Long amountConfirmer) {
		this.amountConfirmer = amountConfirmer;
	}

	public String getAmountConfirmUsername() {
		return amountConfirmUsername;
	}

	public void setAmountConfirmUsername(String amountConfirmUsername) {
		this.amountConfirmUsername = amountConfirmUsername;
	}

	public Timestamp getAmountConfirmDate() {
		return amountConfirmDate;
	}

	public void setAmountConfirmDate(Timestamp amountConfirmDate) {
		this.amountConfirmDate = amountConfirmDate;
	}

	public String getPayRules() {
		return payRules;
	}

	public void setPayRules(String payRules) {
		this.payRules = payRules;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getPortDes() {
		return portDes;
	}

	public void setPortDes(String portDes) {
		this.portDes = portDes;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public Integer getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}

	public String getTorrPackType() {
		return torrPackType;
	}

	public void setTorrPackType(String torrPackType) {
		this.torrPackType = torrPackType;
	}

	public Long getOutCustomsId() {
		return outCustomsId;
	}

	public void setOutCustomsId(Long outCustomsId) {
		this.outCustomsId = outCustomsId;
	}

	public String getOutCustomsCode() {
		return outCustomsCode;
	}

	public void setOutCustomsCode(String outCustomsCode) {
		this.outCustomsCode = outCustomsCode;
	}

	public String getOutPlatformCustoms() {
		return outPlatformCustoms;
	}

	public void setOutPlatformCustoms(String outPlatformCustoms) {
		this.outPlatformCustoms = outPlatformCustoms;
	}

	public Long getInCustomsId() {
		return inCustomsId;
	}

	public void setInCustomsId(Long inCustomsId) {
		this.inCustomsId = inCustomsId;
	}

	public String getInCustomsCode() {
		return inCustomsCode;
	}

	public void setInCustomsCode(String inCustomsCode) {
		this.inCustomsCode = inCustomsCode;
	}

	public String getInPlatformCustoms() {
		return inPlatformCustoms;
	}

	public void setInPlatformCustoms(String inPlatformCustoms) {
		this.inPlatformCustoms = inPlatformCustoms;
	}

	public Long getSubmiter() {
		return submiter;
	}

	public void setSubmiter(Long submiter) {
		this.submiter = submiter;
	}

	public String getSubmiterName() {
		return submiterName;
	}

	public void setSubmiterName(String submiterName) {
		this.submiterName = submiterName;
	}

	public Timestamp getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Timestamp submitDate) {
		this.submitDate = submitDate;
	}

	public String getFinanceStatus() {
		return financeStatus;
	}

	public void setFinanceStatus(String financeStatus) {
		this.financeStatus = financeStatus;
	}

	public String getIsGenerateDeclare() {
		return isGenerateDeclare;
	}

	public void setIsGenerateDeclare(String isGenerateDeclare) {
		this.isGenerateDeclare = isGenerateDeclare;
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


	public String getWriteOffStatus() {
		return writeOffStatus;
	}

	public void setWriteOffStatus(String writeOffStatus) {
		this.writeOffStatus = writeOffStatus;
	}

	public Timestamp getWriteOffDate() {
		return writeOffDate;
	}

	public void setWriteOffDate(Timestamp writeOffDate) {
		this.writeOffDate = writeOffDate;
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
