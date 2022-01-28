package com.topideal.entity.dto.api10003;

import java.sql.Timestamp;
import java.util.List;

/**
 * 电商订单
 * 
 * @author Gy
 *
 */
public class DSOrder {
	// 订单编号
	private String code;
	// 外部订单号
	private String externalCode ;
	// 平台订单号
	private String exOrderId ;
	// 仓库名称
	private String depotName;
	// 状态
	private String status;
	// 平台名称
	private String storePlatformName ;
	// 店铺编码
	private String shopCode;
	// 店铺名称
	private String shopName ;
	// 币种
	private String currency;
	// 交易时间
	private Timestamp tradingDate;
	// 发货时间
	private Timestamp deliverDate;
	// 运费
	private Double wayFrtFee ;
	// 税费
	private Double taxes ;
	// 总优惠金额
	private Double discount ;
	// 总金额
	private Double payment ;
	// 商品
	private List<DSOrderGoods> goodsList;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getExternalCode() {
		return externalCode;
	}
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}
	public String getExOrderId() {
		return exOrderId;
	}
	public void setExOrderId(String exOrderId) {
		this.exOrderId = exOrderId;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStorePlatformName() {
		return storePlatformName;
	}
	public void setStorePlatformName(String storePlatformName) {
		this.storePlatformName = storePlatformName;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getCurrency() {
		return currency;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Timestamp getTradingDate() {
		return tradingDate;
	}
	public void setTradingDate(Timestamp tradingDate) {
		this.tradingDate = tradingDate;
	}
	public Timestamp getDeliverDate() {
		return deliverDate;
	}
	public void setDeliverDate(Timestamp deliverDate) {
		this.deliverDate = deliverDate;
	}
	public Double getWayFrtFee() {
		return wayFrtFee;
	}
	public void setWayFrtFee(Double wayFrtFee) {
		this.wayFrtFee = wayFrtFee;
	}
	public Double getTaxes() {
		return taxes;
	}
	public void setTaxes(Double taxes) {
		this.taxes = taxes;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getPayment() {
		return payment;
	}
	public void setPayment(Double payment) {
		this.payment = payment;
	}
	public List<DSOrderGoods> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<DSOrderGoods> goodsList) {
		this.goodsList = goodsList;
	}
	
	
}
