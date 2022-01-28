package com.topideal.entity.dto;

import java.util.ArrayList;
import java.util.List;

public class ResponseRoot <T>{

	private String mCode = "0000";//响应编码 0000-成功 ,其他-失败 默认成功
	private String message = "成功";//响消息
	private Integer totalCount=0;// 总记录数据
	
	
	private List<T> dataList=new ArrayList<T>();

	public String getmCode() {
		return mCode;
	}

	public void setmCode(String mCode) {
		this.mCode = mCode;
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
	
}
