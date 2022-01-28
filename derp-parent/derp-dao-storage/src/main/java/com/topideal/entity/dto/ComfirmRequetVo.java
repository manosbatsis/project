package com.topideal.entity.dto;

public class ComfirmRequetVo {
	private String order_id;//企业盘点单号
	private String deal_result;//10：驳回20：确认；
	private String date;//指令时间 时间格式：yyyy-mm-dd HH:mi:ss
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getDeal_result() {
		return deal_result;
	}
	public void setDeal_result(String deal_result) {
		this.deal_result = deal_result;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
