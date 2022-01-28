package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.entity.vo.main.CommbarcodeModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 组码响应Response
 * @author 杨创
 */
@ApiModel
public class GroupGetCommbarcodeResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "组码响应")
	private List<CommbarcodeModel> list;

	public List<CommbarcodeModel> getList() {
		return list;
	}

	public void setList(List<CommbarcodeModel> list) {
		this.list = list;
	}



}
