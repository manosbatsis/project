package com.topideal.order.webapi.purchase.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PurchaseOrderForm extends PageForm {

	@ApiModelProperty(value = "令牌", required = true)
	private String token ;
	// 采购订单编号
	@ApiModelProperty(value = "采购订单编号", required = false)
	private String code;
	// 供应商ID
	@ApiModelProperty(value = "供应商ID", required = false)
	private Long supplierId;
	// 业务模式 1 采购 2 代销
	@ApiModelProperty(value = "业务模式 1 采购 2 代销", required = false)
	private String businessModel;
	// 仓库ID
	@ApiModelProperty(value = "仓库ID", required = false)
	private Long depotId;
	// PO号
	@ApiModelProperty(value = "PO号，多个以‘,’隔开", required = false)
	private String poNos;

	private String poNo;
	// 状态 001,待审核,003,已审核,006,已删除,007,已完结
	@ApiModelProperty(value = "状态 001,待审核,003,已审核,006,已删除,007,已完结", required = false)
	private String status;
	// 是否完结(1-是，0-否)
	@ApiModelProperty(value = "是否完结(1-是，0-否)", required = false)
	private String isEnd;
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
	/**
	 *  事业部id
	 */
	@ApiModelProperty(value = "事业部id", required = false)
	private Long buId;
	//金额调整状态1-已调整 0-未调整
	@ApiModelProperty(value = "金额调整状态1-已调整 0-未调整", required = false)
	private String amountAdjustmentStatus;
	/**
	 * 金额确认状态1-已确认 0-未确认
	 */
	@ApiModelProperty(value = "金额确认状态1-已确认 0-未确认", required = false)
	private String amountConfirmStatus;

	@ApiModelProperty(value = "导出IDS", required = false)
	private String ids;

	@ApiModelProperty(value = "多个订单号查询，以‘,’隔开", required = false)
	private String codes;

	@ApiModelProperty(value = "融资申请号", required = false)
	private String financingNo;

	@ApiModelProperty(value = "创建人用户名", required = false)
	private String createName;

	@ApiModelProperty(value = "发票开始时间", required = false)
	private String invoiceStartDate;

	@ApiModelProperty(value = "发票结束时间", required = false)
	private String invoiceEndDate;

	@ApiModelProperty(value = "红冲状态: 001-待红冲 002-红冲中 003-已红冲")
	private String writeOffStatus;
	
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
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
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public String getAmountAdjustmentStatus() {
		return amountAdjustmentStatus;
	}
	public void setAmountAdjustmentStatus(String amountAdjustmentStatus) {
		this.amountAdjustmentStatus = amountAdjustmentStatus;
	}
	public String getAmountConfirmStatus() {
		return amountConfirmStatus;
	}
	public void setAmountConfirmStatus(String amountConfirmStatus) {
		this.amountConfirmStatus = amountConfirmStatus;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getCodes() {
		return codes;
	}
	public void setCodes(String codes) {
		this.codes = codes;
	}
	public String getPoNos() {
		return poNos;
	}
	public void setPoNos(String poNos) {
		this.poNos = poNos;
	}

	public String getFinancingNo() {
		return financingNo;
	}

	public void setFinancingNo(String financingNo) {
		this.financingNo = financingNo;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
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
}
