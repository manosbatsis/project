package com.topideal.entity.dto.api10001;

import java.util.ArrayList;
import java.util.List;

public class OutDepotOrder {
	private String topidealCode;//商家编码
	private String code;//来源单号
	private String businessNo;//业务单号
	private String orderType;//单据类型	
	private String depotCode;//仓库编码	
	private String depotName;//仓库名称	
	private String divergenceDate;//入库时间 YYYY-MM-dd HH:mm:ss
	private String contractNo;//合同号	
	private String poNo;//PO号
	private String customerCode;//客户编码
	private String customerName;//客户名称
	private String status;//出库单状态
	private String currency ;//币种
	
	private List<Goods> goodList=new ArrayList<Goods>();//商品列表

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
    
	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
    
	public List<Goods> getGoodList() {
		return goodList;
	}

	public void setGoodList(List<Goods> goodList) {
		this.goodList = goodList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
