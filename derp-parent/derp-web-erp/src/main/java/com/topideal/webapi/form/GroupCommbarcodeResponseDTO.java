package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.entity.dto.main.GroupCommbarcodeDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 组码响应Response
 * @author 杨创
 */
@ApiModel
public class GroupCommbarcodeResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "组码响应")
	private List<GroupCommbarcodeDTO> list;

	public List<GroupCommbarcodeDTO> getList() {
		return list;
	}

	public void setList(List<GroupCommbarcodeDTO> list) {
		this.list = list;
	}



}
