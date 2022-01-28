package com.topideal.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 成本单价差异Form
 */
public class CostPriceDifferenceForm extends PageForm implements Serializable{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;

	@ApiModelProperty(value = "公司ID")
	private Long merchantId;

	@ApiModelProperty(value = "事业部ID")
	private Long buId;


	@ApiModelProperty(value = "商品条形码")
	private String barcode;

	@ApiModelProperty(value = "商品名称")
	private String goodsName;

	@ApiModelProperty(value = "库位类型id")
	private Long stockLocationTypeId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Long getStockLocationTypeId() {
		return stockLocationTypeId;
	}

	public void setStockLocationTypeId(Long stockLocationTypeId) {
		this.stockLocationTypeId = stockLocationTypeId;
	}
}
