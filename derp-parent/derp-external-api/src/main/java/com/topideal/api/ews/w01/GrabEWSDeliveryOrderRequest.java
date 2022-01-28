package com.topideal.api.ews.w01;

/**
 * 抓取寄售商e仓发货订单
 * 
 * @author 杨创 2018/09/18
 */
public class GrabEWSDeliveryOrderRequest {
	private String root_in;//商家编码（卓志编码）
	private String merchant_code;//商家编码（卓志编码）
	private String status;//已发货：016
	private Integer datatype;//1按发货时间 2按支付时间
	private String start_time;//查询交易创建时间开始。格式:yyyy-MM-dd HH:mm:ss。默认返回3天数据
	private String end_time;//查询交易创建时间结束。格式:yyyy-MM-dd HH:mm:ss。
	private Integer page_no;//页码。取值范围:大于零的整数。默认值为1,即默认返回第一页数据。
	private Integer page_size;//每页条数。取值范围:大于零的整数; 默认值：100。
	private String has_next;//是否启用has_next的分页方式，如果指定1，则返回的结果中不包含总记录数，但是会新增一个是否存在下一页的的字段。
	private String order_id;//此字段不参与签名计算，订单号。如这个参数有传值，其它业务参数（datatype---has_next）不传值，为空；
	public String getMerchant_code() {
		return merchant_code;
	}
	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getDatatype() {
		return datatype;
	}
	public void setDatatype(Integer datatype) {
		this.datatype = datatype;
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
	public String getHas_next() {
		return has_next;
	}
	public void setHas_next(String has_next) {
		this.has_next = has_next;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getRoot_in() {
		return root_in;
	}
	public void setRoot_in(String root_in) {
		this.root_in = root_in;
	}
	
	

}
