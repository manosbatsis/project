package com.topideal.json.api.v6_3;

import java.util.List;

/**
 * 调拨单回推
 * 
 * @author 杨创 2018.7.25
 */
public class CCTransferOrderRootJson {
	private String sourceCode;//来源单据号
	private String adjustmentTypeName;//类型调整名称
	private Long inDepotId;//入库仓库id
	private String inDepotName;//入库仓库名称
	private String inDepotCode;//入库仓库编码
	private String inDepotType;//入库仓库类型
	private String inDepotIsTopBooks;//入库是否代销仓
	private Long outDepotId;//出库仓库id
	private String outDepotName;//出库仓库名称
	private String outDepotCode;//出库仓库编码
	private String outDepotType;//出库仓库类型
	private String outDepotIsTopBooks;//出库是否代销仓
	private Long merchantId;//商家id
	private String merchantName;//商家名称
	private String topidealCode;//卓志编码	
	private String codeTime;//单据日期 格式：yyyy-MM-dd HH:mm:ss
	private List<CCTransferOrderGoodListJson>goodsList;
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getAdjustmentTypeName() {
		return adjustmentTypeName;
	}
	public void setAdjustmentTypeName(String adjustmentTypeName) {
		this.adjustmentTypeName = adjustmentTypeName;
	}
	public Long getInDepotId() {
		return inDepotId;
	}
	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}
	public String getInDepotName() {
		return inDepotName;
	}
	public void setInDepotName(String inDepotName) {
		this.inDepotName = inDepotName;
	}
	public String getInDepotCode() {
		return inDepotCode;
	}
	public void setInDepotCode(String inDepotCode) {
		this.inDepotCode = inDepotCode;
	}
	public String getInDepotType() {
		return inDepotType;
	}
	public void setInDepotType(String inDepotType) {
		this.inDepotType = inDepotType;
	}
	public String getInDepotIsTopBooks() {
		return inDepotIsTopBooks;
	}
	public void setInDepotIsTopBooks(String inDepotIsTopBooks) {
		this.inDepotIsTopBooks = inDepotIsTopBooks;
	}
	public Long getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}
	public String getOutDepotName() {
		return outDepotName;
	}
	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}
	public String getOutDepotCode() {
		return outDepotCode;
	}
	public void setOutDepotCode(String outDepotCode) {
		this.outDepotCode = outDepotCode;
	}
	public String getOutDepotType() {
		return outDepotType;
	}
	public void setOutDepotType(String outDepotType) {
		this.outDepotType = outDepotType;
	}
	public String getOutDepotIsTopBooks() {
		return outDepotIsTopBooks;
	}
	public void setOutDepotIsTopBooks(String outDepotIsTopBooks) {
		this.outDepotIsTopBooks = outDepotIsTopBooks;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getTopidealCode() {
		return topidealCode;
	}
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}
	public String getCodeTime() {
		return codeTime;
	}
	public void setCodeTime(String codeTime) {
		this.codeTime = codeTime;
	}
	public List<CCTransferOrderGoodListJson> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<CCTransferOrderGoodListJson> goodsList) {
		this.goodsList = goodsList;
	}
	
	

}
