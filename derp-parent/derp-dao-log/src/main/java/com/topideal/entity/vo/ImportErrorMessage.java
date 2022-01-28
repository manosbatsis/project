package com.topideal.entity.vo;

import java.io.Serializable;

/**
 * 导入模板 错误提示信息
 * 
 * @author zhanghx
 */
public class ImportErrorMessage implements Serializable{

	// 行数
	private Integer rows;
	// 错误信息
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
