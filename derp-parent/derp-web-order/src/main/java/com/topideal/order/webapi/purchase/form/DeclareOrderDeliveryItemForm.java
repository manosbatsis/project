package com.topideal.order.webapi.purchase.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class DeclareOrderDeliveryItemForm {

	@ApiModelProperty("商品ID")
	private Long goodsId ;

	@ApiModelProperty("正常品量")
	private Integer normalNum ;

	@ApiModelProperty("过期数量")
	private Integer expireNum ;

	@ApiModelProperty("坏品数量")
	private Integer wornNum ;

	@ApiModelProperty("失效时间")
	private String overdueDate;

	@ApiModelProperty("批次号")
	private String batchNo;

	@ApiModelProperty("生产日期")
	private String productionDate;

	@ApiModelProperty("采购明细Id")
	private Long purchaseItemId;

	@ApiModelProperty("采购单Id")
	private Long purchaseId;

	@ApiModelProperty("条形码")
	private String barcode;


	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getNormalNum() {
		return normalNum;
	}

	public void setNormalNum(Integer normalNum) {
		this.normalNum = normalNum;
	}

	public Integer getExpireNum() {
		return expireNum;
	}

	public void setExpireNum(Integer expireNum) {
		this.expireNum = expireNum;
	}

	public Integer getWornNum() {
		return wornNum;
	}

	public void setWornNum(Integer wornNum) {
		this.wornNum = wornNum;
	}

	public String getOverdueDate() {
		return overdueDate;
	}

	public void setOverdueDate(String overdueDate) {
		this.overdueDate = overdueDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public Long getPurchaseItemId() {
		return purchaseItemId;
	}

	public void setPurchaseItemId(Long purchaseItemId) {
		this.purchaseItemId = purchaseItemId;
	}

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
