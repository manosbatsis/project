package com.topideal.inventory.webapi.form;

import java.util.List;

import com.topideal.entity.dto.MonthlyAccountDTO;
import com.topideal.entity.dto.MonthlyAccountItemDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class MonthlyAccountItemResponseDTO {
	@ApiModelProperty(value = "用户类型  1 平台用户  2 商家（超管理）  3 商家用户")
	private String userType ;
	@ApiModelProperty(value = "月结账单")
	private MonthlyAccountDTO monthlyAccountModel ;
	@ApiModelProperty(value = "月结账单明细")
	private List<MonthlyAccountItemDTO>  monthlyAccountItemModelList;
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public MonthlyAccountDTO getMonthlyAccountModel() {
		return monthlyAccountModel;
	}
	public void setMonthlyAccountModel(MonthlyAccountDTO monthlyAccountModel) {
		this.monthlyAccountModel = monthlyAccountModel;
	}
	public List<MonthlyAccountItemDTO> getMonthlyAccountItemModelList() {
		return monthlyAccountItemModelList;
	}
	public void setMonthlyAccountItemModelList(List<MonthlyAccountItemDTO> monthlyAccountItemModelList) {
		this.monthlyAccountItemModelList = monthlyAccountItemModelList;
	}
	
	
	

	
}
