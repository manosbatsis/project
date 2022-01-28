package com.topideal.json.storage.j01;

import java.io.Serializable;
import java.util.List;

/**
 * 库存调整接口ROOT实体
 * 
 * @author 联想302
 *
 */
public class AdjustmentInventoryRootJson implements Serializable {

	private String merchantId; // 商家ID
	private String merchantName;// 商家名称
	private String depotId;// 仓库ID
	private String depotName;// 仓库名称
	private String topidealCode;//商家卓志编码
	private String model;// 业务类别
	private String sourceCode;// 来源单据号
	private String adjustmentTime;// 调整时间
	private String  sourceTime;//单据所属日期
	private String months;// 月份
	private List<AdjustmentInventoryGoodsListJson> goodsList; // 库存调整详情
	
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getDepotId() {
		return depotId;
	}
	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getAdjustmentTime() {
		return adjustmentTime;
	}
	public void setAdjustmentTime(String adjustmentTime) {
		this.adjustmentTime = adjustmentTime;
	}
	public String getMonths() {
		return months;
	}
	public void setMonths(String months) {
		this.months = months;
	}
	
	public String getTopidealCode() {
		return topidealCode;
	}
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}
	public List<AdjustmentInventoryGoodsListJson> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<AdjustmentInventoryGoodsListJson> goodsList) {
		this.goodsList = goodsList;
	}
	public String getSourceTime() {
		return sourceTime;
	}
	public void setSourceTime(String sourceTime) {
		this.sourceTime = sourceTime;
	}

	

	
	
}
