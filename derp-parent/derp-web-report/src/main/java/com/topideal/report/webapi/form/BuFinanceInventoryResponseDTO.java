package com.topideal.report.webapi.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class BuFinanceInventoryResponseDTO {
	@ApiModelProperty(value = "月份")
	private String now ;

	public String getNow() {
		return now;
	}

	public void setNow(String now) {
		this.now = now;
	}


	
	
}
