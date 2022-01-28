package com.topideal.common.system.webapi;

import java.util.ArrayList;
import java.util.List;

public class APIResponse<T>{

	private String code = "0000";//响应编码 0000-成功 ,其他-失败 默认成功
	private String message = "成功";//响消息
	private Integer totalCount=0;// 总记录数据
	private Integer pageNo = 0;// 页码
	
	
	private List<T> dataList=new ArrayList<T>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
}
