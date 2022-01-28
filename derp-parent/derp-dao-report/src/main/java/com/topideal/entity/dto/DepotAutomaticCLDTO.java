package com.topideal.entity.dto;

import java.sql.Timestamp;

/**
 *  菜鸟仓自动校验DTO
 * @author gy
 *
 */
public class DepotAutomaticCLDTO {

	//单号
	private String lbxCode ; 
	
	//LB单号
	private String lbCode ;
	
	//商品货号
	private String goodsNo ; 
	
	//商品名称
	private String goodsName ;
	
	//仓库名称
	private String depotName ;
	
	//出入库时间
	private Timestamp depotTime ;
	
	//单据类型
	private String billType ;
	
	//库存类型
	private String inventoryType ; 
	
	//出入数量
	private Integer account ;
	
	//结存数量
	private Integer settleAccount ;
	
	//ERP订单号
	private String erpCode ;
	
	//外部流水号
	private String extraCode ;
	
	//是否异常
	private String isException ;
	
	//异常原因
	private String excetionResult ;

	public String getLbxCode() {
		return lbxCode;
	}

	public void setLbxCode(String lbxCode) {
		this.lbxCode = lbxCode;
	}

	public String getLbCode() {
		return lbCode;
	}

	public void setLbCode(String lbCode) {
		this.lbCode = lbCode;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Timestamp getDepotTime() {
		return depotTime;
	}

	public void setDepotTime(Timestamp depotTime) {
		this.depotTime = depotTime;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
	}

	public Integer getAccount() {
		return account;
	}

	public void setAccount(Integer account) {
		this.account = account;
	}

	public Integer getSettleAccount() {
		return settleAccount;
	}

	public void setSettleAccount(Integer settleAccount) {
		this.settleAccount = settleAccount;
	}

	public String getErpCode() {
		return erpCode;
	}

	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}

	public String getExtraCode() {
		return extraCode;
	}

	public void setExtraCode(String extraCode) {
		this.extraCode = extraCode;
	}

	public String getIsException() {
		return isException;
	}

	public void setIsException(String isException) {
		this.isException = isException;
	}

	public String getExcetionResult() {
		return excetionResult;
	}

	public void setExcetionResult(String excetionResult) {
		this.excetionResult = excetionResult;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	
	
}
