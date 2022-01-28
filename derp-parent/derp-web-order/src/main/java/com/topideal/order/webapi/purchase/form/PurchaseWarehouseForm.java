package com.topideal.order.webapi.purchase.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 采购入库单
 * @author lianchenxing
 */
@ApiModel
public class PurchaseWarehouseForm extends PageForm implements Serializable {

	@ApiModelProperty(value="令牌", required=true)
	private String token;
	// 采购入库编号
	@ApiModelProperty(value="采购入库单号", required=false)
	private String code;
	// 仓库id
	@ApiModelProperty(value="仓库id", required=false)
	private Long depotId;
	// 状态  011:待入仓,012:已入仓
	@ApiModelProperty(value="状态  011:待入仓,012:已入仓", required=false)
	private String state;
	//拓展字段
	@ApiModelProperty(value="采购订单号", required=false)
	private String purchaseCode;
	//拓展字段 入库开始时间
	@ApiModelProperty(value="入库开始时间", required=false)
	private String warehouseStartDate;
	//拓展字段 入库结束时间
	@ApiModelProperty(value="入库结束时间", required=false)
	private String warehouseEndDate;
	// 事业部id
	@ApiModelProperty(value="事业部id", required=false)
	private Long buId;
	//勾稽状态1-部分勾稽 2-已勾稽 3-勾稽失败
	@ApiModelProperty(value="采购入库勾稽状态1-未勾稽 2-已勾稽3-勾稽失败", required=false)
	private String correlationStatus;
    //合同号
	@ApiModelProperty(value="合同号", required=false)
    private String contractNo;

	@ApiModelProperty(value="采购订单ID", required=false)
	private Long purchaseId;

	@ApiModelProperty(value="入库时间", required=false)
	private String inboundDate;

	@ApiModelProperty("商品表体")
	private List<DeclareOrderDeliveryItemForm> itemList ;

	@ApiModelProperty(value="预申报单编号", required=false)
	private String declareOrderCode;

	@ApiModelProperty(value="是否红冲单：0-否，1-是")
	private String isWriteOff;

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPurchaseCode() {
		return purchaseCode;
	}
	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}
	public String getWarehouseStartDate() {
		return warehouseStartDate;
	}
	public void setWarehouseStartDate(String warehouseStartDate) {
		this.warehouseStartDate = warehouseStartDate;
	}
	public String getWarehouseEndDate() {
		return warehouseEndDate;
	}
	public void setWarehouseEndDate(String warehouseEndDate) {
		this.warehouseEndDate = warehouseEndDate;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public String getCorrelationStatus() {
		return correlationStatus;
	}
	public void setCorrelationStatus(String correlationStatus) {
		this.correlationStatus = correlationStatus;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getInboundDate() {
		return inboundDate;
	}

	public void setInboundDate(String inboundDate) {
		this.inboundDate = inboundDate;
	}

	public List<DeclareOrderDeliveryItemForm> getItemList() {
		return itemList;
	}

	public void setItemList(List<DeclareOrderDeliveryItemForm> itemList) {
		this.itemList = itemList;
	}

	public String getDeclareOrderCode() {
		return declareOrderCode;
	}

	public void setDeclareOrderCode(String declareOrderCode) {
		this.declareOrderCode = declareOrderCode;
	}

	public String getIsWriteOff() {
		return isWriteOff;
	}

	public void setIsWriteOff(String isWriteOff) {
		this.isWriteOff = isWriteOff;
	}
}
