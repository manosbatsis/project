package com.topideal.api.smurfs.s01;


/**
 * 查询蓝精灵订单采集 实体类
 * @author 杨创
 * 2019/03/12
 */
public class SmurfsOrderCollectionRequest {
	
	private String storeCode;//店铺编码
	private String orderNo;//订单号
	private String startDate;//订单创建时间开始 格式：yyyy-MM-dd HH:mm:ss
	private String endDate;//订单创建时间结束 格式：yyyy-MM-dd HH:mm:ss	
	private String status;//订单状态（多个以英文“,”逗号分隔）待付款：1、已付款：2部分发货：3、已发货：4  已完成：5、已关闭：6
	private Integer pageNo;// 页数 蓝精灵接口2.0必填字段
	private Integer pageSize;//	每页显示的记录数 2.0必填字段
	
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

	
	
	

	

}
