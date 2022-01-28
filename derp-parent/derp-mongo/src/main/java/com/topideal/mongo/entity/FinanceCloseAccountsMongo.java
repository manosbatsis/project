package com.topideal.mongo.entity;

/**
 * 财务经销存关账表
 * 
 * @author 杨创
 */
public class FinanceCloseAccountsMongo {

	// 商家id
	private Long merchantId;
	// 商家名称
	private String merchantName;
	// 月份
	private String month;
	// 关账状态029-未关账 030-已关账  1-未结转 2-已结转
	private String status;
	//1.财务经销存关账 2.已经月结	
	private String source;
	// 仓库id
	private Long depotId;
	// 仓库名称
	private String depotName;
	// 事业部id
	private Long buId;
	// 事业部名称
	private String buName;
	// 创建时间
	private String createDateStr;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
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
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateDateStr() {
		return createDateStr;
	}
	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public String getBuName() {
		return buName;
	}
	public void setBuName(String buName) {
		this.buName = buName;
	}
	
	

}
