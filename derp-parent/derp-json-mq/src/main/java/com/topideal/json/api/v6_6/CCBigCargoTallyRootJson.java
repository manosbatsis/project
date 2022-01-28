package com.topideal.json.api.v6_6;

import java.util.List;

/**
 * 大货理货接口实体
 * @author 杨创
 *2019.04.01 
 */
public class CCBigCargoTallyRootJson {
	private String orderId;// 来源单据号
	private Long merchantId;// 商家id
	private String merchantName;//商家名称
	private String topidealCode;//卓志编码	
	private Long depotId;//仓库id
	private String depotName;//仓库名称
	private String depotCode;//仓库编码
	private String depotType;//仓库类型
	private String isTopBooks;//是否代销仓
	private String batchValidation;// 是否强校验 1是 0 否
	private String bomFinishTime; // 加工时间	
	private List <CCBigCargoTallyOriginGoodsListJson>  originGoodsList;// 原商品批次
	private List <CCBigCargoTallyTargeGoodsListJson> targeGoodsList;// 目标商品批次
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public String getDepotType() {
		return depotType;
	}
	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}
	public String getIsTopBooks() {
		return isTopBooks;
	}
	public void setIsTopBooks(String isTopBooks) {
		this.isTopBooks = isTopBooks;
	}
	public String getBatchValidation() {
		return batchValidation;
	}
	public void setBatchValidation(String batchValidation) {
		this.batchValidation = batchValidation;
	}
	public List<CCBigCargoTallyOriginGoodsListJson> getOriginGoodsList() {
		return originGoodsList;
	}
	public void setOriginGoodsList(List<CCBigCargoTallyOriginGoodsListJson> originGoodsList) {
		this.originGoodsList = originGoodsList;
	}
	public List<CCBigCargoTallyTargeGoodsListJson> getTargeGoodsList() {
		return targeGoodsList;
	}
	public void setTargeGoodsList(List<CCBigCargoTallyTargeGoodsListJson> targeGoodsList) {
		this.targeGoodsList = targeGoodsList;
	}
	public String getBomFinishTime() {
		return bomFinishTime;
	}
	public void setBomFinishTime(String bomFinishTime) {
		this.bomFinishTime = bomFinishTime;
	}
	
	

	


}
