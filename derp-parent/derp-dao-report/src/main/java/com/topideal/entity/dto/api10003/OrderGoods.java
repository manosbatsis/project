package com.topideal.entity.dto.api10003;

import java.io.Serializable;

/**
 * 采购、销售表体
 * @author Gy
 *
 */
public class OrderGoods implements Serializable{
   
	//商品货号
	private String goodsNo ;
	//商品名称
	private String goodsName ;
	//条形码
	private String barcode ;
	//数量
	private Integer num ;
	//单价
	private Double price ;
	//金额
	private Double amount ;
	
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
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
}
