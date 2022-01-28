package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

/**
 * 采购订单
 * @author zhanghx
 */
public class PurchaseOrderDTO extends PageModel implements Serializable {

	// 供应商名称
	private String supplierName;
	// 供应商编码
	private String supplierCode;
	// 供应商主数据ID
	private String supplierMainId;
	// 仓库名称
	private String depotName;
	// 采购订单编号
	private String code;
	// 供应商ID
	private Long supplierId;
	// 付款主体 1 卓普信 2 商家
	private String paySubject;
	// 修改时间
	private Timestamp modifyDate;
	// 开发票时间
	private Timestamp drawInvoiceDate;
	// 合同号
	private String contractNo;
	// 业务模式 1 采购 2 代销
	private String businessModel;
	// 修改人
	private Long modifier;
	// 仓库ID
	private Long depotId;
	// 商家名称
	private String merchantName;
	// 预计入仓时间
	private Timestamp arriveDepotDate;
	// PO号
	private String poNo;
	// 商家ID
	private Long merchantId;
	// 交货地点
	private String deliveryAddr;
	// 创建人
	private Long creater;
	// id
	private Long id;
	// 创建时间
	private Timestamp createDate;
	// 状态 001,待审核,003,已审核,006,已删除,007,已完结
	private String status;
	// 状态 001,待审核,003,已审核,006,已删除,007,已完结
	private String statusLabel;
	// 装船时间
	private Timestamp shipDate;
	// 装货港
	private String loadPort;
	// 预计付款时间
	private Timestamp paymentDate;
	// 融资申请号
	private String financingNo;
	// 备注
	private String remark;
	// 来源
	private String source;
	// 汇率
	private Double rate;
	// 提单号
	private String ladingBill;
	// 提单毛重
	private Double grossWeight;
	// 卸货港
	private String unloadPort;
	// 是否已生成预申报单(1是,0否)
	private String isGenerate;
	// 完结入库时间
	private Timestamp endDate;
	// LBX单号
	private String lbxNo;
	// 审核人id
	private Long auditer;
	// 审核时间
	private Timestamp auditDate;
	// 审核人用户名
	private String auditName;
	// 创建人用户名
	private String createName;
	// 是否完结(1-是，0-否)
	private String isEnd;
	// 币种
	private String currency;
	// 卓志编码
	private String topidealCode;
	// 理货单位 00-托盘 01-箱 02-件
	private String tallyingUnit;
	// 理货单位 00-托盘 01-箱 02-件
	private String tallyingUnitLabel;
	// 开票人
	private String invoiceName;
	// 付款人
	private String payName;
	// 发送邮件状态(0 未发送邮件 1,发送邮件1次 2 发送邮件2次)
	private String mailStatus;
	// 发票日期
	private Timestamp invoiceDate;
	// 付款日期
	private Timestamp payDate;
	//发票号
    private String invoiceNo;

    private String modifierUsername;

	/**
	 * 事业部名称
	 */
	private String buName;
	/**
	 *  事业部id
	 */
	private Long buId;

	//归属时间
	private Timestamp attributionDate;

	/**
	 * 车次
	 */
	private String trainNumber;
	/**
	 * 标准箱TEU
	 */
	private String standardCaseTeu;
	/**
	 * 托数
	 */
	private Integer torrNum;
	/**
	 * 承运商
	 */
	private String carrier;
	//本位币
	private String tgtCurrency;
	//货权切割日
	private Timestamp cargoCuttingDate;
	//采购链路生成标识
	private String linkUniueCode;
	
	private Timestamp inboundDate ;

	//金额调整状态1-已调整 0-未调整
	private String amountAdjustmentStatus;

	// 加价比例
	private Double priceIncreaseRate;
	//金额调整人
	private Long amountAdjustmenter;
	//金额调整人名
	private String amountAdjustmentName;
	//金额调整时间
	private Timestamp amountAdjustmentDate;
	/**
	 * 金额确认状态1-已确认 0-未确认
	 */
	private String amountConfirmStatus;
	/**
	 * 金额确认人
	 */
	private Long amountConfirmer;
	/**
	 * 金额确认人名
	 */
	private String amountConfirmerName;
	/**
	 * 金额确认时间
	 */
	private Timestamp amountConfirmerDate;

