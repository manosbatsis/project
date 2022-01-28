package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 出库仓库为 综合1仓 自动校验DTO
 *
 */
public class POPAutomaticZHYCDTO implements Serializable{
	// 商家名称
	private String merchantName; 
	// 仓库名称
	private String depotName; 
	// 商品货号
	private String goodsNo;
	// 商品OPG号
	private String goodsOPGNo;
	// 商品名称
	private String goodsName;
	// 交易类型
	private String tradeType;
	// 交易数量
	private Integer num;
	// 单位
	private String unit;
	// 业务单据号
	private String businessNo;
	// 原始批次号
	private String originalBatchNo;
	// 生产日期
	private Date productionDate;
	// 失效日期
	private Date overdueDate;
	// 交易创建时间
	private Timestamp tradeCreateDate;
	// 平台订单号
	private String externalCode;
	// 发货单号
	private String deliverNo;
	// 发货时间
	private Timestamp deliverDate;
	// 外部系统单号
	private String externalSystemNo;
	//是否异常
	private String isException ;		
	//异常原因
	private String excetionResult ;
	
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getGoodsOPGNo() {
		return goodsOPGNo;
	}
	public void setGoodsOPGNo(String goodsOPGNo) {
		this.goodsOPGNo = goodsOPGNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	public String getOriginalBatchNo() {
		return originalBatchNo;
	}
	public void setOriginalBatchNo(String originalBatchNo) {
		this.originalBatchNo = originalBatchNo;
	}
	public String getExternalCode() {
		return externalCode;
	}
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}
	public String getDeliverNo() {
		return deliverNo;
	}
	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}
	public String getExternalSystemNo() {
		return externalSystemNo;
	}
	public void setExternalSystemNo(String externalSystemNo) {
		this.externalSystemNo = externalSystemNo;
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
	public Timestamp getTradeCreateDate() {
		return tradeCreateDate;
	}
	public void setTradeCreateDate(Timestamp tradeCreateDate) {
		this.tradeCreateDate = tradeCreateDate;
	}
	public Timestamp getDeliverDate() {
		return deliverDate;
	}
	public void setDeliverDate(Timestamp deliverDate) {
		this.deliverDate = deliverDate;
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
	
}
