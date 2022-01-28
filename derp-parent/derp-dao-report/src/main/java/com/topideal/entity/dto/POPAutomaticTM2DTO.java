package com.topideal.entity.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 平台为天猫（天猫出仓仓库为天运二期仓）自动校验DTO
 *
 */
public class POPAutomaticTM2DTO implements Serializable {
	// 店铺名称
	private String shopName;
	// 仓库名称
	private String depotName;
	// 下单时间
	private Timestamp makingTime;
	// 支付时间
	private Timestamp payTime;
	// 发货时间
	private Timestamp deliverDate;
	// 订单状态
	private String status;
	// 物流订单号
	private String logisticsNo;
	// 交易订单号
	private String tradeOrderNo;
	// 仓库订单号
	private String depotOrderNo;
	// 配送公司
	private String shippingCompany;
	// 配送运单号
	private String waybillNo;
	// 买家昵称
	private String buyerName;
	// 收件人名字
	private String receiverName;
	// 省
	private String province;
	// 市
	private String city;
	// 区
	private String area;
	// 街道
	private String street;
	// 手机
	private String phone;
	// 订单金额
	private BigDecimal orderPrice;
	// 运费
	private BigDecimal wayFrtFee;
	// 商品数量
	private Integer goodsNum;
	// 商品货号（货品id）
	private String goodsNo;
	// 货品编码
	private String goodsCode;
	// 商品id
	private String goodsId;
	// 商家编码
	private String merchantsCode;
	// 商品名称
	private String goodsName;
	// 税费
	private BigDecimal taxFee;
	// 商品包税金额
	private BigDecimal goodsTaxFee;
	// 是否异常
	private String isException;
	// 异常原因
	private String excetionResult;
	// 映射SKU_ID
	private String skuId;

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public Timestamp getMakingTime() {
		return makingTime;
	}

	public void setMakingTime(Timestamp makingTime) {
		this.makingTime = makingTime;
	}

	public Timestamp getPayTime() {
		return payTime;
	}

	public void setPayTime(Timestamp payTime) {
		this.payTime = payTime;
	}

	public Timestamp getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Timestamp deliverDate) {
		this.deliverDate = deliverDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public String getTradeOrderNo() {
		return tradeOrderNo;
	}

	public void setTradeOrderNo(String tradeOrderNo) {
		this.tradeOrderNo = tradeOrderNo;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getDepotOrderNo() {
		return depotOrderNo;
	}

	public void setDepotOrderNo(String depotOrderNo) {
		this.depotOrderNo = depotOrderNo;
	}

	public String getShippingCompany() {
		return shippingCompany;
	}

	public void setShippingCompany(String shippingCompany) {
		this.shippingCompany = shippingCompany;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public BigDecimal getWayFrtFee() {
		return wayFrtFee;
	}

	public void setWayFrtFee(BigDecimal wayFrtFee) {
		this.wayFrtFee = wayFrtFee;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getMerchantsCode() {
		return merchantsCode;
	}

	public void setMerchantsCode(String merchantsCode) {
		this.merchantsCode = merchantsCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public BigDecimal getTaxFee() {
		return taxFee;
	}

	public void setTaxFee(BigDecimal taxFee) {
		this.taxFee = taxFee;
	}

	public BigDecimal getGoodsTaxFee() {
		return goodsTaxFee;
	}

	public void setGoodsTaxFee(BigDecimal goodsTaxFee) {
		this.goodsTaxFee = goodsTaxFee;
	}

	public String getIsException() {
		return isException;
	}

	public void setIsException(String isException) {
		this.isException = isException;
	}

	public String getExcetionResult() {
		return excetionResult;
	}

	public void setExcetionResult(String excetionResult) {
		this.excetionResult = excetionResult;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	
}