	/**
	 * 提交人
	 */
	private Long submiter;
	/**
	 * 提交人用户名
	 */
	private String submiterName;
	/**
	 * 提交时间
	 */
	private Timestamp submitDate;
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
	//融资备注
	private String financingRemark;
	/**
	 * 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款
	 */
	private String paymentProvision;
	/**
	 * 贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW
	 */
	private String tradeTerms;
	/**
    * 框架合同号
    */
    private String frameContractNo;
    /**
    * 审批单号
    */
    private String approvalNo;

	/**
	 * 红冲状态: 001-待红冲 002-红冲中 003-已红冲
	 */
	private String writeOffStatus;

	/**
	 * 红冲日期
	 */
	private Timestamp writeOffDate;

	//预计发货时间
	private Timestamp estimateDeliverDate;
	//采购方式 0-以销定采 1-备货(已立项) 2-备货（集团自主） 3-试单
	private String purchaseMethod;
	//审批方式 1-OA审批 2-经分销审批
	private String auditMethod;
	//采购试单申请编号
	private String tryApplyCode;
	//OA流程单号
	private String requestId;
	/**
	 * 库位类型id
	 */
	private Long stockLocationTypeId;
	/**
	 * 库位类型
	 */
	private String stockLocationTypeName;


	public String getFrameContractNo() {
		return frameContractNo;
	}

	public void setFrameContractNo(String frameContractNo) {
		this.frameContractNo = frameContractNo;
	}

	public Timestamp getCargoCuttingDate() {
		return cargoCuttingDate;
	}

