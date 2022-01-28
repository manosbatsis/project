package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class OrderReturnIdepotForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token; 
	
	@ApiModelProperty(value = "订单号(自生成)")
    private String code;
   
	@ApiModelProperty(value = "来源交易订单号(电商订单的外部单号)")
    private String externalCode;
	
	@ApiModelProperty(value = "仓库入库单号")
    private String inDepotCode;
	
	@ApiModelProperty(value = "退货单号")
    private String returnCode;

	@ApiModelProperty(value = "退入仓库id")
    private String returnInDepotId;
 
	@ApiModelProperty(value = "电商平台编码：第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790")
    private String storePlatformCode;
	
	@ApiModelProperty(value = "订单来源  1:导入 2.接口数据")
    private String orderReturnSource;
   
    @ApiModelProperty(value = "单据状态：011:待入仓 012:已入仓 028.入仓中")
    private String status;
    
    @ApiModelProperty(value = "退货时间开始")
 	private String returnInCreateStartDate;
 	 
    @ApiModelProperty(value = "退货时间结束")
 	private String returnInCreateEndDate; 	
    
    @ApiModelProperty(value = "入库时间开始")
 	private String returnInStartDate; 
    
    @ApiModelProperty(value = "入库时间结束")
 	private String returnInEndDate;
    
   	@ApiModelProperty(value = "主键集合ids")
   	private String ids;
   	@ApiModelProperty(value = "售后类型 00-仅退款 01-退货退款")
	private String afterSaleType;
   	 
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

	public String getExternalCode() {
		return externalCode;
	}

	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}

	public String getInDepotCode() {
		return inDepotCode;
	}

	public void setInDepotCode(String inDepotCode) {
		this.inDepotCode = inDepotCode;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnInDepotId() {
		return returnInDepotId;
	}

	public void setReturnInDepotId(String returnInDepotId) {
		this.returnInDepotId = returnInDepotId;
	}

	public String getStorePlatformCode() {
		return storePlatformCode;
	}

	public void setStorePlatformCode(String storePlatformCode) {
		this.storePlatformCode = storePlatformCode;
	}

	public String getOrderReturnSource() {
		return orderReturnSource;
	}

	public void setOrderReturnSource(String orderReturnSource) {
		this.orderReturnSource = orderReturnSource;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReturnInCreateStartDate() {
		return returnInCreateStartDate;
	}

	public void setReturnInCreateStartDate(String returnInCreateStartDate) {
		this.returnInCreateStartDate = returnInCreateStartDate;
	}

	public String getReturnInCreateEndDate() {
		return returnInCreateEndDate;
	}

	public void setReturnInCreateEndDate(String returnInCreateEndDate) {
		this.returnInCreateEndDate = returnInCreateEndDate;
	}

	public String getReturnInStartDate() {
		return returnInStartDate;
	}

	public void setReturnInStartDate(String returnInStartDate) {
		this.returnInStartDate = returnInStartDate;
	}

	public String getReturnInEndDate() {
		return returnInEndDate;
	}

	public void setReturnInEndDate(String returnInEndDate) {
		this.returnInEndDate = returnInEndDate;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getAfterSaleType() {
		return afterSaleType;
	}

	public void setAfterSaleType(String afterSaleType) {
		this.afterSaleType = afterSaleType;
	}
   
}
