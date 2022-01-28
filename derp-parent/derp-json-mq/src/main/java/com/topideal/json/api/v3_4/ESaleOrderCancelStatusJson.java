package com.topideal.json.api.v3_4;

/**
 * 订单取消状态回推
 * @author lian_
 *
 */
public class ESaleOrderCancelStatusJson {

	private String orderCode;   //订单号
	private String updateDate;  //日期格式：yyyy-mm-dd 
	private String status;      //单据状态 字符串90：作废
	private Long merchantId ;
	
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
}
