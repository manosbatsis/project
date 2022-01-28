package com.topideal.json.api.v1_4;
/**
 * 
 * 单据状态更新实体
 *
 */
public class OrderStatusUpdateJson {

	private Long merchantId ;
	private String orderId; //订单单号
	private String statusCode;  //节点编码
	private String updateTime;       //发货时间
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
}
