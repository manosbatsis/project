package com.topideal.order.webapi.sale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品数量返回值 dto
 * @date 2021-02-07
 */
@ApiModel
public class OrderGoodsInfoNumDTO {
	
	@ApiModelProperty(value = "key")
	private String code;
	@ApiModelProperty(value = "value，商品数量")
	private Integer num;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}

	
}
