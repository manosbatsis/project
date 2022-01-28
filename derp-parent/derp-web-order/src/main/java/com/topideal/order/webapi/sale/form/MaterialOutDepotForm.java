package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 领料出库单
 *
 */
@ApiModel
public class MaterialOutDepotForm  extends PageForm{
	@ApiModelProperty(value = "令牌",required = true)
	private String token; 
	@ApiModelProperty(value = "领料出库单号")
	private String code;
	@ApiModelProperty(value = "领料单号")
	private String materialOrderCode;
	@ApiModelProperty(value = "调出仓库ID") 
	private String outDepotId;	
	@ApiModelProperty(value = "po单号") 
	private String poNo;
	@ApiModelProperty(value = "客户ID(供应商)") 
	private String customerId;
	@ApiModelProperty(value = "事业部id")
	private String buId;
	@ApiModelProperty(value = "状态 017-待出库,018-已出库,027-出库中 006-已删除 ")
	private String status;
	@ApiModelProperty(value = "发货开始时间")
	private String deliverStartDate;
	@ApiModelProperty(value = "发货结束时间")
	private String deliverEndDate;
	 
	@ApiModelProperty(value = "主键集合ids")
	private String ids;
		 	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMaterialOrderCode() {
		return materialOrderCode;
	}
	public void setMaterialOrderCode(String materialOrderCode) {
		this.materialOrderCode = materialOrderCode;
	}
	public String getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(String outDepotId) {
		this.outDepotId = outDepotId;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getBuId() {
		return buId;
	}
	public void setBuId(String buId) {
		this.buId = buId;
	}	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDeliverStartDate() {
		return deliverStartDate;
	}
	public void setDeliverStartDate(String deliverStartDate) {
		this.deliverStartDate = deliverStartDate;
	}
	public String getDeliverEndDate() {
		return deliverEndDate;
	}
	public void setDeliverEndDate(String deliverEndDate) {
		this.deliverEndDate = deliverEndDate;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}