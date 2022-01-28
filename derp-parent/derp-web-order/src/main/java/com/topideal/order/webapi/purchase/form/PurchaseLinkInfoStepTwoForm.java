package com.topideal.order.webapi.purchase.form;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 采购链路step 2 返回form
 * @author Guobs
 *
 */
@ApiModel
public class PurchaseLinkInfoStepTwoForm {

	@ApiModelProperty(value="采购链路ID")
	private Long id ;
	
	@ApiModelProperty(value="预览订单集合")
	private Map<String, Object> map ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	
}
