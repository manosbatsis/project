package com.topideal.entity.dto.main;



import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 导入模板 错误提示信息
 * 
 * @author zhanghx
 */
public class ImportErrorMessage implements Serializable{

	// 行数
	@ApiModelProperty(value = "行数",position = 0)
	private Integer rows;
	// 错误信息
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
