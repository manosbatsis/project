package com.topideal.entity.dto.sale;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
/**
 * 销售订单
 * @author wenyan
 *
 */
@ApiModel
public class SaleOrderDTO extends PageModel implements Serializable {

	@ApiModelProperty(value = "LBX单号")
	private String lbxNo;
	@ApiModelProperty(value = "销售订单编号")
	private String code;
	@ApiModelProperty(value = "修改时间")
	private Timestamp modifyDate;
	@ApiModelProperty(value = "调出仓库ID")
	private Long outDepotId;
	@ApiModelProperty(value = "业务模式 1 购销 2 代销")
	private String businessModel;
	@ApiModelProperty(value = "业务模式（中文）")
	private String businessModelLabel;
	@ApiModelProperty(value = "修改人")
	private Long modifier;
	@ApiModelProperty(value = "审核人")
	private Long auditor;
	@ApiModelProperty(value = "客户名称")
	private String customerName;
	@ApiModelProperty(value = "商家名称")
	private String merchantName;
	@ApiModelProperty(value = "参照订单")
	private String referToOrder;
	@ApiModelProperty(value = "调出仓库名称")
	private String outDepotName;
	@ApiModelProperty(value = "po单号")
	private String poNo;
	@ApiModelProperty(value = "商家ID")
	private Long merchantId;
	@ApiModelProperty(value = "客户ID(供应商)")
	private Long customerId;
	@ApiModelProperty(value = "创建人")
	private Long creater;
	@ApiModelProperty(value = "业务场景 账册内调拨 枚举")
	private String model;
	@ApiModelProperty(value = "业务场景（中文）")
	private String modelLabel;
	@ApiModelProperty(value = "ID")
	private Long id;
	@ApiModelProperty(value = "订单状态")
	private String state;
	@ApiModelProperty(value = "订单状态（中文）")
	private String stateLabel;
	@ApiModelProperty(value = "交货日期")
	private Timestamp deliveryDate;
	@ApiModelProperty(value = "创建时间")
	private Timestamp createDate;
	@ApiModelProperty(value = "审核时间")
	private Timestamp auditDate;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "销售订单总数量")
	private Integer totalNum;
	@ApiModelProperty(value = "销售订单总金额")
	private BigDecimal totalAmount;
	@ApiModelProperty(value = "入仓仓库id")
	private Long inDepotId;
	@ApiModelProperty(value = "入仓仓库")
	private String inDepotName;
	@ApiModelProperty(value = "审核人名称")
	private String auditName;
	@ApiModelProperty(value = "创建人名称")
	private String createName;
	@ApiModelProperty(value = "服务类型")
	private String serveTypes;
	@ApiModelProperty(value = "服务类型（中文）")
	private String serveTypesLabel;
	@ApiModelProperty(value = "合同号")
	private String contractNo;
	@ApiModelProperty(value = "申报地海关编码")
	private String customsNo;
	@ApiModelProperty(value = "申报地国检编码")
	private String inspectNo;
	@ApiModelProperty(value = "出仓仓库编码")
	private String outDepotCode;
	@ApiModelProperty(value = "入仓仓库编码")
	private String inDepotCode;
	@ApiModelProperty(value = "出库数量")
	private Integer outDepotNum;
	@ApiModelProperty(value = "卓志编码")
	private String topidealCode;
	@ApiModelProperty(value = "是否同关区（0-否，1-是）")
	private String isSameArea;
	@ApiModelProperty(value = "是否同关区（中文）")
	private String isSameAreaLabel;
	@ApiModelProperty(value = "完结时间")
	private Timestamp endDate;
	@ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件")
	private String tallyingUnit;
	@ApiModelProperty(value = "理货单位（中文）")
	private String tallyingUnitLabel;
	@ApiModelProperty(value = "国家")
	private String country;
	@ApiModelProperty(value = "目的地")
	private String destination;
	@ApiModelProperty(value = "目的地（中文）")
	private String destinationLabel;
	@ApiModelProperty(value = "收件人名称")
	private String receiverName;
	@ApiModelProperty(value = "收件人地址")
	private String receiverAddress;
	@ApiModelProperty(value = "收件人地址")
	private String mailMode;
	@ApiModelProperty(value = "收件人地址（中文）")
	private String mailModeLabel;
	@ApiModelProperty(value = "订单来源 1：人工录入；2：系统自动生成")
	private String orderSource;
	@ApiModelProperty(value = "订单来源（中文）")
	private String orderSourceLabel;
	@ApiModelProperty(value = "归属月份")
	private String ownMonth;
	@ApiModelProperty(value = "原销出仓仓库ID")
	private Long originalOutDepotId;
	@ApiModelProperty(value = "原销出仓仓库名字")
	private String originalOutDepotName;
	@ApiModelProperty(value = "原销出仓仓库code")
	private String originalOutDepotCode;

	@ApiModelProperty(value = "商品信息-表体")
	private List<SaleOrderItemDTO> itemList;

	@ApiModelProperty(value = "供应商ID")
	private Long supplierId;

	@ApiModelProperty(value = "供应商名称")
	private String supplierName;

	@ApiModelProperty(value = "付款主体")
	private String paySubject;

	@ApiModelProperty(value = "po单时间")
	private Timestamp poDate;

	@ApiModelProperty(value = "币种")
	private String currency;
	@ApiModelProperty(value = "币种（中文）")
	private String currencyLabel;
	@ApiModelProperty(value = "修改人用户名")
	private String modifierUsername;
	@ApiModelProperty(value = "code 的集合")
    private List<String> codeList;
	@ApiModelProperty(value = "订单日期时间开始")
	private String createDateStartDate;
	@ApiModelProperty(value = "订单日期时间结束")
	private String createDateEndDate;

	@ApiModelProperty(value = "签收时间")
	private Timestamp receiveDate;

	@ApiModelProperty(value = "签收人名称")
	private String receiveName;

	@ApiModelProperty(value = "签收人")
	private Long receiver;

	@ApiModelProperty(value = "事业部名称")
	private String buName;

	@ApiModelProperty(value = "事业部id")
	private Long buId;
	@ApiModelProperty(value = "销售出库单号")
	private String saleOutDepotCode;
	@ApiModelProperty(value = "单据标识  1-预售转销 2-非预售转销")
	private String orderType;
	@ApiModelProperty(value = "单据标识(中文)")
	private String orderTypeLabel;
	@ApiModelProperty(value = "关联预售单单号")
	private String preSaleOrderRelCode;
	@ApiModelProperty(value = "关联预售单id")
	private String preSaleOrderIds;
	@ApiModelProperty(value = "标识为内部销售单")
	private String ownSaleType;

	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;

	@ApiModelProperty(value = "提单毛重")
	private Double billWeight;

	@ApiModelProperty(value = "运输方式  海运、空运、陆运、港到仓拖车、中欧铁路、其他")
	private String transport;
	@ApiModelProperty(value = "运输方式（中文）")
	private String transportLabel;

	@ApiModelProperty(value = "标准箱TEU")
	private String teu;

	@ApiModelProperty(value = "车次")
	private String trainno;

	@ApiModelProperty(value = "承运商")
	private String carrier;

	@ApiModelProperty(value = "托数")
	private Integer torusNumber;

	@ApiModelProperty(value = "出库地点")
	private String outdepotAddr;
	@ApiModelProperty(value = "是否客户")
	private String isInnerCustomer;
	@ApiModelProperty(value = "采购链路生成标识")
	private String linkUniueCode;

	@ApiModelProperty(value = "金额调整状态")
    private String amountStatus;
    @ApiModelProperty(value = "金额调整状态（中文）")
	private String amountStatusLabel;
    @ApiModelProperty(value = "出仓仓库进出接口指令 1-是 0 - 否")
    private String outDepotIsInOutInstruction;
    @ApiModelProperty(value = "出库仓库是否批次强校验 1-是 0-否")
    private String outDepotBatchValidation;
	@ApiModelProperty(value = "调整人")
	private Long adjuster;
	@ApiModelProperty(value = "调整时间")
	private Timestamp adjustDate;
	@ApiModelProperty(value = "调整人用户名")
	private String adjusterUsername;

	@ApiModelProperty(value = "金额确认状态 0-未确认，1-确认通过，2-确认不通过")
	private String amountConfirmStatus;
	@ApiModelProperty(value = "金额确认状态（中文）")
	private String amountConfirmStatusLabel;

	@ApiModelProperty(value = "金额确认用户id")
	private Long amountConfirmer;

	@ApiModelProperty(value = "金额确认用户名称")
	private String amountConfirmUsername;

	@ApiModelProperty(value = "金额确认时间")
	private Timestamp amountConfirmDate;

	@ApiModelProperty(value = "是否已关账")
	private String hasFinanceClose;

	@ApiModelProperty(value = "单据状态集合")
	private String stateList;
	@ApiModelProperty(value = "上架日期时间开始")
	private String shelfDateStartDate;
	@ApiModelProperty(value = "上架日期时间结束")
	private String shelfDateEndDate;
	@ApiModelProperty(value = "销售订单id集合")
	private String ids;

	@ApiModelProperty(value = " 付款条约")
	private String payRules;

	@ApiModelProperty(value = "发票单号")
	private String invoiceNo;

	@ApiModelProperty(value = "卸货港")
	private String portDes;

	@ApiModelProperty(value = "包装")
	private String pack;

	@ApiModelProperty(value = "箱数")
	private Integer boxNum;

	@ApiModelProperty(value = "托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱")
	private String torrPackType;

	@ApiModelProperty(value = "出库关区id")
	private Long outCustomsId;

	@ApiModelProperty(value = "出库关区编码")
	private String outCustomsCode;

	@ApiModelProperty(value = "出库关区名称")
	private String outPlatformCustoms;

	@ApiModelProperty(value = "入库关区id")
	private Long inCustomsId;

	@ApiModelProperty(value = "入库关区编码")
	private String inCustomsCode;

	@ApiModelProperty(value = "入库关区名称")
	private String inPlatformCustoms;
	/**
	 * 提交人
	 */
	@ApiModelProperty(value = "提交人")
	private Long submiter;
	/**
	 * 提交人名称
	 */
	@ApiModelProperty(value = "提交人名称")
	private String submiterName;
	/**
	 * 提交时间
	 */
	@ApiModelProperty(value = "提交时间")
	private Timestamp submitDate;
	/**
	 * 融资状态 0-未申请，1-已申请，2-已赎回
	 */
	@ApiModelProperty(value = "融资状态 0-未申请，1-已申请，2-已赎回")
	private String financeStatus;

	@ApiModelProperty(value = "仓库类别(a-保税仓，b-备查仓，c-海外仓，d-中转仓,e-暂存区，f-销毁区）")
	private String outDepotType;

	@ApiModelProperty(value = "po号集合")
	private List<String> poNos;

	/**
    * 是否已生成预申报单 1-是,0-否
    */
	@ApiModelProperty(value = "是否已生成预申报单 1-是,0-否")
    private String isGenerateDeclare;

	/**
    * 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款
    */
	@ApiModelProperty(value = "付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款")
    private String paymentTerms;
	@ApiModelProperty(value = "付款条款(中文)")
    private String paymentTermsLabel;
    /**
    * 贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW
    */
	@ApiModelProperty(value = "贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW")
    private String tradeTerms;
	@ApiModelProperty(value = "贸易条款(中文)")
    private String tradeTermsLabel;

	@ApiModelProperty(value = "销售预申报单集合")
    private String saleDeclareCodes;

	@ApiModelProperty(value = "赊销单号")
    private String saleCreditCode;

	@ApiModelProperty(value = "是否可以上架")
    private Boolean isShelf;

	@ApiModelProperty(value = "上架量")
    private Integer shelfNum;

	@ApiModelProperty(value = "上架金额")
    private BigDecimal shelfAmount;

	@ApiModelProperty(value = "审批单号")
	private String approvalNo;

	@ApiModelProperty(value = "公司id集合")
	private List<Long> merchantIds;

	@ApiModelProperty(value = "开始时间")
	private String orderStartDate;

	@ApiModelProperty(value = "结束时间")
	private String orderEndDate;
	@ApiModelProperty(value = "平台采购订单号")
	private String platformPurchaseIds;

	@ApiModelProperty(value = "审核结果")
	private String auditResult;

	@ApiModelProperty(value = "红冲状态: 001-待红冲 002-红冲中 003-已红冲")
	private String writeOffStatus;
	@ApiModelProperty(value = "红冲状态(中文)")
	private String writeOffStatusLabel;

	@ApiModelProperty(value = "红冲日期")
	private Timestamp writeOffDate;

	/**
	 * 事业部库位类型ID
	 */
	@ApiModelProperty(value = "事业部库位类型ID")
	private Long stockLocationTypeId;

	/**
	 * 库位类型
	 */
	@ApiModelProperty(value = "库位类型")
	private String stockLocationTypeName;

	public String getApprovalNo() {
		return approvalNo;
	}

	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}

    public String getOwnSaleType() {
		return ownSaleType;
	}

	public void setOwnSaleType(String ownSaleType) {
		this.ownSaleType = ownSaleType;
	}

	public String getPreSaleOrderIds() {
		return preSaleOrderIds;
	}

	public void setPreSaleOrderIds(String preSaleOrderIds) {
		this.preSaleOrderIds = preSaleOrderIds;
	}

	public String getOrderTypeLabel() {
		return orderTypeLabel;
	}

	public void setOrderTypeLabel(String orderTypeLabel) {
		this.orderTypeLabel = orderTypeLabel;
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
		if(StringUtils.isNotBlank(orderSource)){
			this.orderSourceLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_orderSourceList, orderSource);
		}
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
		if(StringUtils.isNotBlank(destination)){
			this.destinationLabel=DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_destinationList, destination);
		}
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
		if(StringUtils.isNotBlank(mailMode)){
			this.mailModeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_mailModeList, mailMode);
		}
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
		if(StringUtils.isNotBlank(tallyingUnit)){
			this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
		}
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
		if(StringUtils.isNotBlank(isSameArea)){
			this.isSameAreaLabel = DERP.getLabelByKey(DERP.isSameAreaList, isSameArea);
		}
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
		if(StringUtils.isNotBlank(serveTypes)){
			this.serveTypesLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_serveTypesList, serveTypes);
		}
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
		if(StringUtils.isNotBlank(businessModel)){
			this.businessModelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_businessModelList, businessModel);
		}
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
		if(StringUtils.isNotBlank(state)){
			this.stateLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_stateList, state);
		}
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

	public List<SaleOrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleOrderItemDTO> itemList) {
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
		if(StringUtils.isNotBlank(currency)){
			this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
		}
	}

	public String getModifierUsername() {
		return modifierUsername;
	}

	public void setModifierUsername(String modifierUsername) {
		this.modifierUsername = modifierUsername;
	}

	public String getBusinessModelLabel() {
		return businessModelLabel;
	}

	public void setBusinessModelLabel(String businessModelLabel) {
		this.businessModelLabel = businessModelLabel;
	}

	public String getModelLabel() {
		return modelLabel;
	}

	public void setModelLabel(String modelLabel) {
		this.modelLabel = modelLabel;
	}

	public String getServeTypesLabel() {
		return serveTypesLabel;
	}

	public void setServeTypesLabel(String serveTypesLabel) {
		this.serveTypesLabel = serveTypesLabel;
	}

	public String getIsSameAreaLabel() {
		return isSameAreaLabel;
	}

	public void setIsSameAreaLabel(String isSameAreaLabel) {
		this.isSameAreaLabel = isSameAreaLabel;
	}

	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}

	public String getOrderSourceLabel() {
		return orderSourceLabel;
	}

	public void setOrderSourceLabel(String orderSourceLabel) {
		this.orderSourceLabel = orderSourceLabel;
	}
	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}

	public String getStateLabel() {
		return stateLabel;
	}

	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}

	public String getDestinationLabel() {
		return destinationLabel;
	}

	public void setDestinationLabel(String destinationLabel) {
		this.destinationLabel = destinationLabel;
	}

	public String getMailModeLabel() {
		return mailModeLabel;
	}

	public void setMailModeLabel(String mailModeLabel) {
		this.mailModeLabel = mailModeLabel;
	}

	public List<String> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<String> codeList) {
		this.codeList = codeList;
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

	public String getSaleOutDepotCode() {
		return saleOutDepotCode;
	}

	public void setSaleOutDepotCode(String saleOutDepotCode) {
		this.saleOutDepotCode = saleOutDepotCode;
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
		if(StringUtils.isNotBlank(orderType)){
			this.orderTypeLabel = DERP.getLabelByKey(DERP_ORDER.saleOrder_orderTypeList, orderType);
		}
	}

	public String getPreSaleOrderRelCode() {
		return preSaleOrderRelCode;
	}

	public void setPreSaleOrderRelCode(String preSaleOrderRelCode) {
		this.preSaleOrderRelCode = preSaleOrderRelCode;
	}
	public List<Long> getBuList() {
		return buList;
	}

	public void setBuList(List<Long> buList) {
		this.buList = buList;
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
		if(StringUtils.isNotBlank(transport)){
			this.transportLabel = DERP.getLabelByKey(DERP.transportList, transport);
		}
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

	public String getTransportLabel() {
		return transportLabel;
	}

	public void setTransportLabel(String transportLabel) {
		this.transportLabel = transportLabel;
	}

	public String getIsInnerCustomer() {
		return isInnerCustomer;
	}

	public void setIsInnerCustomer(String isInnerCustomer) {
		this.isInnerCustomer = isInnerCustomer;
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
		if(StringUtils.isNotBlank(amountStatus)){
			this.amountStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_amountStatusList, amountStatus);
		}
	}

	public String getOutDepotIsInOutInstruction() {
		return outDepotIsInOutInstruction;
	}

	public void setOutDepotIsInOutInstruction(String outDepotIsInOutInstruction) {
		this.outDepotIsInOutInstruction = outDepotIsInOutInstruction;
	}

	public String getOutDepotBatchValidation() {
		return outDepotBatchValidation;
	}

	public void setOutDepotBatchValidation(String outDepotBatchValidation) {
		this.outDepotBatchValidation = outDepotBatchValidation;
	}

	public String getAmountStatusLabel() {
		return amountStatusLabel;
	}

	public void setAmountStatusLabel(String amountStatusLabel) {
		this.amountStatusLabel = amountStatusLabel;
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
		this.amountConfirmStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_amountConfirmStatusList, amountConfirmStatus);
	}

	public String getAmountConfirmStatusLabel() {
		return amountConfirmStatusLabel;
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
	public String getHasFinanceClose() {
		return hasFinanceClose;
	}

	public void setHasFinanceClose(String hasFinanceClose) {
		this.hasFinanceClose = hasFinanceClose;
	}

	public String getStateList() {
		return stateList;
	}

	public void setStateList(String stateList) {
		this.stateList = stateList;
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

	public void setAmountConfirmStatusLabel(String amountConfirmStatusLabel) {
		this.amountConfirmStatusLabel = amountConfirmStatusLabel;
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

	public String getOutDepotType() {
		return outDepotType;
	}

	public void setOutDepotType(String outDepotType) {
		this.outDepotType = outDepotType;
	}

	public List<String> getPoNos() {
		return poNos;
	}

	public void setPoNos(List<String> poNos) {
		this.poNos = poNos;
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
		if(StringUtils.isNotBlank(paymentTerms)) {
			this.paymentTermsLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_paymentTermsList, paymentTerms);
		}
	}

	public String getPaymentTermsLabel() {
		return paymentTermsLabel;
	}

	public String getTradeTerms() {
		return tradeTerms;
	}

	public void setTradeTerms(String tradeTerms) {
		this.tradeTerms = tradeTerms;
		if(StringUtils.isNotBlank(tradeTerms)) {
			this.tradeTermsLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_tradeTermsList, tradeTerms);
		}
	}

	public String getTradeTermsLabel() {
		return tradeTermsLabel;
	}

	public String getSaleDeclareCodes() {
		return saleDeclareCodes;
	}

	public void setSaleDeclareCodes(String saleDeclareCodes) {
		this.saleDeclareCodes = saleDeclareCodes;
	}

	public String getSaleCreditCode() {
		return saleCreditCode;
	}

	public void setSaleCreditCode(String saleCreditCode) {
		this.saleCreditCode = saleCreditCode;
	}

	public Boolean getIsShelf() {
		return isShelf;
	}

	public void setIsShelf(Boolean isShelf) {
		this.isShelf = isShelf;
	}

	public Integer getShelfNum() {
		return shelfNum;
	}

	public void setShelfNum(Integer shelfNum) {
		this.shelfNum = shelfNum;
	}

	public BigDecimal getShelfAmount() {
		return shelfAmount;
	}

	public void setShelfAmount(BigDecimal shelfAmount) {
		this.shelfAmount = shelfAmount;
	}

	public List<Long> getMerchantIds() {
		return merchantIds;
	}

	public void setMerchantIds(List<Long> merchantIds) {
		this.merchantIds = merchantIds;
	}

	public String getOrderStartDate() {
		return orderStartDate;
	}

	public void setOrderStartDate(String orderStartDate) {
		this.orderStartDate = orderStartDate;
	}

	public String getOrderEndDate() {
		return orderEndDate;
	}

	public void setOrderEndDate(String orderEndDate) {
		this.orderEndDate = orderEndDate;
	}

	public String getPlatformPurchaseIds() {
		return platformPurchaseIds;
	}

	public void setPlatformPurchaseIds(String platformPurchaseIds) {
		this.platformPurchaseIds = platformPurchaseIds;
	}

	public String getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}

	public Boolean getShelf() {
		return isShelf;
	}

	public void setShelf(Boolean shelf) {
		isShelf = shelf;
	}

	public String getWriteOffStatus() {
		return writeOffStatus;
	}

	public void setWriteOffStatus(String writeOffStatus) {
		this.writeOffStatus = writeOffStatus;
		if(StringUtils.isNotBlank(writeOffStatus)){
			this.writeOffStatusLabel =  DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_writeOffStatusList, writeOffStatus);
		}
	}

	public Timestamp getWriteOffDate() {
		return writeOffDate;
	}

	public void setWriteOffDate(Timestamp writeOffDate) {
		this.writeOffDate = writeOffDate;
	}

	public String getWriteOffStatusLabel() {
		return writeOffStatusLabel;
	}

	public void setPaymentTermsLabel(String paymentTermsLabel) {
		this.paymentTermsLabel = paymentTermsLabel;
	}

	public void setTradeTermsLabel(String tradeTermsLabel) {
		this.tradeTermsLabel = tradeTermsLabel;
	}

	public void setWriteOffStatusLabel(String writeOffStatusLabel) {
		this.writeOffStatusLabel = writeOffStatusLabel;
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

