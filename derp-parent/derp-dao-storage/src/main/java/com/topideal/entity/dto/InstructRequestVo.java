package com.topideal.entity.dto;

import java.util.List;

/**
 * 盘点指令
 * */
public class InstructRequestVo {
	private String order_id;//企业盘点单号	
	private String order_date;//录入日期	时间格式：yyyy-mm-dd HH:mi:ss
	private String trust_code;//委托单位	字符串
	private String electric_code;//电商企业编码	字符串
	private String warehouse_id;//仓库编码	字符串
	private String serve_types;//服务类型	字符串
	private String busi_scene;//业务场景	字符串
	private List<InstructGood> good_list;//商品列表	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getTrust_code() {
		return trust_code;
	}
	public void setTrust_code(String trust_code) {
		this.trust_code = trust_code;
	}
	public String getElectric_code() {
		return electric_code;
	}
	public void setElectric_code(String electric_code) {
		this.electric_code = electric_code;
	}
	public String getWarehouse_id() {
		return warehouse_id;
	}
	public void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = warehouse_id;
	}
	public String getServe_types() {
		return serve_types;
	}
	public void setServe_types(String serve_types) {
		this.serve_types = serve_types;
	}
	public String getBusi_scene() {
		return busi_scene;
	}
	public void setBusi_scene(String busi_scene) {
		this.busi_scene = busi_scene;
	}
	public List<InstructGood> getGood_list() {
		return good_list;
	}
	public void setGood_list(List<InstructGood> good_list) {
		this.good_list = good_list;
	}
	

}
