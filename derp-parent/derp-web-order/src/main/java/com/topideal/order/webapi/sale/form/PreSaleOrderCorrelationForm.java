package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PreSaleOrderCorrelationForm  extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token; 	
    
	@ApiModelProperty(value = "预售单号")
    private String preSaleOrderCode;    
	
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;
    
	@ApiModelProperty(value = "条形码")
    private String barcode;
	
	@ApiModelProperty(value = "审核开始时间")
    private String auditStartDate;

	@ApiModelProperty(value = "审核结束时间")
    private String auditEndDate;

	@ApiModelProperty(value = "出库开始时间")
    private String outDepotStartDate;

	@ApiModelProperty(value = "出库结束时间")
    private String outDepotEndDate;
  
	@ApiModelProperty(value = "客户ID")
    private String customerId;
	@ApiModelProperty(value = "出仓仓库ID")
    private String outDepotId;
	
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

	public String getPreSaleOrderCode() {
		return preSaleOrderCode;
	}

	public void setPreSaleOrderCode(String preSaleOrderCode) {
		this.preSaleOrderCode = preSaleOrderCode;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getAuditStartDate() {
		return auditStartDate;
	}

	public void setAuditStartDate(String auditStartDate) {
		this.auditStartDate = auditStartDate;
	}

	public String getAuditEndDate() {
		return auditEndDate;
	}

	public void setAuditEndDate(String auditEndDate) {
		this.auditEndDate = auditEndDate;
	}

	public String getOutDepotStartDate() {
		return outDepotStartDate;
	}

	public void setOutDepotStartDate(String outDepotStartDate) {
		this.outDepotStartDate = outDepotStartDate;
	}

	public String getOutDepotEndDate() {
		return outDepotEndDate;
	}

	public void setOutDepotEndDate(String outDepotEndDate) {
		this.outDepotEndDate = outDepotEndDate;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(String outDepotId) {
		this.outDepotId = outDepotId;
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
