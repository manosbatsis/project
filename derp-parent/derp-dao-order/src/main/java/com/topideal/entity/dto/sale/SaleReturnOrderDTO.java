package com.topideal.entity.dto.sale;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 销售退货订单
 * @author wenyan
 *
 */
@ApiModel
public class SaleReturnOrderDTO extends PageModel implements Serializable {

	@ApiModelProperty(value = "销售退货订单号")
	private String code;
	@ApiModelProperty(value = "合同号")
	private String contractNo;
	@ApiModelProperty(value = "退出仓库id")
	private Long outDepotId;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "申报地海关编码")
	private String customsNo;
	@ApiModelProperty(value = "商家名称")
	private String merchantName;
	@ApiModelProperty(value = "商家ID")
	private Long merchantId;
	@ApiModelProperty(value = "客户id")
	private Long customerId;
	@ApiModelProperty(value = "业务场景 账册内货权转移/账册内货权转移调仓")
	private String model;
	@ApiModelProperty(value = "业务场景（中文）")
	private String modelLabel;
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "创建时间")
	private Timestamp createDate;
	@ApiModelProperty(value = "审核时间")
	private Timestamp auditDate;
	@ApiModelProperty(value = "审核人")
	private Long auditor;
	@ApiModelProperty(value = "申报地国检编码")
	private String inspectNo;
	@ApiModelProperty(value = "客户名称")
	private String customerName;
	@ApiModelProperty(value = "退入仓库名称")
	private String inDepotName;
	@ApiModelProperty(value = "退出仓库名称")
	private String outDepotName;

	@ApiModelProperty(value = "企业退运单号")
	private String merchantReturnNo;
	@ApiModelProperty(value = "删除时间")
	private Timestamp deletedDate;
	@ApiModelProperty(value = "计划退货数量")
	private Integer totalReturnNum;
	@ApiModelProperty(value = "退入仓库id")
	private Long inDepotId;
	@ApiModelProperty(value = "创建人")
	private Long creater;
	@ApiModelProperty(value = "服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务")
	private String serveTypes;
	@ApiModelProperty(value = "服务类型（中文）")
	private String serveTypesLabel;
	@ApiModelProperty(value = "状态：0-待审核 1-已审核 2-已删除")
	private String status;
	@ApiModelProperty(value = "状态（中文）")
	private String statusLabel;
	@ApiModelProperty(value = "发票号")
	private String invoiceNo;
	@ApiModelProperty(value = "收货地址")
	private String deliveryAddr;
	@ApiModelProperty(value = "装货港")
	private String portLoading;
	@ApiModelProperty(value = "包装方式")
	private String packType;
	@ApiModelProperty(value = "包装方式（中文）")
	private String packTypeLabel;
	@ApiModelProperty(value = "二程提单号")
	private String blNo;
	@ApiModelProperty(value = "箱数")
	private Integer boxNum;
	@ApiModelProperty(value = "付款条约")
	private String payRules;
	@ApiModelProperty(value = "提单毛重")
	private Double billWeight;
	@ApiModelProperty(value = "头程提单号")
	private String ladingBill;
	@ApiModelProperty(value = "卸货港编码")
	private String portDestNo;
	@ApiModelProperty(value = "邮箱地址")
	private String email;
	@ApiModelProperty(value = "LBX单号")
	private String lbxNo;
	@ApiModelProperty(value = "审核人名称")
	private String auditName;
	@ApiModelProperty(value = "创建人名称")
	private String createName;
	@ApiModelProperty(value = "入仓仓库编码")
	private String inDepotCode;
	@ApiModelProperty(value = "出仓仓库编码")
	private String outDepotCode;
	@ApiModelProperty(value = "卓志编码")
	private String topidealCode;
	@ApiModelProperty(value = "境外发货人")
	private String shipper;
	@ApiModelProperty(value = "唛头")
	private String mark;
	@ApiModelProperty(value = "修改时间")
	private Timestamp modifyDate;
	@ApiModelProperty(value = "是否同关区（0-否，1-是）")
	private String isSameArea;
	@ApiModelProperty(value = "是否同关区（中文）")
	private String isSameAreaLabel;
	@ApiModelProperty(value = "销售退货类型 1-消费者退货 2-代销退货")
	private String returnType;
	@ApiModelProperty(value = "销售退货类型（中文）")
	private String returnTypeLabel;

	//------扩展字段-----------------------
	@ApiModelProperty(value = "销售单号（用于列表查询）")
	private String saleOrderCode;
	@ApiModelProperty(value = "云集结算账单号")
	private String yunjiAccountNo;
	@ApiModelProperty(value = "币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元  07 英镑")
	private String currency;
	@ApiModelProperty(value = "币种(中文)")
	private String currencyLabel;
	@ApiModelProperty(value = "海外仓理货单位 00-托盘 01-箱 02-件")
	private String tallyingUnit;
	@ApiModelProperty(value = "海外仓理货单位（中文）")
	private String tallyingUnitLabel;
	@ApiModelProperty(value = "关联销售单单号")
	private String saleOrderRelCode;
	@ApiModelProperty(value = "创建开始日期",hidden=true)
	private String createStartDate;
	@ApiModelProperty(value = "创建结束日期",hidden=true)
	private String createEndDate;

	@ApiModelProperty(value = "事业部名称")
	private String buName;

	@ApiModelProperty(value = "事业部id")
	private Long buId;

	@ApiModelProperty(value = "表体信息")
	private List<SaleReturnOrderItemDTO> itemList;

	@ApiModelProperty(value = "出仓仓库进出接口指令 1-是 0 - 否")
    private String outDepotIsInOutInstruction;

	@ApiModelProperty(value = "出库仓库是否已入定出 1-是 0-否")
    private String outDepotIsInDependOut;

    @ApiModelProperty(value = "'出库仓批次效期强校验：1-是 0-否'")
    private String outBatchValidation;

    @ApiModelProperty(value = "表体入库仓库是否已出定入 1-是 0-否")
    private String inDepotisOutDependIn;

    @ApiModelProperty(value = "'入库仓批次效期强校验：1-是 0-否'")
    private String inBatchValidation;

	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;
	@ApiModelProperty(value = "主键id集合")
	private String ids;

	/**
	 * PO号
	 */
	@ApiModelProperty(value = "PO号")
	private String poNo;
	/**
	 * 退货方式 1-退货退款，2-仅退货
	 */
	@ApiModelProperty(value = "退货方式 1-退货退款，2-仅退货")
	private String returnMode;
	@ApiModelProperty(value = "退货方式（中文）")
	private String returnModeLabel;
	/**
	 * 关联销售单id
	 */
	@ApiModelProperty(value = "关联销售单id")
	private Long saleOrderId;
	/**
	 * 是否已生成预申报单 1-是,0-否
	 */
	@ApiModelProperty(value = "是否已生成预申报单 1-是,0-否")
	private String isGenerateDeclare;
	/**
	 * 业务模式 1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结
	 */
	@ApiModelProperty(value = "业务模式 1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结")
	private String businessModel;
	@ApiModelProperty(value = "业务模式(中文)")
	private String businessModelLabel;

	@ApiModelProperty(value = "是否已生成tob暂估 1-是,0-否")
	private String isGenerateTemp;

	@ApiModelProperty(value = "入库时间")
	private String returnInMonth;
	@ApiModelProperty(value = "销售退单号，多个以逗号隔开")
	private String returnCodes;
	/**
	 * 平台售后单号
	 */
	@ApiModelProperty(value = "平台售后单号")
	private String platformReturnCode;
	@ApiModelProperty(value = "退入仓库id集合")
	private List<Long> inDepotIdList;

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

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
		if(StringUtils.isNotBlank(returnType)){
			this.returnTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleReturnOrder_returnTypeList	, returnType);
		}
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
		if(StringUtils.isNotBlank(packType)){
			this.packTypeLabel = DERP.getLabelByKey(DERP.packTypeList, packType);
		}
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
		 if(StringUtils.isNotBlank(model)){
	    	   this.modelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleReturn_modelList, model);
	      }
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
		if(StringUtils.isNotBlank(serveTypes)){
			this.serveTypesLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleReturn_serveTypesList, serveTypes);
		}
	}

	/* status get 方法 */
	public String getStatus() {
		return status;
	}

	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
		if(StringUtils.isNotBlank(status)){
			this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleReturnOrder_statusList, status);
		}
	}

	public List<SaleReturnOrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleReturnOrderItemDTO> itemList) {
		this.itemList = itemList;
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
		if(StringUtils.isNotBlank(isSameArea)){
			this.isSameAreaLabel = DERP.getLabelByKey(DERP.isSameAreaList, isSameArea);
		}
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

	public String getReturnTypeLabel() {
		return returnTypeLabel;
	}

	public void setReturnTypeLabel(String returnTypeLabel) {
		this.returnTypeLabel = returnTypeLabel;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
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
		if(StringUtils.isNotBlank(currency)){
			this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList,currency);
		}
	}

	public String getCurrencyLabel() {
		return currencyLabel;
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

	public String getSaleOrderRelCode() {
		return saleOrderRelCode;
	}

	public void setSaleOrderRelCode(String saleOrderRelCode) {
		this.saleOrderRelCode = saleOrderRelCode;
	}
	public String getPackTypeLabel() {
		return packTypeLabel;
	}

	public void setPackTypeLabel(String packTypeLabel) {
		this.packTypeLabel = packTypeLabel;
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

	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}

	public String getOutDepotIsInOutInstruction() {
		return outDepotIsInOutInstruction;
	}

	public void setOutDepotIsInOutInstruction(String outDepotIsInOutInstruction) {
		this.outDepotIsInOutInstruction = outDepotIsInOutInstruction;
	}

	public String getOutDepotIsInDependOut() {
		return outDepotIsInDependOut;
	}

	public void setOutDepotIsInDependOut(String outDepotIsInDependOut) {
		this.outDepotIsInDependOut = outDepotIsInDependOut;
	}

	public List<Long> getBuList() {
		return buList;
	}

	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}



	public String getOutBatchValidation() {
		return outBatchValidation;
	}

	public void setOutBatchValidation(String outBatchValidation) {
		this.outBatchValidation = outBatchValidation;
	}



	public String getInDepotisOutDependIn() {
		return inDepotisOutDependIn;
	}

	public void setInDepotisOutDependIn(String inDepotisOutDependIn) {
		this.inDepotisOutDependIn = inDepotisOutDependIn;
	}

	public String getInBatchValidation() {
		return inBatchValidation;
	}

	public void setInBatchValidation(String inBatchValidation) {
		this.inBatchValidation = inBatchValidation;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
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
		if(StringUtils.isNotBlank(returnMode)){
			this.returnModeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleReturnOrder_returnModeList, returnMode);
		}
	}

	public String getReturnModeLabel() {
		return returnModeLabel;
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
		if(StringUtils.isNotBlank(businessModel)) {
			this.businessModelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_businessModelList, businessModel);
		}
	}

	public String getBusinessModelLabel() {
		return businessModelLabel;
	}

	public String getIsGenerateTemp() {
		return isGenerateTemp;
	}

	public void setIsGenerateTemp(String isGenerateTemp) {
		this.isGenerateTemp = isGenerateTemp;
	}

	public void setReturnModeLabel(String returnModeLabel) {
		this.returnModeLabel = returnModeLabel;
	}

	public void setBusinessModelLabel(String businessModelLabel) {
		this.businessModelLabel = businessModelLabel;
	}

	public String getReturnInMonth() {
		return returnInMonth;
	}

	public void setReturnInMonth(String returnInMonth) {
		this.returnInMonth = returnInMonth;
	}

	public String getReturnCodes() {
		return returnCodes;
	}

	public void setReturnCodes(String returnCodes) {
		this.returnCodes = returnCodes;
	}

	public String getPlatformReturnCode() {
		return platformReturnCode;
	}

	public void setPlatformReturnCode(String platformReturnCode) {
		this.platformReturnCode = platformReturnCode;
	}

	public List<Long> getInDepotIdList() {
		return inDepotIdList;
	}

	public void setInDepotIdList(List<Long> inDepotIdList) {
		this.inDepotIdList = inDepotIdList;
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

