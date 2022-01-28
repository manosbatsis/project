package com.topideal.json.api.v2_2;
/**
 * 进仓单状态回推 mq报文
 * @author lian_
 *
 */
public class SaleReturnIntoWarehouseStatusJson {

	
	private String entInboundId;//企业入仓编号（电商企业采购编号，唯一）
	private String status;//1: 成功 ，2： 失败
	private String merchantId;//商家id
	private String merchantName;//商家名称
	private String topidealCode;//商家卓志编码
	private String inboundDate;//进仓单生效时间
	public String getEntInboundId() {
		return entInboundId;
	}
	public void setEntInboundId(String entInboundId) {
		this.entInboundId = entInboundId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
	public String getTopidealCode() {
		return topidealCode;
	}
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}
	public String getInboundDate() {
		return inboundDate;
	}
	public void setInboundDate(String inboundDate) {
		this.inboundDate = inboundDate;
	}

	
}
