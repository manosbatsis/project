package com.topideal.report.webapi.form;

import java.util.List;

import com.topideal.mongo.entity.MerchantShopRelMongo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class AutomaticCheckTaskToAddResponseDTO {
	@ApiModelProperty(value = "商家店铺信息表")
	private List<MerchantShopRelMongo> shopList ;

	public List<MerchantShopRelMongo> getShopList() {
		return shopList;
	}

	public void setShopList(List<MerchantShopRelMongo> shopList) {
		this.shopList = shopList;
	}


	
}
