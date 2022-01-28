package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 访问客户币种页面
 * @author 杨创
 */
@ApiModel
public class CustomerCurrencyResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "selectValue")
	private String selectValue;
	@ApiModelProperty(value = "selectLable")
	private String selectLable;
	public String getSelectValue() {
		return selectValue;
	}
	public void setSelectValue(String selectValue) {
		this.selectValue = selectValue;
	}
	public String getSelectLable() {
		return selectLable;
	}
	public void setSelectLable(String selectLable) {
		this.selectLable = selectLable;
	}
	
	
	




}
