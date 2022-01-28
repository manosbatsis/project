package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 销售退货入库
 * @author wenyan
 *
 */
@ApiModel
public class SaleReturnIdepotForm extends PageForm{
	
	@ApiModelProperty(value = "令牌",required = true)
	private String token; 
    @ApiModelProperty(value = "销售退货入库单号")
    private String code; 
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "退入仓库id")
    private String inDepotId;
    @ApiModelProperty(value = "销售退货编号")
    private String orderCode;
    @ApiModelProperty(value = "状态 011:待入仓  012:已入仓  006:已删除  028:入库中")
    private String status;
    @ApiModelProperty(value = "退货入库开始时间") 
    private String returnInDateStartDate;
    @ApiModelProperty(value = "退货入库结束时间") 
    private String returnInDateEndDate;
    @ApiModelProperty(value = "事业部id")
    private String buId;
    
   	@ApiModelProperty(value = "主键集合ids")
   	private String ids;
   	 
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getInDepotId() {
		return inDepotId;
	}
	public void setInDepotId(String inDepotId) {
		this.inDepotId = inDepotId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReturnInDateStartDate() {
		return returnInDateStartDate;
	}
	public void setReturnInDateStartDate(String returnInDateStartDate) {
		this.returnInDateStartDate = returnInDateStartDate;
	}
	public String getReturnInDateEndDate() {
		return returnInDateEndDate;
	}
	public void setReturnInDateEndDate(String returnInDateEndDate) {
		this.returnInDateEndDate = returnInDateEndDate;
	}
	public String getBuId() {
		return buId;
	}
	public void setBuId(String buId) {
		this.buId = buId;
	}
    
}
