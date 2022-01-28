package com.topideal.mongo.entity;

public class ContractNoMongo {

	// 合同号
	private String contractNo;// ContractNo
	// 订单号
	private String orderCode;
	//
	private String type;// XSO-销售 , CGO-采购 , DBO-调拨, XSTO-销售退货

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
