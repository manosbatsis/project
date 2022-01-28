package com.topideal.order.webapi.purchase.form;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class PurchaseInvoiceForm extends PageForm implements Serializable{

    /**
    * 采购订单号
    */
	@ApiModelProperty("采购订单号")
    private String purchaseOrderCode;
    /**
    * 事业部ID
    */
	@ApiModelProperty("事业部ID")
    private Long buId;
    /**
    * 供应商ID
    */
	@ApiModelProperty("供应商ID")
    private Long supplierId;
    /**
    * PO号
    */
	@ApiModelProperty("PO号")
    private String poNo;
	/**
    * 发票号
    */
	@ApiModelProperty("发票号")
    private String invoiceNo;
    /**
    * 付款人
    */
	@ApiModelProperty("开票人")
    private String payName;
	/**
    * 票据
    */
	@ApiModelProperty("票据")
    private String token;
	
	
	public String getPurchaseOrderCode() {
		return purchaseOrderCode;
	}
	public void setPurchaseOrderCode(String purchaseOrderCode) {
		this.purchaseOrderCode = purchaseOrderCode;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

    



}
