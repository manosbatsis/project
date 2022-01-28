package com.topideal.inventory.webapi.form;

import java.util.List;

import com.topideal.entity.dto.InitInventoryVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class InitInventorySaveForm {
	@ApiModelProperty(value = "期初保存实体")
	private List<InitInventoryVo> list;

	public List<InitInventoryVo> getList() {
		return list;
	}

	public void setList(List<InitInventoryVo> list) {
		this.list = list;
	}

	
	
	
	
}
