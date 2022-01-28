package com.topideal.order.webapi.sale.form;

import java.util.List;

import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.dto.sale.SaleOutDepotItemDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel
public class SaveSaleOutDepotForm extends PageForm{
	@ApiModelProperty(value = "令牌",required = true)
	private String token; 
	@ApiModelProperty(value = "销售订单id") 
	private Long orderId;
	@ApiModelProperty(value = "销售订单code") 
	private String orderCode;
	@ApiModelProperty(value = "销售预申报单id") 
	private Long saleDeclareOrderId;
	@ApiModelProperty(value = "销售预申报单code") 
	private Long saleDeclareOrderCode;
	@ApiModelProperty(value = "出库时间",required = true)
	private String returnOutDate;
	@ApiModelProperty(value = "商品信息",required = true)
	private List<SaleOutDepotItemDTO> itemList;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public Long getSaleDeclareOrderId() {
		return saleDeclareOrderId;
	}
	public void setSaleDeclareOrderId(Long saleDeclareOrderId) {
		this.saleDeclareOrderId = saleDeclareOrderId;
	}
	public Long getSaleDeclareOrderCode() {
		return saleDeclareOrderCode;
	}
	public void setSaleDeclareOrderCode(Long saleDeclareOrderCode) {
		this.saleDeclareOrderCode = saleDeclareOrderCode;
	}
	public String getReturnOutDate() {
		return returnOutDate;
	}
	public void setReturnOutDate(String returnOutDate) {
		this.returnOutDate = returnOutDate;
	}
	public List<SaleOutDepotItemDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleOutDepotItemDTO> itemList) {
		this.itemList = itemList;
	}

}
