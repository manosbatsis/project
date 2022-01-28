package com.topideal.entity.dto.purchase;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class PurchaseOrderDTO extends PageModel implements Serializable{

	// 供应商名称
	@ApiModelProperty(value = "供应商名称", required = false)
	private String supplierName;
	// 仓库名称
	@ApiModelProperty(value = "仓库名称", required = false)
	private String depotName;
	// 采购订单编号
	@ApiModelProperty(value = "采购订单编号", required = false)
	private String code;
	// 采购订单编号
	@ApiModelProperty(value = "多个采购订单编号，以‘,’隔开", required = false)
	private String codes;
	// 供应商ID
	@ApiModelProperty(value = "供应商ID", required = false)
	private Long supplierId;
	// 付款主体 1 卓普信 2 商家
	@ApiModelProperty(value = "付款主体 1 卓普信 2 商家", required = false)
	private String paySubject;
	// 付款主体中文
	@ApiModelProperty(value = "付款主体中文", required = false)
	private String paySubjectLabel;
	// 修改时间
	@ApiModelProperty(value = "修改时间", required = false)
	private Timestamp modifyDate;
	// 合同号
	@ApiModelProperty(value = "合同号", required = false)
	private String contractNo;
	// 业务模式 1 采购 2 代销
	@ApiModelProperty(value = "业务模式 1 采购 2 代销", required = false)
	private String businessModel;
	// 业务模式中文
	@ApiModelProperty(value = "业务模式中文", required = false)
	private String businessModelLabel ;
	// 修改人
	@ApiModelProperty(value = "修改人", required = false)
	private Long modifier;
	// 仓库ID
	@ApiModelProperty(value = "仓库ID", required = false)
	private Long depotId;
	// 商家名称
	@ApiModelProperty(value = "商家名称", required = false)
	private String merchantName;
	// 预计入仓时间
	@ApiModelProperty(value = "预计入仓时间", required = false)
	private Timestamp arriveDepotDate;
	// PO号
	@ApiModelProperty(value = "PO号", required = false)
	private String poNo;
	@ApiModelProperty(value = "批量PO号，多个以','隔开")
	private String poNos;
	// 商家ID
	@ApiModelProperty(value = "商家ID", required = false)
	private Long merchantId;
	// 交货地点
	@ApiModelProperty(value = "交货地点", required = false)
	private String deliveryAddr;
	// 创建人
	@ApiModelProperty(value = "创建人", required = false)
	private Long creater;
	// id
	@ApiModelProperty(value = "采购订单ID", required = false)
	private Long id;
	@ApiModelProperty(value = "采购订单ID,逗号分割", required = false)
	private String ids;
	// 创建时间
	@ApiModelProperty(value = "创建时间", required = false)
	private Timestamp createDate;
	// 状态 001,待审核,003,已审核,006,已删除,007,已完结
	@ApiModelProperty(value = "状态 001,待审核,003,已审核,006,已删除,007,已完结", required = false)
	private String status;
	// 状态中文
	@ApiModelProperty(value = "状态中文", required = false)
	private String statusLabel;
	// 装船时间
	@ApiModelProperty(value = "装船时间", required = false)
	private Timestamp shipDate;
	// 装货港
	@ApiModelProperty(value = "装货港", required = false)
	private String loadPort;
	// 预计付款时间
	@ApiModelProperty(value = "预计付款时间", required = false)
	private Timestamp paymentDate;
	// 融资申请号
	@ApiModelProperty(value = "融资申请号", required = false)
	private String financingNo;
	// 备注
	@ApiModelProperty(value = "备注", required = false)
	private String remark;
	// 来源
	@ApiModelProperty(value = "来源", required = false)
	private String source;
	// 汇率
	@ApiModelProperty(value = "汇率", required = false)
	private Double rate;
	// 提单号
	@ApiModelProperty(value = "提单号", required = false)
	private String ladingBill;
	// 提单毛重
	@ApiModelProperty(value = "提单毛重", required = false)
	private Double grossWeight;
	// 卸货港
	@ApiModelProperty(value = "卸货港", required = false)
	private String unloadPort;
	// 是否已生成预申报单(1是,0否)
	@ApiModelProperty(value = "是否已生成预申报单(1是,0否)", required = false)
	private String isGenerate;
	// 是否已生成预申报单中文(1是,0否)
	@ApiModelProperty(value = "是否已生成预申报单中文(1是,0否)", required = false)
	private String isGenerateLabel;
	// 完结入库时间
	@ApiModelProperty(value = "完结入库时间", required = false)
	private Timestamp endDate;
	// LBX单号
	@ApiModelProperty(value = "LBX单号", required = false)
	private String lbxNo;
	// 审核人id
	@ApiModelProperty(value = "审核人id", required = false)
	private Long auditer;
	// 审核时间
	@ApiModelProperty(value = "审核时间", required = false)
	private Timestamp auditDate;
	// 审核人用户名
	@ApiModelProperty(value = "审核人用户名", required = false)
	private String auditName;
	// 创建人用户名
	@ApiModelProperty(value = "创建人用户名", required = false)
	private String createName;
	// 是否完结(1-是，0-否)
	@ApiModelProperty(value = "是否完结(1-是，0-否)", required = false)
	private String isEnd;
	@ApiModelProperty(value = "是否完结中文(1-是，0-否)", required = false)
	private String isEndLabel;
	// 币种
	@ApiModelProperty(value = "币种", required = false)
	private String currency;
	@ApiModelProperty(value = "币种中文", required = false)
	private String currencyLabel;
	// 卓志编码
	@ApiModelProperty(value = "卓志编码", required = false)
	private String topidealCode;
    //理货单位 00-托盘 01-箱 02-件
	@ApiModelProperty(value = "理货单位", required = false)
    private String tallyingUnit;
	@ApiModelProperty(value = "理货单位中文", required = false)
    private String tallyingUnitLabel;
    //开票人
	@ApiModelProperty(value = "开票人", required = false)
    private String invoiceName;
    //付款人
	@ApiModelProperty(value = "付款人", required = false)
    private String payName;
    //发送邮件状态(0 未发送邮件 1,发送邮件1次  2 发送邮件2次)
	@ApiModelProperty(value = "发送邮件状态(0 未发送邮件 1,发送邮件1次  2 发送邮件2次)", required = false)
    private String mailStatus;
	@ApiModelProperty(value = "发送邮件状态中文", required = false)
    private String mailStatusLabel;
    //收到发票时间
	@ApiModelProperty(value = "收到发票时间", required = false)
    private Timestamp invoiceDate;
    //付款日期
	@ApiModelProperty(value = "付款日期", required = false)
    private Timestamp payDate;
    //开发票时间
	@ApiModelProperty(value = "开发票时间", required = false)
    private Timestamp drawInvoiceDate;
    //单据状态(1.收到发票,2. 已付款)
	@ApiModelProperty(value = "单据状态(1.收到发票,2. 已付款)", required = false)
    private String billStatus;
	@ApiModelProperty(value = "单据状态中文(1.收到发票,2. 已付款)", required = false)
    private String billStatusLabel;
    //销售渠道 属性说明
	@ApiModelProperty(value = "销售渠道", required = false)
    private String channel;
    //运输方式  1 空运 2 海运
	@ApiModelProperty(value = "运输方式  1 空运 2 海运", required = false)
    private String transport;
	@ApiModelProperty(value = "运输方式中文  1 空运 2 海运", required = false)
    private String transportLabel;
	//箱数
	@ApiModelProperty(value = "箱数", required = false)
    private Integer boxNum;
    //目的港名称 属性说明
	@ApiModelProperty(value = "目的港名称", required = false)
    private String destinationPortName;
    //包装方式
	@ApiModelProperty(value = "包装方式", required = false)
    private String packType;
    //付款条约
	@ApiModelProperty(value = "付款条约", required = false)
    private String payRules;
    //二程提单号
	@ApiModelProperty(value = "二程提单号", required = false)
    private String blNo;
    //进出口口岸
	@ApiModelProperty(value = "进出口口岸", required = false)
    private String imExpPort;
    //境外发货人 属性说明
	@ApiModelProperty(value = "境外发货人", required = false)
    private String shipper;
   //唛头
	@ApiModelProperty(value = "唛头", required = false)
    private String mark;
    //预计到港时间 属性说明
	@ApiModelProperty(value = "预计到港时间", required = false)
    private Timestamp arriveDate;
    //目的地名称
	@ApiModelProperty(value = "目的地名称", required = false)
    private String destinationName;
    //发票号
	@ApiModelProperty(value = "发票号", required = false)
    private String invoiceNo;

	@ApiModelProperty(value = "采购商品明细", required = false)
	private List<PurchaseOrderItemModel> itemList;
	@ApiModelProperty(value = "修改人", required = false)
	private String modifierUsername;

	// 拓展字段
	// 入库单编码
	@ApiModelProperty(value = "入库单编码", required = false)
	private String warehouseCode;
	// 创建开始时间
	@ApiModelProperty(value = "创建开始时间", required = false)
	private String createStartDate;
	// 创建结束时间
	@ApiModelProperty(value = "创建结束时间", required = false)
	private String createEndDate;
	// code 的集合
	@ApiModelProperty(hidden = true)
    private List<String> codeList;
	// 供应商ID 的集合
	@ApiModelProperty(hidden = true)
	private List<Long> supplierIdList;
    
	// 采购商品的总金额
	@ApiModelProperty(value = "采购商品的总金额", required = false)
	private BigDecimal goodsAmount;
	
	/**
	 * 事业部名称
	 */
	@ApiModelProperty(value = "事业部名称", required = false)
	private String buName;
	/**
	 *  事业部id
	 */
	@ApiModelProperty(value = "事业部id", required = false)
	private Long buId;
	
	//页面查询用户事业部关联ID
	@ApiModelProperty(hidden=true)
	private List<Long> buIds ;

	//归属时间
	@ApiModelProperty(value = "归属时间", required = false)
	private Timestamp attributionDate;

	/**
	 * 车次
	 */
	@ApiModelProperty(value = "车次", required = false)
	private String trainNumber;
	/**
	 * 标准箱TEU
	 */
	@ApiModelProperty(value = "标准箱TEU", required = false)
	private String standardCaseTeu;
	/**
	 * 托数
	 */
	@ApiModelProperty(value = "托数", required = false)
	private Integer torrNum;
	/**
	 * 承运商
	 */
	@ApiModelProperty(value = "承运商", required = false)
	private String carrier;

	//本位币
	@ApiModelProperty(value = "本位币", required = false)
	private String tgtCurrency;
	
	//货权切割日
	@ApiModelProperty(value = "货权切割日", required = false)
	private Timestamp cargoCuttingDate;
	
	//显示总数
	@ApiModelProperty(hidden=true)
	private Integer totalNum ;

	//采购链路生成标识
	@ApiModelProperty(value = "采购链路生成标识", required = false)
	private String linkUniueCode;

	//金额调整状态1-已调整 0-未调整
	@ApiModelProperty(value = "金额调整状态1-已调整 0-未调整", required = false)
	private String amountAdjustmentStatus;
	@ApiModelProperty(value = "金额调整状态中文1-已调整 0-未调整", required = false)
	private String amountAdjustmentStatusLabel ;

	// 加价比例
	@ApiModelProperty(value = "加价比例", required = false)
	private Double priceIncreaseRate;
	
	//是否可以出库打托
	@ApiModelProperty(value = "是否可以入库打托", required = false)
    private String isInDepotAble ;
	//金额调整人
	@ApiModelProperty(value = "金额调整人", required = false)
	private Long amountAdjustmenter;
	//金额调整人名
	@ApiModelProperty(value = "金额调整人名", required = false)
	private String amountAdjustmentName;
	//金额调整时间
	@ApiModelProperty(value = "金额调整时间", required = false)
	private Timestamp amountAdjustmentDate;
	//是否可以金额调整
	@ApiModelProperty(value = "是否可以金额调整", required = false)
	private String isAmountAdjustmentAble ;
	/**
	 * 金额确认状态1-已确认 0-未确认
	 */
	@ApiModelProperty(value = "金额确认状态1-已确认 0-未确认", required = false)
	private String amountConfirmStatus;
	@ApiModelProperty(value = "金额确认状态中文1-已确认 0-未确认", required = false)
	private String amountConfirmStatusLabel;
	//是否可以金额确认
	@ApiModelProperty(value = "是否可以金额确认", required = false)
	private String isAmountConfirmAble ;
	/**
	 * 金额确认人
	 */
	@ApiModelProperty(value = "金额确认人", required = false)
	private Long amountConfirmer;
	/**
	 * 金额确认人名
	 */
	@ApiModelProperty(value = "金额确认人名", required = false)
	private String amountConfirmerName;
	/**
	 * 金额确认时间
	 */
	@ApiModelProperty(value = "金额确认时间", required = false)
	private Timestamp amountConfirmerDate;

	/**
	 * 提交人
	 */
	@ApiModelProperty(value = "提交人", required = false)
	private Long submiter;
	/**
	 * 提交人用户名
	 */
	@ApiModelProperty(value = "提交人用户名", required = false)
	private String submiterName;
	/**
	 * 提交时间
	 */
	@ApiModelProperty(value = "提交时间", required = false)
	private Timestamp submitDate;

	/**
	 * 入库关区id
	 */
	@ApiModelProperty(value = "入库关区id", required = false)
	private Long inCustomsId;
	/**
	 * 入库关区编码
	 */
	@ApiModelProperty(value = "入库关区编码", required = false)
	private String inCustomsCode;
	/**
	 * 入库关区名称
	 */
	@ApiModelProperty(value = "入库关区名称", required = false)
	private String inPlatformCustoms;

	//融资备注
	@ApiModelProperty(value = "融资备注", required = false)
	private String financingRemark;

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
	@ApiModelProperty(value = "贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW", required = false)
	private String tradeTerms;
	@ApiModelProperty(value = "贸易条款中文 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW", required = false)
	private String tradeTermsLabel;
	@ApiModelProperty(value = "是否可开发票标示 1-是 0-否", required = false)
	private String isInvoiceAble ;
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

	@ApiModelProperty(value = "入库月份", required = false)
	private String inBoundMonth;

	/**
	 * 红冲状态: 001-待红冲 002-红冲中 003-已红冲
	 */
	@ApiModelProperty(value = "红冲状态: 001-待红冲 002-红冲中 003-已红冲")
	private String writeOffStatus;
	@ApiModelProperty(value = "红冲状态中文")
	private String writeOffStatusLabel;

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
	@ApiModelProperty(value = "采购方式 0-以销定采 1-备货(已立项) 2-备货（集团自主） 3-试单")
	private String purchaseMethodLabel;
	
	//审批方式 1-OA审批 2-经分销审批
	@ApiModelProperty(value = "审批方式 1-OA审批 2-经分销审批")
	private String auditMethod;
	@ApiModelProperty(value = "审批方式 1-OA审批 2-经分销审批")
	private String auditMethodLabel;
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

	@ApiModelProperty(value = "SkU数量（个）")
	private Long skuNum;

	@ApiModelProperty(value = "采购总数量（件）")
	private Long purchaseTotalNum;

	@ApiModelProperty(value = "OA流程单号")
	private String requestId;

	/**
	 * 库位类型id
	 */
	@ApiModelProperty(value = "库位类型id")
	private Long stockLocationTypeId;
	/**
	 * 库位类型
	 */
	@ApiModelProperty(value = "库位类型")
	private String stockLocationTypeName;

	public Long getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(Long skuNum) {
		this.skuNum = skuNum;
	}

	public void setPurchaseTotalNum(Long purchaseTotalNum) {
		this.purchaseTotalNum = purchaseTotalNum;
	}

	public List<Long> getSupplierIdList() {
		return supplierIdList;
	}

	public void setSupplierIdList(List<Long> supplierIdList) {
		this.supplierIdList = supplierIdList;
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
	public List<PurchaseOrderItemModel> getItemList() {
		return itemList;
	}
	public void setItemList(List<PurchaseOrderItemModel> itemList) {
		this.itemList = itemList;
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
	public List<String> getCodeList() {
		return codeList;
	}
	public void setCodeList(List<String> codeList) {
		this.codeList = codeList;
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
	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public Timestamp getAttributionDate() {
		return attributionDate;
	}

	public void setAttributionDate(Timestamp attributionDate) {
		this.attributionDate = attributionDate;
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

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
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
		this.amountAdjustmentStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_amountAdjustmentStatusList, amountAdjustmentStatus) ;
	}

	public Double getPriceIncreaseRate() {
		return priceIncreaseRate;
	}

	public void setPriceIncreaseRate(Double priceIncreaseRate) {
		this.priceIncreaseRate = priceIncreaseRate;
	}

	public String getAmountAdjustmentStatusLabel() {
		return amountAdjustmentStatusLabel;
	}

	public void setAmountAdjustmentStatusLabel(String amountAdjustmentStatusLabel) {
		this.amountAdjustmentStatusLabel = amountAdjustmentStatusLabel;
	}

	public String getIsInDepotAble() {
		return isInDepotAble;
	}

	public void setIsInDepotAble(String isInDepotAble) {
		this.isInDepotAble = isInDepotAble;
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

	public String getIsAmountAdjustmentAble() {
		return isAmountAdjustmentAble;
	}

	public void setIsAmountAdjustmentAble(String isAmountAdjustmentAble) {
		this.isAmountAdjustmentAble = isAmountAdjustmentAble;
	}

	public String getAmountConfirmStatus() {
		return amountConfirmStatus;
	}

	public void setAmountConfirmStatus(String amountConfirmStatus) {
		this.amountConfirmStatus = amountConfirmStatus;
		this.amountConfirmStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_amountConfirmStatusList, amountConfirmStatus) ;
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

	public String getAmountConfirmStatusLabel() {
		return amountConfirmStatusLabel;
	}

	public void setAmountConfirmStatusLabel(String amountConfirmStatusLabel) {
		this.amountConfirmStatusLabel = amountConfirmStatusLabel;
	}

	public String getIsAmountConfirmAble() {
		return isAmountConfirmAble;
	}

	public void setIsAmountConfirmAble(String isAmountConfirmAble) {
		this.isAmountConfirmAble = isAmountConfirmAble;
	}

	public String getPoNos() {
		return poNos;
	}

	public void setPoNos(String poNos) {
		this.poNos = poNos;
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

	public String getIsInvoiceAble() {
		return isInvoiceAble;
	}

	public void setIsInvoiceAble(String isInvoiceAble) {
		this.isInvoiceAble = isInvoiceAble;
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

	public String getInBoundMonth() {
		return inBoundMonth;
	}

	public void setInBoundMonth(String inBoundMonth) {
		this.inBoundMonth = inBoundMonth;
	}

	public String getWriteOffStatus() {
		return writeOffStatus;
	}

	public void setWriteOffStatus(String writeOffStatus) {
		this.writeOffStatus = writeOffStatus;
		this.writeOffStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_writeOffStatusList, writeOffStatus);
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

	public String getAuditMethod() {
		return auditMethod;
	}

	public void setAuditMethod(String auditMethod) {
		this.auditMethod = auditMethod;
		this.auditMethodLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_auditMethodList, auditMethod);
	}

	public String getTryApplyCode() {
		return tryApplyCode;
	}

	public void setTryApplyCode(String tryApplyCode) {
		this.tryApplyCode = tryApplyCode;
	}

	public String getWriteOffStatusLabel() {
		return writeOffStatusLabel;
	}

	public void setWriteOffStatusLabel(String writeOffStatusLabel) {
		this.writeOffStatusLabel = writeOffStatusLabel;
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

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}


	public String getAuditMethodLabel() {
		return auditMethodLabel;
	}

	public void setAuditMethodLabel(String auditMethodLabel) {
		this.auditMethodLabel = auditMethodLabel;
	}

	public String getPurchaseMethodLabel() {
		return purchaseMethodLabel;
	}

	public void setPurchaseMethodLabel(String purchaseMethodLabel) {
		this.purchaseMethodLabel = purchaseMethodLabel;
	}

	public Long getPurchaseTotalNum() {
		return purchaseTotalNum;
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
