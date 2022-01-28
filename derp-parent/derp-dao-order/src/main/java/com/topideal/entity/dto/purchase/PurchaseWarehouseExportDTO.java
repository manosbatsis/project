package com.topideal.entity.dto.purchase;

import com.topideal.common.constant.DERP_ORDER;

import java.io.Serializable;
import java.util.List;

/**
 * 入库明细导出表头
 *
 * @author zhanghx
 */
public class PurchaseWarehouseExportDTO implements Serializable {

	// 商家id
	private String merchantCode;
	// 入库单号
	private String warehouseCode;
	// 入库单号
	private Long warehouseId;
	// 仓库编码
	private String depotCode;
	private String depotName;
	// 预申报单号
	private String declareCode;
	// 理货单号
	private String tallyingCode;
	// 理货时间
	private String tallyingDate;
	// 合同号
	private String contractNo;
	// 业务场景
	private String businessType;
	// 单据状态
	private String state;
	private String stateLabel;
	// 托板数量
	private String palletNum;
	// LBX单号
	private String lbxNo;
	// 入库时间
	private String inboundDate;
	// 入库类型
	private String warehouseType;
	// 仓库id
	private String depotId;
	// 商家名称
	private String merchantName;
	// 事业部
	private String buName ;
	// 合同号
	private String extraCode ;
	// 合同号
	private String purchaseCode ;

	// 表体
	private List<PurchaseWarehouseItemExportDTO> itemList;

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getDepotId() {
		return depotId;
	}

	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}

	public List<PurchaseWarehouseItemExportDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<PurchaseWarehouseItemExportDTO> itemList) {
		this.itemList = itemList;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getDeclareCode() {
		return declareCode;
	}

	public void setDeclareCode(String declareCode) {
		this.declareCode = declareCode;
	}

	public String getTallyingCode() {
		return tallyingCode;
	}

	public void setTallyingCode(String tallyingCode) {
		this.tallyingCode = tallyingCode;
	}

	public String getTallyingDate() {
		return tallyingDate;
	}

	public void setTallyingDate(String tallyingDate) {
		this.tallyingDate = tallyingDate;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getPalletNum() {
		return palletNum;
	}

	public void setPalletNum(String palletNum) {
		this.palletNum = palletNum;
	}

	public String getLbxNo() {
		return lbxNo;
	}

	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	public String getWarehouseType() {
		return warehouseType;
	}

	public void setWarehouseType(String warehouseType) {
		this.warehouseType = warehouseType;
	}

	public String getInboundDate() {
		return inboundDate;
	}

	public void setInboundDate(String inboundDate) {
		this.inboundDate = inboundDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		this.stateLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseWarehouse_stateList, state);
	}

	public String getStateLabel() {
		return stateLabel;
	}

	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel ;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public String getExtraCode() {
		return extraCode;
	}

	public void setExtraCode(String extraCode) {
		this.extraCode = extraCode;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}
}
