package com.topideal.order.webapi.purchase.form;

import com.topideal.entity.dto.purchase.PurchaseOrderDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PurchaseOrderPageForm {

	@ApiModelProperty("跳转页面入口：1-首页 2-订单列表页") 
	private String pageSource ;
	
	@ApiModelProperty("仓库类型") 
	private String depotType ;
	
	@ApiModelProperty("转采购类型，销售订单-sale、预售单-preSale、采购订单复制-purchaseCopy") 
	private String type ;
	
	@ApiModelProperty("采购商品已选择商品ID，多个以','隔开") 
	private String unNeedIds ;
	
	@ApiModelProperty("采购明细") 
	private PurchaseOrderDTO details ;

	public String getPageSource() {
		return pageSource;
	}

	public void setPageSource(String pageSource) {
		this.pageSource = pageSource;
	}

	public String getDepotType() {
		return depotType;
	}

	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnNeedIds() {
		return unNeedIds;
	}

	public void setUnNeedIds(String unNeedIds) {
		this.unNeedIds = unNeedIds;
	}

	public PurchaseOrderDTO getDetails() {
		return details;
	}

	public void setDetails(PurchaseOrderDTO details) {
		this.details = details;
	}
	
	
}
