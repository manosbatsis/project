package com.topideal.report.webapi.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class BuFinanceInventoryAddResponseDTO {
	@ApiModelProperty(value = "开始时间 yyyy-MM-dd")
	private String nowStart ;
	@ApiModelProperty(value = "结束时间 yyyy-MM-dd")
	private String nowEnd ;
	public String getNowStart() {
		return nowStart;
	}
	public void setNowStart(String nowStart) {
		this.nowStart = nowStart;
	}
	public String getNowEnd() {
		return nowEnd;
	}
	public void setNowEnd(String nowEnd) {
		this.nowEnd = nowEnd;
	}
	
	
	
}
