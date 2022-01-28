package com.topideal.order.webapi.purchase.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

/**
 * 采购订单
 *
 * @author gy
 */
@ApiModel
public class PurchaseOrderAddForm {

	@ApiModelProperty(value="令牌", required=true)
	private String token ;
	// 供应商名称
	@ApiModelProperty(value="供应商名称", required=false)
	private String supplierName;
	// 仓库名称
	@ApiModelProperty(value="仓库名称", required=false)
	private String depotName;
	// 采购订单编号
	@ApiModelProperty(value="采购订单编号", required=false)
	private String code;
	// 供应商ID
	@ApiModelProperty(value="供应商ID", required=false)
	private Long supplierId;
	// 付款主体 1 卓普信 2 商家
	//@ApiModelProperty(value="付款主体 1 卓普信 2 商家", required=false)
	//private String paySubject;
	// 业务模式 1 采购 2 代销
	@ApiModelProperty(value="业务模式 1 采购 2 代销", required=false)
	private String businessModel;
	// 仓库ID
	@ApiModelProperty(value="仓库ID", required=false)
	private Long depotId;
	// 商家名称
	@ApiModelProperty(value="商家名称", required=false)
	private String merchantName;
	// PO号
	@ApiModelProperty(value="PO号", required=false)
	private String poNo;
	// 商家ID
	@ApiModelProperty(value="商家ID", required=false)
	private Long merchantId;
	// id
	@ApiModelProperty(value="采购订单id", required=false)
	private Long id;
	// 状态 001,待审核,003,已审核,006,已删除,007,已完结
	@ApiModelProperty(value="状态 001,待审核,003,已审核,006,已删除,007,已完结", required=false)
	private String status;
	// 融资申请号
	@ApiModelProperty(value="融资申请号", required=false)
	private String financingNo;
	// 来源
	@ApiModelProperty(value="来源", required=false)
	private String source;
	// 汇率
	@ApiModelProperty(value="汇率", required=false)
	private Double rate;
	// 是否已生成预申报单(1是,0否)
	@ApiModelProperty(value="是否已生成预申报单(1是,0否)", required=false)
	private String isGenerate;
	// LBX单号
	//@ApiModelProperty(value="LBX单号", required=false)
	//private String lbxNo;
	// 是否完结(1-是，0-否)
	@ApiModelProperty(value="是否完结(1-是，0-否)", required=false)
	private String isEnd;
	// 币种
	@ApiModelProperty(value="币种", required=false)
	private String currency;
	// 卓志编码
	@ApiModelProperty(value="卓志编码", required=false)
	private String topidealCode;
    //理货单位 00-托盘 01-箱 02-件
	@ApiModelProperty(value="理货单位 00-托盘 01-箱 02-件", required=false)
    private String tallyingUnit;
    //开票人
	@ApiModelProperty(value="开票人", required=false)
    private String invoiceName;
    //付款人
	@ApiModelProperty(value="付款人", required=false)
    private String payName;
    //发送邮件状态(0 未发送邮件 1,发送邮件1次  2 发送邮件2次)
	@ApiModelProperty(value="发送邮件状态(0 未发送邮件 1,发送邮件1次  2 发送邮件2次)", required=false)
    private String mailStatus;
    //收到发票时间字符串
	@ApiModelProperty(value="收到发票时间字符串，格式'yyyy-MM-dd'", required=false)
    private String invoiceDateStr;
    //付款日期
	@ApiModelProperty(value="付款日期字符串，格式'yyyy-MM-dd'", required=false)
    private String payDateStr;
    //开发票时间
	@ApiModelProperty(value="开发票时间字符串，格式'yyyy-MM-dd'", required=false)
    private String drawInvoiceDateStr;
    //单据状态(1.收到发票,2. 已付款)
	@ApiModelProperty(value="单据状态(1.收到发票,2. 已付款)", required=false)
    private String billStatus;
    //运输方式  1 空运 2 海运
	//@ApiModelProperty(value="运输方式  1 空运 2 海运", required=false)
    //private String transport;
    //发票号
	@ApiModelProperty(value="发票号", required=false)
    private String invoiceNo;
	//表体明细字符串
	@ApiModelProperty(value="表体明细JSON字符串", required=false)
	private String items;

	/**
	 * 事业部名称
	 */
	@ApiModelProperty(value="事业部名称", required=false)
	private String buName;
	/**
	 *  事业部id
	 */
	@ApiModelProperty(value="事业部id", required=false)
	private Long buId;

	//归属时间
	@ApiModelProperty(value="归属时间字符串，格式'yyyy-MM-dd'", required=false)
	private String attributionDateStr;
	//本位币
	@ApiModelProperty(value="本位币", required=false)
	private String tgtCurrency;
	//货权切割日
	@ApiModelProperty(value="货权切割日字符串，'yyyy-MM-dd'", required=false)
	private Timestamp cargoCuttingDate;
	//采购链路生成标识
	@ApiModelProperty(value="采购链路生成标识", required=false)
	private String linkUniueCode;
	//金额调整状态1-已调整 0-未调整
	@ApiModelProperty(value="金额调整状态1-已调整 0-未调整", required=false)
	private String amountAdjustmentStatus;
	// 加价比例
	@ApiModelProperty(value="加价比例", required=false)
	private Double priceIncreaseRate;
	/**
	 * 金额确认状态1-已确认 0-未确认
	 */
	@ApiModelProperty(value="金额确认状态1-已确认 0-未确认", required=false)
	private String amountConfirmStatus;

