package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 平台为天猫（天猫出仓仓库不为天运二期仓）自动校验DTO
 * @author wenyan
 */
public class POPAutomaticTMDTO implements Serializable{
	// 单号
	private String code;
	// LP单号
	private String lpCode;
	// 商品条形码
	private String barcode;
	// 货品编码
	private String goodsCode;
	// 货品名称
	private String goodsName;
	// 仓库名称
	private String depotName;
	// 出入库时间
	private Timestamp outInDepotTime;
	// 单据类型
	private String orderType;
	// 库存类型
	private String inventoryType;
	// 出入数量
	private Integer outInNum;
	// 结存数量
	private Integer balanceAmount;
	// ERP订单号
	private String erpNo;
	// 外部流水号
	private String externalCode;
	//是否异常
	private String isException ;		
	//异常原因
	private String excetionResult ;
	// 映射SKU_ID
	private String skuId;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLpCode() {
		return lpCode;
	}
	public void setLpCode(String lpCode) {
		this.lpCode = lpCode;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public Timestamp getOutInDepotTime() {
		return outInDepotTime;
	}
	public void setOutInDepotTime(Timestamp outInDepotTime) {
		this.outInDepotTime = outInDepotTime;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getInventoryType() {
		return inventoryType;
	}
	public void setInventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
	}
	public Integer getOutInNum() {
		return outInNum;
	}
	public void setOutInNum(Integer outInNum) {
		this.outInNum = outInNum;
	}
	public Integer getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(Integer balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	public String getErpNo() {
		return erpNo;
	}
	public void setErpNo(String erpNo) {
		this.erpNo = erpNo;
	}
	public String getExternalCode() {
		return externalCode;
	}
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
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
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	
}
