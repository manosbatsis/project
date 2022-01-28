package com.topideal.inventory.webapi.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class BuInventoryResponseDTO {
	@ApiModelProperty(value = "事业部id")
	private Long buId ;

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	
	
	
	
}
