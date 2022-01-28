package com.topideal.entity.dto;

/**
 * 盘点指令商品
 * */
public class InstructGood {
	
	private Integer index;//序号	
	private String goods_id;//商品货号或海关备案号	
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	

}
