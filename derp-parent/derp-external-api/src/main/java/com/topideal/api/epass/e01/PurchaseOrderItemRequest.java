package com.topideal.api.epass.e01;

/**
 * 采购订单表体 推送报文
 * @author zhanghx
 * */
public class PurchaseOrderItemRequest {

	// 商品编码
	private String goods_id;
	// 商品名称
	private String goods_name;
	// 数量，整数
	private String amount;
	// 单价
	private String unit_price;

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}

}
