package com.topideal.common.system.webapi;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 导入模板 错误提示信息
 */
@ApiModel
public class ImportMessage {

	@ApiModelProperty(value = "行数",position = 0)
	private Integer rows;
	@ApiModelProperty(value = "错误信息",position = 1)
	private String message;

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
