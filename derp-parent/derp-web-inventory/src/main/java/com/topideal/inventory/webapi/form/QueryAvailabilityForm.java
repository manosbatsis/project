package com.topideal.inventory.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 校验库存量接口是否足够
 *
 * @author lian_
 */
@ApiModel
public class QueryAvailabilityForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "令牌", required = true)
    private String token;
	@ApiModelProperty(value = "表体信息",required = true)
	private List<QueryAvailabilityItem> itemList;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public List<QueryAvailabilityItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<QueryAvailabilityItem> itemList) {
		this.itemList = itemList;
	}
	
	

  
}

