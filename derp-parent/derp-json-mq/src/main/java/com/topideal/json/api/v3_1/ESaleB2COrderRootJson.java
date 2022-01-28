package com.topideal.json.api.v3_1;

import java.math.BigDecimal;
import java.util.List;

/**
 * B2C订单接MQ
 * 
 * @author lian_
 *
 */
public class ESaleB2COrderRootJson {

	private String externalCode; // 外部单号
	private String wayBillNo; // 运单号
	private Long merchantId; // 商家id
	private String merchantName; // 商家名称
	private Long depotId; // 仓库id
	private String depotName; // 仓库名称
	private String tradingDate; // 交易时间,格式:yyyy-MM-dd HH:mm:ss
	private String logisticsName; // 物流企业编码
	private String storePlatformName; // 电商平台编码
	private String makerRegisterNo; // 订购人注册号, 订购人在电商平台唯一注册号
	private String makerName; // 订购人姓名
	private String makerTel; // 订购人电话
	private String receiverName; // 收货人姓名
	private String receiverTel; // 收件人电话
	private String receiverCountry; // 收货人国家
	private String receiverProvince; // 收货人省份
	private String receiverCity; // 收货人市
	private String receiverArea; // 收货人市区县
	private String receiverAddress; // 收货人地址
	private BigDecimal wayFrtFee; // 运费，2位小数
	private BigDecimal wayIndFee; // 保税,2位小数
	private BigDecimal taxes; // 税费,2位小数
	private BigDecimal discount; // 优惠减免金额
	private BigDecimal goodsAmount; // 货款
	private BigDecimal payment; // 订单实付金额
	private String remark; // 备注
    private String shopName;  // 店铺名称
    private String shopCode; // 店铺编码
    private String shopTypeCode; // 店铺类型编码
    private String exOrderId; // 平台订单号
	
	
	

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	private List<ESaleB2COrderGoodsListJson> orderGoods;

	public List<ESaleB2COrderGoodsListJson> getOrderGoods() {
		return orderGoods;
	}

	public void setOrderGoods(List<ESaleB2COrderGoodsListJson> orderGoods) {
		this.orderGoods = orderGoods;
	}

	public String getExternalCode() {
		return externalCode;
	}

	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}

	public String getWayBillNo() {
		return wayBillNo;
	}

	public void setWayBillNo(String wayBillNo) {
		this.wayBillNo = wayBillNo;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Long getDepotId() {
		return depotId;
	}

	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public String getTradingDate() {
		return tradingDate;
	}

	public void setTradingDate(String tradingDate) {
		this.tradingDate = tradingDate;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public String getStorePlatformName() {
		return storePlatformName;
	}

	public void setStorePlatformName(String storePlatformName) {
		this.storePlatformName = storePlatformName;
	}

	public String getMakerRegisterNo() {
		return makerRegisterNo;
	}

	public void setMakerRegisterNo(String makerRegisterNo) {
		this.makerRegisterNo = makerRegisterNo;
	}

	public String getMakerName() {
		return makerName;
	}

	public void setMakerName(String makerName) {
		this.makerName = makerName;
	}

	public String getMakerTel() {
		return makerTel;
	}

	public void setMakerTel(String makerTel) {
		this.makerTel = makerTel;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverTel() {
		return receiverTel;
	}

	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}

	public String getReceiverCountry() {
		return receiverCountry;
	}

	public void setReceiverCountry(String receiverCountry) {
		this.receiverCountry = receiverCountry;
	}

	public String getReceiverProvince() {
		return receiverProvince;
	}

	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	public String getReceiverArea() {
		return receiverArea;
	}

	public void setReceiverArea(String receiverArea) {
		this.receiverArea = receiverArea;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public BigDecimal getWayFrtFee() {
		return wayFrtFee;
	}

	public void setWayFrtFee(BigDecimal wayFrtFee) {
		this.wayFrtFee = wayFrtFee;
	}

	public BigDecimal getWayIndFee() {
		return wayIndFee;
	}

	public void setWayIndFee(BigDecimal wayIndFee) {
		this.wayIndFee = wayIndFee;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExOrderId() {
		return exOrderId;
	}

	public void setExOrderId(String exOrderId) {
		this.exOrderId = exOrderId;
	}

	public String getShopTypeCode() {
		return shopTypeCode;
	}

	public void setShopTypeCode(String shopTypeCode) {
		this.shopTypeCode = shopTypeCode;
	}

}
