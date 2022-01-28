package com.topideal.api.epass.e05;

/**
 * 查询订单100 实体类
 * @author 杨创
 * 2018/10/22
 */
public class EpassOrder100Request {
	
	private String electric_code;//电商企业编码
	private String start_time;//订单创建时间开始
	private String end_time;//订单创建时间结束
	private String order_status;//订单状态
	private String shop_id;//店铺编码
	private String order_id;//订单号
	private Integer page_no;//查询页数
	private Integer page_size;//每页查询数量
	
	
	
	public String getElectric_code() {
		return electric_code;
	}
	public void setElectric_code(String electric_code) {
		this.electric_code = electric_code;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public Integer getPage_no() {
		return page_no;
	}
	public void setPage_no(Integer page_no) {
		this.page_no = page_no;
	}
	public Integer getPage_size() {
		return page_size;
	}
	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}
	
	
	

}
