package com.topideal.json.pushapi.epass.e05;
/**
 * 推送盘点指令商品类
 * @author 杨创
 *2018/06/16
 */
public class InventoryInstructionGoodsItem {
	private String index;//序号
	private String goods_id;// 商品货号
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	
}
