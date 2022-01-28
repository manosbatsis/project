package com.topideal.order.webapi.purchase.form;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class DeclareOrderDeliveryForm {
	
	@ApiModelProperty(value="票据", required=true)
	private String token ;

	@ApiModelProperty("预申报单ID")
	private Long declareOrderId ;
	
	@ApiModelProperty("入库时间")
	private String inboundDate ;
	
	@ApiModelProperty("商品表体")
	private List<DeclareOrderDeliveryItemForm> itemList ;

	public Long getDeclareOrderId() {
		return declareOrderId;
	}

	public void setDeclareOrderId(Long declareOrderId) {
		this.declareOrderId = declareOrderId;
	}

	public String getInboundDate() {
		return inboundDate;
	}

	public void setInboundDate(String inboundDate) {
		this.inboundDate = inboundDate;
	}

	public List<DeclareOrderDeliveryItemForm> getItemList() {
		return itemList;
	}

	public void setItemList(List<DeclareOrderDeliveryItemForm> itemList) {
		this.itemList = itemList;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
