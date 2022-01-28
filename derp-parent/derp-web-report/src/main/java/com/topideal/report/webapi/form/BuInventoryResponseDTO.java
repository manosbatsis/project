package com.topideal.report.webapi.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class BuInventoryResponseDTO {
	@ApiModelProperty(value = "月份")
	private String now ;
	@ApiModelProperty(value = "用户类型  1 平台用户  2 商家（超管理）  3 商家用户")
	private String userType ;
	public String getNow() {
		return now;
	}
	public void setNow(String now) {
		this.now = now;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}

	
	
}
