package com.topideal.entity.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 平台为京东自动校验DTO
 *
 */
public class POPAutomaticJDDTO implements Serializable{

	//销售平台订单号
	private String saleOrderCode ;
	//实际出库数量
	private Integer outDepotNum ;
	//条形码
	private String barcode ;
	//是否异常
	private String isException ;		
	//异常原因
	private String excetionResult ;
	// 映射SKU_ID
	private String skuId;
	
	public String getSaleOrderCode() {
		return saleOrderCode;
	}
	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}
	public Integer getOutDepotNum() {
		return outDepotNum;
	}
	public void setOutDepotNum(Integer outDepotNum) {
		this.outDepotNum = outDepotNum;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
