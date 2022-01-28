package com.topideal.entity.dto;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *  菜鸟仓自动校验DTO
 * @author gy
 *
 */
public class DepotAutomaticGssDTO {

	//商家名称
	private String merchantName ; 
	
	//仓库名称
	private String depotName ;
	
	//商品货号
	private String goodsNo ; 
	
	//商品名称
	private String goodsName ;
	
	//商品OPG号
	private String opgCode ;
	
	//交易创建时间
	private Timestamp tradeCreateTime ;
	
	//单据类型
	private String billType ;
	
	//交易类型
	private String sourceType ; 
	
	//交易数量
	private Integer account ;
	
	//单位
	private String unit ;
	
	//业务单据号
	private String sourceCode ;
	
	//原始批次号
	private String batchNo ;
	
	//是否异常
	private String isException ;
	
	//异常原因
	private String excetionResult ;
	
	//生产日期
	private Date productionDate ;
	
	//失效日期
	private Date overdueDate ;


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

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public Integer getAccount() {
		return account;
	}

	public void setAccount(Integer account) {
		this.account = account;
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

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getOpgCode() {
		return opgCode;
	}

	public void setOpgCode(String opgCode) {
		this.opgCode = opgCode;
	}

	public Timestamp getTradeCreateTime() {
		return tradeCreateTime;
	}

	public void setTradeCreateTime(Timestamp tradeCreateTime) {
		this.tradeCreateTime = tradeCreateTime;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public Date getOverdueDate() {
		return overdueDate;
	}

	public void setOverdueDate(Date overdueDate) {
		this.overdueDate = overdueDate;
	}
	
	
}
