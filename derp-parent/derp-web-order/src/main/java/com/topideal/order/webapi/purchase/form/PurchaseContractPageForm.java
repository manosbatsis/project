package com.topideal.order.webapi.purchase.form;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="采购合同通用返回对象，合同类型分为一般合同、美赞、宝洁、拜耳")
public class PurchaseContractPageForm {

	@ApiModelProperty(value="合同详情") 
	private JSONObject detail ;
	
	@ApiModelProperty(value="合同商品明细") 
	private JSONArray goodsList ;
	
	@ApiModelProperty(value="采购订单ID") 
	private Long purchaseId ;
	
	@ApiModelProperty(value="总金额,一般合同模版有值") 
	private BigDecimal totalAccount ;
	
	@ApiModelProperty(value="总数量,一般合同模版有值") 
	private Integer total ;
	
	@ApiModelProperty(value="币种,一般合同模版有值") 
	private String currencyLabel ;

	@ApiModelProperty(value="合同类型 1-拜耳 2-宝洁  3-美赞 4-一般")
	private String contractType ;

	public JSONObject getDetail() {
		return detail;
	}

	public void setDetail(JSONObject detail) {
		this.detail = detail;
	}

	public JSONArray getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(JSONArray goodsList) {
		this.goodsList = goodsList;
	}

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public BigDecimal getTotalAccount() {
		return totalAccount;
	}

	public void setTotalAccount(BigDecimal totalAccount) {
		this.totalAccount = totalAccount;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
}