	public void setCargoCuttingDate(Timestamp cargoCuttingDate) {
		this.cargoCuttingDate = cargoCuttingDate;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getPaySubject() {
		return paySubject;
	}

	public void setPaySubject(String paySubject) {
		this.paySubject = paySubject;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getBusinessModel() {
		return businessModel;
	}

	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	public Long getDepotId() {
		return depotId;
	}

	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Timestamp getArriveDepotDate() {
		return arriveDepotDate;
	}

	public void setArriveDepotDate(Timestamp arriveDepotDate) {
		this.arriveDepotDate = arriveDepotDate;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getDeliveryAddr() {
		return deliveryAddr;
	}

	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_statusList, status);
	}
	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public Timestamp getShipDate() {
		return shipDate;
	}

	public void setShipDate(Timestamp shipDate) {
		this.shipDate = shipDate;
	}

	public String getLoadPort() {
		return loadPort;
	}

	public void setLoadPort(String loadPort) {
		this.loadPort = loadPort;
	}

	public Timestamp getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Timestamp paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getFinancingNo() {
		return financingNo;
	}

	public void setFinancingNo(String financingNo) {
		this.financingNo = financingNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getLadingBill() {
		return ladingBill;
	}

	public void setLadingBill(String ladingBill) {
		this.ladingBill = ladingBill;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public String getUnloadPort() {
		return unloadPort;
	}

	public void setUnloadPort(String unloadPort) {
		this.unloadPort = unloadPort;
	}

	public String getIsGenerate() {
		return isGenerate;
	}

	public void setIsGenerate(String isGenerate) {
		this.isGenerate = isGenerate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getLbxNo() {
		return lbxNo;
	}

	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	public Long getAuditer() {
		return auditer;
	}

	public void setAuditer(Long auditer) {
		this.auditer = auditer;
	}

	public Timestamp getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTopidealCode() {
		return topidealCode;
	}

	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
		this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit) ;
	}

	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(String mailStatus) {
		this.mailStatus = mailStatus;
	}

	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Timestamp getPayDate() {
		return payDate;
	}

	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}

	public Timestamp getDrawInvoiceDate() {
		return drawInvoiceDate;
	}

	public void setDrawInvoiceDate(Timestamp drawInvoiceDate) {
		this.drawInvoiceDate = drawInvoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getModifierUsername() {
		return modifierUsername;
	}

	public void setModifierUsername(String modifierUsername) {
		this.modifierUsername = modifierUsername;
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

	public Timestamp getAttributionDate() {
		return attributionDate;
	}

	public void setAttributionDate(Timestamp attributionDate) {
		this.attributionDate = attributionDate;
	}

	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getStandardCaseTeu() {
		return standardCaseTeu;
	}

	public void setStandardCaseTeu(String standardCaseTeu) {
		this.standardCaseTeu = standardCaseTeu;
	}

	public Integer getTorrNum() {
		return torrNum;
	}

	public void setTorrNum(Integer torrNum) {
		this.torrNum = torrNum;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getTgtCurrency() {
		return tgtCurrency;
	}

	public void setTgtCurrency(String tgtCurrency) {
		this.tgtCurrency = tgtCurrency;
	}

	public String getLinkUniueCode() {
		return linkUniueCode;
	}

	public void setLinkUniueCode(String linkUniueCode) {
		this.linkUniueCode = linkUniueCode;
	}

	public Timestamp getInboundDate() {
		return inboundDate;
	}

	public void setInboundDate(Timestamp inboundDate) {
		this.inboundDate = inboundDate;
	}

	public String getAmountAdjustmentStatus() {
		return amountAdjustmentStatus;
	}

	public void setAmountAdjustmentStatus(String amountAdjustmentStatus) {
		this.amountAdjustmentStatus = amountAdjustmentStatus;
	}

	public Double getPriceIncreaseRate() {
		return priceIncreaseRate;
	}

	public void setPriceIncreaseRate(Double priceIncreaseRate) {
		this.priceIncreaseRate = priceIncreaseRate;
	}

	public Long getAmountAdjustmenter() {
		return amountAdjustmenter;
	}

	public void setAmountAdjustmenter(Long amountAdjustmenter) {
		this.amountAdjustmenter = amountAdjustmenter;
	}

	public String getAmountAdjustmentName() {
		return amountAdjustmentName;
	}

	public void setAmountAdjustmentName(String amountAdjustmentName) {
		this.amountAdjustmentName = amountAdjustmentName;
	}

	public Timestamp getAmountAdjustmentDate() {
		return amountAdjustmentDate;
	}

	public void setAmountAdjustmentDate(Timestamp amountAdjustmentDate) {
		this.amountAdjustmentDate = amountAdjustmentDate;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierMainId() {
		return supplierMainId;
	}

	public void setSupplierMainId(String supplierMainId) {
		this.supplierMainId = supplierMainId;
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

	public String getAmountConfirmerName() {
		return amountConfirmerName;
	}

	public void setAmountConfirmerName(String amountConfirmerName) {
		this.amountConfirmerName = amountConfirmerName;
	}

	public Timestamp getAmountConfirmerDate() {
		return amountConfirmerDate;
	}

	public void setAmountConfirmerDate(Timestamp amountConfirmerDate) {
		this.amountConfirmerDate = amountConfirmerDate;
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

	public String getFinancingRemark() {
		return financingRemark;
	}

	public void setFinancingRemark(String financingRemark) {
		this.financingRemark = financingRemark;
	}

	public String getPaymentProvision() {
		return paymentProvision;
	}

	public void setPaymentProvision(String paymentProvision) {
		this.paymentProvision = paymentProvision;
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

	public Timestamp getEstimateDeliverDate() {
		return estimateDeliverDate;
	}

	public void setEstimateDeliverDate(Timestamp estimateDeliverDate) {
		this.estimateDeliverDate = estimateDeliverDate;
	}

	public String getPurchaseMethod() {
		return purchaseMethod;
	}

	public void setPurchaseMethod(String purchaseMethod) {
		this.purchaseMethod = purchaseMethod;
	}

	public String getAuditMethod() {
		return auditMethod;
	}

	public void setAuditMethod(String auditMethod) {
		this.auditMethod = auditMethod;
	}

	public String getTryApplyCode() {
		return tryApplyCode;
	}

	public void setTryApplyCode(String tryApplyCode) {
		this.tryApplyCode = tryApplyCode;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
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
