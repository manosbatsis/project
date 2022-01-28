package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 销售退货出库
 * @author wenyan
 *
 */
@ApiModel
public class SaleReturnOdepotForm  extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
    @ApiModelProperty(value = "销售退货出库单号")
    private String code;
    @ApiModelProperty(value = "退出仓库id")
    private String outDepotId;
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "销售退货编号")
    private String orderCode;
    @ApiModelProperty(value = "状态015:待出仓,016:已出仓,027:出库中 006-已删除")
    private String status;
    @ApiModelProperty(value = "退货出库开始时间") 
    private String returnOutStartDate;
    @ApiModelProperty(value = "退货出库结束时间") 
    private String returnOutEndDate;
    @ApiModelProperty(value = "事业部id")
    private String buId;
    
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
	public String getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(String outDepotId) {
		this.outDepotId = outDepotId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
	public String getReturnOutStartDate() {
		return returnOutStartDate;
	}
	public void setReturnOutStartDate(String returnOutStartDate) {
		this.returnOutStartDate = returnOutStartDate;
	}
	public String getReturnOutEndDate() {
		return returnOutEndDate;
	}
	public void setReturnOutEndDate(String returnOutEndDate) {
		this.returnOutEndDate = returnOutEndDate;
	}
	public String getBuId() {
		return buId;
	}
	public void setBuId(String buId) {
		this.buId = buId;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}

