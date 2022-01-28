package com.topideal.report.webapi.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class InWarehouseDetailsForm extends PageForm{

	@ApiModelProperty(value = "令牌")
	private String token;

	@ApiModelProperty(value = "月份")
    private String month;
   
	@ApiModelProperty(value = "事业部")
    private Long buId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

}
