package com.topideal.webapi.form;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 获取启用用户响应
 * @author 杨创
 */
@ApiModel
public class RoleUserMapResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "用户id")
	private Long value;
	@ApiModelProperty(value = "用户名称")
	private String text;
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}



}
