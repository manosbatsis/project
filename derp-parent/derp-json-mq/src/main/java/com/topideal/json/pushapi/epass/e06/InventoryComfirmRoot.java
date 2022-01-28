package com.topideal.json.pushapi.epass.e06;

public class InventoryComfirmRoot {
	//企业盘点单号
	private String order_id;
	//指令值
	private String deal_result;
	//指令时间
	private String date;
	
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
