package com.topideal.json.api.v6_2;

import java.util.List;

/**
 * 仓储-自营库存更新
 * @author 杨创 
 * 2018.07.25
 */
public class CCSelfInventoryUpdateRootJson {
	private String sourceCode;//来源单据号
	private String adjustmentTypeName;//类型调整名称
	private Long depotId;//仓库id
	private String depotName;//仓库名称
	private String depotCode;//仓库编码
	private Long merchantId;//商家id
	private String merchantName;//商家名称
	private String topidealCode;//卓志编码
	private String depotType;//仓库类型
	private String depotIsTopBooks;//是否代销仓
	private String codeTime;//单据日期 格式：yyyy-MM-dd HH:mm:ss
	private String pushTime;//推送时间 格式：yyyy-MM-dd HH:mm:ss
	private String orderType;//调整类型 05：好坏品互转   06：效期调整    
	
	private List<CCSelfInventoryUpdateGoodsListJson>goodsList;

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

	public Long getDepotId() {
		return depotId;
	}

	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
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

	public String getDepotType() {
		return depotType;
	}

	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}

	public String getDepotIsTopBooks() {
		return depotIsTopBooks;
	}

	public void setDepotIsTopBooks(String depotIsTopBooks) {
		this.depotIsTopBooks = depotIsTopBooks;
	}

	public String getCodeTime() {
		return codeTime;
	}

	public void setCodeTime(String codeTime) {
		this.codeTime = codeTime;
	}

	public String getPushTime() {
		return pushTime;
	}

	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}

	public List<CCSelfInventoryUpdateGoodsListJson> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<CCSelfInventoryUpdateGoodsListJson> goodsList) {
		this.goodsList = goodsList;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	
}
