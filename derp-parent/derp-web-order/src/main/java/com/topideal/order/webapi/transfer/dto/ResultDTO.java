package com.topideal.order.webapi.transfer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * 返回信息dto
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

	public static ResultDTO fail(String message) {
		ResultDTO resultDTO = new ResultDTO();
		resultDTO.setCode("01");
		resultDTO.setMessage(StringUtils.isBlank(message)? "失败" : message);
		return resultDTO;
	}

	public static ResultDTO success(String message) {
		ResultDTO resultDTO = new ResultDTO();
		resultDTO.setCode("00");
		resultDTO.setMessage(StringUtils.isBlank(message)? "成功" : message);
		return resultDTO;
	}
}
