package com.topideal.entity.dto.purchase;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class PurchaseOrderExportDTO {

	// 供应商名称
	private String supplierName;
	// 仓库名称
	private String depotName;
	// 采购订单编号
	private String code;

	private String codes;
	// 供应商ID
	private Long supplierId;
	// 付款主体 1 卓普信 2 商家
	private String paySubject;
	// 付款主体中文
	private String paySubjectLabel;
	// 修改时间
	private Timestamp modifyDate;
	// 合同号
	private String contractNo;
	// 业务模式 1 采购 2 代销
	private String businessModel;
	// 业务模式中文
	private String businessModelLabel ;
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
	private String poNos;
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
	// 状态中文
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
	// 是否已生成预申报单中文(1是,0否)
	private String isGenerateLabel;
	// 完结入库时间
	private Timestamp endDate;
	//归属时间
	private Timestamp attributionDate;
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
	private String isEndLabel;
	// 币种
	private String currency;
	private String currencyLabel;
	// 卓志编码
	private String topidealCode;
    //理货单位 00-托盘 01-箱 02-件
    private String tallyingUnit;
    private String tallyingUnitLabel;
    //开票人
    private String invoiceName;
    //付款人
    private String payName;
    //发送邮件状态(0 未发送邮件 1,发送邮件1次  2 发送邮件2次)
    private String mailStatus;
    private String mailStatusLabel;
    //收到发票时间
    private Timestamp invoiceDate;
    //付款日期
    private Timestamp payDate;
    //开发票时间
    private Timestamp drawInvoiceDate;
    //单据状态(1.收到发票,2. 已付款)
    private String billStatus;
    private String billStatusLabel;
    //销售渠道 属性说明
    private String channel;
    //运输方式  1 空运 2 海运
    private String transport;
    private String transportLabel;
  //箱数
    private Integer boxNum;
    //目的港名称 属性说明
    private String destinationPortName;
    //包装方式
    private String packType;
    //付款条约
    private String payRules;
    //二程提单号
    private String blNo;
    //邮箱
    private String email;
    //进出口口岸
    private String imExpPort;
    //境外发货人 属性说明
    private String shipper;
   //唛头
    private String mark;
    //预计到港时间 属性说明
    private Timestamp arriveDate;
    //目的地名称
    private String destinationName;
    //发票号
    private String invoiceNo;

	private String modifierUsername;

	// 拓展字段
	// 入库单编码
	private String warehouseCode;
	// 创建开始时间
	private String createStartDate;
	// 创建结束时间
	private String createEndDate;
	//导出ID
	private String ids ;

	// 采购总金额
	private BigDecimal amount;

	// 是否内部公司
	private String isInnerMerchant ;

	/**
	 * 事业部名称
	 */
	private String buName;
	/**
	 *  事业部id
	 */
	private Long buId;

	//页面查询用户事业部关联ID
	private List<Long> buIds ;

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

	//货权切割日
	private Timestamp cargoCuttingDate;

	/**
	 * 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款
	 */
	@ApiModelProperty(value = "付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款", required = false)
	private String paymentProvision;
	@ApiModelProperty(value = "付款条款中文 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款", required = false)
	private String paymentProvisionLabel;
	/**
	 * 贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW
	 */
	@ApiModelProperty(value = "收货方式 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW", required = false)
	private String tradeTerms;
	@ApiModelProperty(value = "收货方式中文 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW", required = false)
	private String tradeTermsLabel;

	@ApiModelProperty(value = "框架合同号", required = false)
	private String frameContractNo;
	/**
	 * 审批单号
	 */
	@ApiModelProperty(value = "审批单号", required = false)
	private String approvalNo;

	/**
	 * 红冲状态: 001-待红冲 002-红冲中 003-已红冲
	 */
	@ApiModelProperty(value = "红冲状态: 001-待红冲 002-红冲中 003-已红冲")
	private String writeOffStatus;

	/**
	 * 红冲日期
	 */
	@ApiModelProperty(value = "红冲日期")
	private Timestamp writeOffDate;

	//预计发货时间
	@ApiModelProperty(value = "预计发货时间")
	private Timestamp estimateDeliverDate;
	//采购方式 0-以销定采 1-备货(已立项) 2-备货（集团自主） 3-试单
	@ApiModelProperty(value = "采购方式 0-以销定采 1-备货(已立项) 2-备货（集团自主） 3-试单")
	private String purchaseMethod;
	private String purchaseMethodLabel;
	//审批方式 1-OA审批 2-经分销审批
	@ApiModelProperty(value = "审批方式 1-OA审批 2-经分销审批")
	private String auditMethod;
	//采购试单申请编号
	@ApiModelProperty(value = "采购试单申请编号")
	private String tryApplyCode;

	@ApiModelProperty(value = "发票开始时间", required = false)
	private String invoiceStartDate;

	@ApiModelProperty(value = "发票结束时间", required = false)
	private String invoiceEndDate;

	@ApiModelProperty(value = "采购总金额", required = false)
	private BigDecimal totalAmount;

	@ApiModelProperty(value = "采购折算人民币金额", required = false)
	private BigDecimal totalRMBAmount;

	@ApiModelProperty(value = "OA流程单号")
	private String requestId;

	@ApiModelProperty(value = "库位类型id")
	private Long stockLocationTypeId;

	@ApiModelProperty(value = "库位类型")
	private String stockLocationTypeName;

	public String getPaymentProvisionLabel() {
		return paymentProvisionLabel;
	}

	public void setPaymentProvisionLabel(String paymentProvisionLabel) {
		this.paymentProvisionLabel = paymentProvisionLabel;
	}

	public String getTradeTermsLabel() {
		return tradeTermsLabel;
	}

	public void setTradeTermsLabel(String tradeTermsLabel) {
		this.tradeTermsLabel = tradeTermsLabel;
	}

	public String getFrameContractNo() {
		return frameContractNo;
	}

	public void setFrameContractNo(String frameContractNo) {
		this.frameContractNo = frameContractNo;
	}

	public String getApprovalNo() {
		return approvalNo;
	}

	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}

	public Timestamp getAttributionDate() {
		return attributionDate;
	}

	public void setAttributionDate(Timestamp attributionDate) {
		this.attributionDate = attributionDate;
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
		if(paySubject != null) {
			this.paySubjectLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_paySubjectList, paySubject);
		}
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
		if(businessModel != null) {
			this.businessModelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_businessModelList, businessModel);
		}
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
		if(status != null) {
			this.statusLabel = DERP.getLabelByKey(DERP_ORDER.purchaseOrder_statusList, status) ;
		}
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
		if(isGenerate != null) {
			this.isGenerateLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_isGenerateList, isGenerate) ;
		}
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
		if(isEnd != null) {
			this.isEndLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_isEndList, isEnd) ;
		}
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
		if(currency != null) {
			this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency) ;
		}
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
		if(tallyingUnit != null) {
			this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit) ;
		}

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
		if(mailStatus != null) {
			this.mailStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_mailStatusList, mailStatus);
		}
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
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
		if(billStatus != null) {
			this.billStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_billStatusList, billStatus) ;
		}
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getTransport() {
		return transport;
	}
	public void setTransport(String transport) {
		this.transport = transport;
		if(transport != null) {
			this.transportLabel = DERP_ORDER.getLabelByKey(DERP.transportList, transport) ;
		}
	}
	public Integer getBoxNum() {
		return boxNum;
	}
	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}
	public String getDestinationPortName() {
		return destinationPortName;
	}
	public void setDestinationPortName(String destinationPortName) {
		this.destinationPortName = destinationPortName;
	}
	public String getPackType() {
		return packType;
	}
	public void setPackType(String packType) {
		this.packType = packType;
	}
	public String getPayRules() {
		return payRules;
	}
	public void setPayRules(String payRules) {
		this.payRules = payRules;
	}
	public String getBlNo() {
		return blNo;
	}
	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImExpPort() {
		return imExpPort;
	}
	public void setImExpPort(String imExpPort) {
		this.imExpPort = imExpPort;
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
	public Timestamp getArriveDate() {
		return arriveDate;
	}
	public void setArriveDate(Timestamp arriveDate) {
		this.arriveDate = arriveDate;
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
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
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public String getCreateStartDate() {
		return createStartDate;
	}
	public void setCreateStartDate(String createStartDate) {
		this.createStartDate = createStartDate;
	}
	public String getCreateEndDate() {
		return createEndDate;
	}
	public void setCreateEndDate(String createEndDate) {
		this.createEndDate = createEndDate;
	}
	public String getPaySubjectLabel() {
		return paySubjectLabel;
	}
	public void setPaySubjectLabel(String paySubjectLabel) {
		this.paySubjectLabel = paySubjectLabel;
	}
	public String getBusinessModelLabel() {
		return businessModelLabel;
	}
	public void setBusinessModelLabel(String businessModelLabel) {
		this.businessModelLabel = businessModelLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getIsGenerateLabel() {
		return isGenerateLabel;
	}
	public void setIsGenerateLabel(String isGenerateLabel) {
		this.isGenerateLabel = isGenerateLabel;
	}
	public String getIsEndLabel() {
		return isEndLabel;
	}
	public void setIsEndLabel(String isEndLabel) {
		this.isEndLabel = isEndLabel;
	}
	public String getCurrencyLabel() {
		return currencyLabel;
	}
	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}
	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}
	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}
	public String getMailStatusLabel() {
		return mailStatusLabel;
	}
	public void setMailStatusLabel(String mailStatusLabel) {
		this.mailStatusLabel = mailStatusLabel;
	}
	public String getBillStatusLabel() {
		return billStatusLabel;
	}
	public void setBillStatusLabel(String billStatusLabel) {
		this.billStatusLabel = billStatusLabel;
	}
	public String getTransportLabel() {
		return transportLabel;
	}
	public void setTransportLabel(String transportLabel) {
		this.transportLabel = transportLabel;
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
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public List<Long> getBuIds() {
		return buIds;
	}
	public void setBuIds(List<Long> buIds) {
		this.buIds = buIds;
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
	public String getIsInnerMerchant() {
		return isInnerMerchant;
	}
	public void setIsInnerMerchant(String isInnerMerchant) {
		this.isInnerMerchant = isInnerMerchant;
	}
	public Timestamp getCargoCuttingDate() {
		return cargoCuttingDate;
	}
	public void setCargoCuttingDate(Timestamp cargoCuttingDate) {
		this.cargoCuttingDate = cargoCuttingDate;
	}
	public String getPoNos() {
		return poNos;
	}
	public void setPoNos(String poNos) {
		this.poNos = poNos;
	}

	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	public String getPaymentProvision() {
		return paymentProvision;
	}

	public void setPaymentProvision(String paymentProvision) {
		this.paymentProvision = paymentProvision;
		this.paymentProvisionLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_paymentProvisionList, paymentProvision) ;
	}

	public String getTradeTerms() {
		return tradeTerms;
	}

	public void setTradeTerms(String tradeTerms) {
		this.tradeTerms = tradeTerms;
		this.tradeTermsLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_tradeTermsList, tradeTerms) ;
	}

	public String getInvoiceStartDate() {
		return invoiceStartDate;
	}

	public void setInvoiceStartDate(String invoiceStartDate) {
		this.invoiceStartDate = invoiceStartDate;
	}

	public String getInvoiceEndDate() {
		return invoiceEndDate;
	}

	public void setInvoiceEndDate(String invoiceEndDate) {
		this.invoiceEndDate = invoiceEndDate;
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
		this.purchaseMethodLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_purchaseMethodList, purchaseMethod);
	}

	public String getPurchaseMethodLabel() {
		return purchaseMethodLabel;
	}

	public void setPurchaseMethodLabel(String purchaseMethodLabel) {
		this.purchaseMethodLabel = purchaseMethodLabel;
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

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalRMBAmount() {
		return totalRMBAmount;
	}

	public void setTotalRMBAmount(BigDecimal totalRMBAmount) {
		this.totalRMBAmount = totalRMBAmount;
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
