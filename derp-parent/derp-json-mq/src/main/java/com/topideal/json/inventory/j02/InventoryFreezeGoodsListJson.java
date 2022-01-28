package com.topideal.json.inventory.j02;

import java.io.Serializable;

/**
 * 库存冻结和解冻商品集合实体
 * @author 联想302
 *
 */
public class InventoryFreezeGoodsListJson implements Serializable{
                                  
	private String goodsId;//商品ID
	private String goodsName;//商品名称
	private String goodsNo;//商品货号
	private String divergenceDate;//出入时间
	private int num;//数量
	private String unit;//单位
	
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getDivergenceDate() {
		return divergenceDate;
	}
	public void setDivergenceDate(String divergenceDate) {
		this.divergenceDate = divergenceDate;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	
}
