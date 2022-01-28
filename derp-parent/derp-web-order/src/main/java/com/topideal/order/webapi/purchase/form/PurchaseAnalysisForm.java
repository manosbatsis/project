package com.topideal.order.webapi.purchase.form;


import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 采购入库勾稽明细表form
 * 
 * @author gy
 */
@ApiModel
public class PurchaseAnalysisForm extends PageForm  {

	@ApiModelProperty(value = "令牌", required = true)
	private String token;
	// 采购入库编码
	@ApiModelProperty(value = "采购入库单号")
	private String warehouseCode;
	// 采购订单编号
	@ApiModelProperty(value = "采购订单号")
	private String orderCode;
	// 是否完结入库(1-是,0-否)
	@ApiModelProperty(value = "是否完结入库")
	private String isEnd;
	// 事业部id
	@ApiModelProperty(value = "事业部id")
	private Long buId;
	
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
