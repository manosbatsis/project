package com.topideal.order.webapi.sale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 返回信息dto
 * @date 2021-02-07
 */
@ApiModel
public class ResultDTO {
	
	@ApiModelProperty(value = "返回编码 00-成功，01-失败")
	private String code;
	@ApiModelProperty(value = "返回信息描述")
	private String message;
	@ApiModelProperty(value = "返回数据")
	private Object data;

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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
