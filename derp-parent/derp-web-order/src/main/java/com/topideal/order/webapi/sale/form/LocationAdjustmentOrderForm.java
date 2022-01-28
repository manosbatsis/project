package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LocationAdjustmentOrderForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;

	@ApiModelProperty(value = "库位调整单号")
	private String code;
	
	@ApiModelProperty(value = "事业部id")
    private Long buId;

	@ApiModelProperty(value = "客户id")
	private Long customerId;
   
	@ApiModelProperty(value = "单据类型 DSDD-电商订单 DBRK-调拨入库 XSDD-销售订单 PYPK-盘盈盘亏 XSTH-销售退货单 XH-销毁")
    private String orderType;

	@ApiModelProperty(value = "状态")
	private String status;

	@ApiModelProperty(value = "归属月份")
	private String month;
	 
	@ApiModelProperty(value = "主键集合ids")
	private String ids;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
}
