package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MerchandiseForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;

	 @ApiModelProperty(value = "单据id集合,多个用逗号隔开")
	 private String orderIds;

    @ApiModelProperty(value = "单据号，用于检索")
    private String orderCode;

    @ApiModelProperty(value = "商品货号")
    private String goodsNo;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "已经选择的商品")
    private String unNeedGoodsJson;

	@ApiModelProperty(value = "关联的订单号集合，多个用逗号隔开")
	private String orderCodes;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getUnNeedGoodsJson() {
		return unNeedGoodsJson;
	}

	public void setUnNeedGoodsJson(String unNeedGoodsJson) {
		this.unNeedGoodsJson = unNeedGoodsJson;
	}

	public String getOrderCodes() {
		return orderCodes;
	}

	public void setOrderCodes(String orderCodes) {
		this.orderCodes = orderCodes;
	}
}
