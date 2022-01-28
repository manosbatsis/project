package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.entity.vo.main.CommbarcodeModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户访问列表页面Response
 * @author 杨创
 */
@ApiModel
public class UserToPageResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "响应标识")
	private String tag;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}




}
