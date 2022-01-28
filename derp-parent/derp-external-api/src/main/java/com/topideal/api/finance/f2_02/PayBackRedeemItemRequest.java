package com.topideal.api.finance.f2_02;

import java.math.BigDecimal;

/**
 * 3.7企业还款赎回接收接口    表体  货品信息，可循环，非现金代采可空
 * @author 杨创
 * 2021-04-09
 */
public class PayBackRedeemItemRequest {
	private String goodsNo;//商品ID  必填
	private String goodsName;//商品名称 必填
	private Integer qty;//赎回数量  必填
	private String procurementNo;//融资申请单号 必填
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
