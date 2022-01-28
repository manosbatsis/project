package com.topideal.api.finance.f2_03;

import java.math.BigDecimal;

/**
 * 3.16 还款试算 表体
 * @author 杨创
 * 2021-04-09
 */
public class PayBackTrialGoodRequest {
	private String goodsNo;//商品货号   非必填
	private String goodsName;//商品名称 非必填
	private Integer qty;//赎回数量 非必填
	private String procurementNo; //融资申请单号 非必填
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
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getProcurementNo() {
		return procurementNo;
	}
	public void setProcurementNo(String procurementNo) {
		this.procurementNo = procurementNo;
	}


	
	
	
	
}
