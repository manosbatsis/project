package com.topideal.api.finance.f01;

/**
 * 商品分类查询接口请求表体
 * @author 杨创
 * 2020-01-07
 */
public class GoodsClassificationItemRequest {
	private String goodsBarcode; // 商品条码

	public String getGoodsBarcode() {
		return goodsBarcode;
	}

	public void setGoodsBarcode(String goodsBarcode) {
		this.goodsBarcode = goodsBarcode;
	}
}
