package com.topideal.report.webapi.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class APIMonthlyAccountAutoVeriResponseDTO {
	@ApiModelProperty(value = "月份 yyyy-MM 页面传月份返回当前月份,页面不传 返回系统当前月份")
	private String month ;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	
	
	
}
