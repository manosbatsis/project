package com.topideal.entity.dto.api10002;

import java.util.List;

public class InDepotOrder {
	private String topidealCode;//商家编码
	private String code;//来源单号
	private String businessNo;//业务单号
	private String orderType;//单据类型	
	private String depotCode;//仓库编码	
	private String depotName;//仓库名称	
	private String divergenceDate;//入库时间 YYYY-MM-dd HH:mm:ss
	private String contractNo;//合同号	
	private String poNo;//PO号
	private String supplierCode;//供应商编码
	private String supplierName;//供应商名称
	private String currency;//币种
	private String status;//01-已入库、02-已删除（指已入库后又删除回滚）
	
	private List<InGoods> goodList;//商品列表

	public String getTopidealCode() {
		return topidealCode;
	}

	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public String getDivergenceDate() {
		return divergenceDate;
	}

	public void setDivergenceDate(String divergenceDate) {
		this.divergenceDate = divergenceDate;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<InGoods> getGoodList() {
		return goodList;
	}

	public void setGoodList(List<InGoods> goodList) {
		this.goodList = goodList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