	@ApiModelProperty(value="旧LBX号，编辑是必传", required=false)
	private String oldLbxNo;

	@ApiModelProperty(value="旧合同号，编辑是必传", required=false)
	private String oldContractNo;

	/**
	 * 结算条款 1-先款后货—全款预付 2-先款后货—分期付款（预付30%-50%，理货后付余款） 3-先货后款—账期结算（收货后N天结算） 4.实销实结
	 */
	@ApiModelProperty(value = "结算条款 1-先款后货—全款预付 2-先款后货—分期付款（预付30%-50%，理货后付余款） 3-先货后款—账期结算（收货后N天结算） 4.实销实结", required = false)
	private String paymentProvision;
	/**
	 * 收货方式 1- CIF 2-CFR 3-FOB 4-DAP 5-EXW 6-CIP 7-FCA 8-其他
	 */
	@ApiModelProperty(value = "交货方式 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW", required = false)
	private String tradeTerms;

	/**
	 * 框架合同号
	 */
	@ApiModelProperty(value = "框架合同号", required = false)
	private String frameContractNo;
	/**
	 * 审批单号
	 */
	@ApiModelProperty(value = "审批单号", required = false)
	private String approvalNo;

	/**
	 * 预计付款日期
	 */
	@ApiModelProperty(value = "预计付款日期字符串，格式'yyyy-MM-dd'")
	private String paymentDateStr;


	//审批方式 1-OA审批 2-经分销审批
	private String auditMethod;
	//预计发货时间
	private String estimateDeliverDateStr;
	//采购方式 0-以销定采 1-备货(已立项) 2-备货（集团自主） 3-试单
	private String purchaseMethod;
	//采购试单申请编号
	private String tryApplyCode;

	@ApiModelProperty(value = "库位类型Id")
	private Long  stockLocationTypeId;

	public String getPaymentDateStr() {
		return paymentDateStr;
	}

	public void setPaymentDateStr(String paymentDateStr) {
		this.paymentDateStr = paymentDateStr;
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

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	public String getBusinessModel() {
		return businessModel;
	}
	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFinancingNo() {
		return financingNo;
	}
	public void setFinancingNo(String financingNo) {
		this.financingNo = financingNo;
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
	public String getIsGenerate() {
		return isGenerate;
	}
	public void setIsGenerate(String isGenerate) {
		this.isGenerate = isGenerate;
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
	public String getInvoiceDateStr() {
		return invoiceDateStr;
	}
	public void setInvoiceDateStr(String invoiceDateStr) {
		this.invoiceDateStr = invoiceDateStr;
	}
	public String getPayDateStr() {
		return payDateStr;
	}
	public void setPayDateStr(String payDateStr) {
		this.payDateStr = payDateStr;
	}
	public String getDrawInvoiceDateStr() {
		return drawInvoiceDateStr;
	}
	public void setDrawInvoiceDateStr(String drawInvoiceDateStr) {
		this.drawInvoiceDateStr = drawInvoiceDateStr;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
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
	public String getAttributionDateStr() {
		return attributionDateStr;
	}
	public void setAttributionDateStr(String attributionDateStr) {
		this.attributionDateStr = attributionDateStr;
	}
	public String getTgtCurrency() {
		return tgtCurrency;
	}
	public void setTgtCurrency(String tgtCurrency) {
		this.tgtCurrency = tgtCurrency;
	}
	public Timestamp getCargoCuttingDate() {
		return cargoCuttingDate;
	}
	public void setCargoCuttingDate(Timestamp cargoCuttingDate) {
		this.cargoCuttingDate = cargoCuttingDate;
	}
	public String getLinkUniueCode() {
		return linkUniueCode;
	}
	public void setLinkUniueCode(String linkUniueCode) {
		this.linkUniueCode = linkUniueCode;
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
	public String getAmountConfirmStatus() {
		return amountConfirmStatus;
	}
	public void setAmountConfirmStatus(String amountConfirmStatus) {
		this.amountConfirmStatus = amountConfirmStatus;
	}
	public String getOldLbxNo() {
		return oldLbxNo;
	}
	public void setOldLbxNo(String oldLbxNo) {
		this.oldLbxNo = oldLbxNo;
	}
	public String getOldContractNo() {
		return oldContractNo;
	}
	public void setOldContractNo(String oldContractNo) {
		this.oldContractNo = oldContractNo;
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

	public String getAuditMethod() {
		return auditMethod;
	}

	public void setAuditMethod(String auditMethod) {
		this.auditMethod = auditMethod;
	}



	public String getEstimateDeliverDateStr() {
		return estimateDeliverDateStr;
	}

	public void setEstimateDeliverDateStr(String estimateDeliverDateStr) {
		this.estimateDeliverDateStr = estimateDeliverDateStr;
	}

	public String getPurchaseMethod() {
		return purchaseMethod;
	}

	public void setPurchaseMethod(String purchaseMethod) {
		this.purchaseMethod = purchaseMethod;
	}

	public String getTryApplyCode() {
		return tryApplyCode;
	}

	public void setTryApplyCode(String tryApplyCode) {
		this.tryApplyCode = tryApplyCode;
	}

	public Long getStockLocationTypeId() {
		return stockLocationTypeId;
	}

	public void setStockLocationTypeId(Long stockLocationTypeId) {
		this.stockLocationTypeId = stockLocationTypeId;
	}
}
