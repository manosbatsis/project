package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.topideal.entity.vo.main.CommbarcodeModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户绑定用户信息Response
 * @author 杨创
 */
@ApiModel
public class RoleSearchUserResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "所有用户")
	private List<RoleUserMapResponseDTO> allUser;

	@ApiModelProperty(value = "绑定用户")
	private List<String> bindUser;

	public List<RoleUserMapResponseDTO> getAllUser() {
		return allUser;
	}

	public void setAllUser(List<RoleUserMapResponseDTO> allUser) {
		this.allUser = allUser;
	}

	public List<String> getBindUser() {
		return bindUser;
	}

	public void setBindUser(List<String> bindUser) {
		this.bindUser = bindUser;
	}




}
