package com.topideal.json.api.v3_1;

import java.math.BigDecimal;

/**
 * B2C订单接MQ 商品推mq报文
 * 
 * @author lian_
 *
 */
public class ESaleB2COrderGoodsListJson {

	private Long goodsId; // 商品id
	private String goodsName; // 商品名称
	private Integer num; // 商品数量
	private BigDecimal decTotal; // 结算总金额
	private BigDecimal price; // 结算单价，浮点数，double，2位小数
	private String barcode; // 货品条码
	private String goodsCode; // 商品编码
	private String goodsNo; // 商品货号
	private BigDecimal originalPrice; //销售单价
	private BigDecimal originalDecTotal; //销售总金额


	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public BigDecimal getDecTotal() {
		return decTotal;
	}

	public void setDecTotal(BigDecimal decTotal) {
		this.decTotal = decTotal;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getOriginalDecTotal() {
		return originalDecTotal;
	}

	public void setOriginalDecTotal(BigDecimal originalDecTotal) {
		this.originalDecTotal = originalDecTotal;
	}
}
