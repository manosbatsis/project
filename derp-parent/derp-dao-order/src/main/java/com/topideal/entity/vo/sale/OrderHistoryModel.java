package com.topideal.entity.vo.sale;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class OrderHistoryModel extends PageModel implements Serializable {

	/**
	 * id
	 */
	private Long id;
	/**
	 * 订单号(自生成)
	 */
	private String code;
	/**
	 * 外部单号
	 */
	private String externalCode;
	/**
	 * 运单号
	 */
	private String wayBillNo;
	/**
	 * 交易时间
	 */
	private Timestamp tradingDate;
	/**
	 * 单据状态：1:待申报 2.待放行,8:待审核,3:待发货,4:已发货,5:已取消,027:出库中 006-已删除
	 */
	private String status;
	/**
	 * 仓库ID
	 */
	private Long depotId;
	/**
	 * 仓库名称
	 */
	private String depotName;
	/**
	 * 审核时间
	 */
	private Timestamp auditDate;
	/**
	 * 申报时间
	 */
	private Timestamp declareDate;
	/**
	 * 放行时间
	 */
	private Timestamp releaseDate;
	/**
	 * 发货时间
	 */
	private Timestamp deliverDate;
	/**
	 * 物流企业名称
	 */
	private String logisticsName;
	/**
	 * 进口模式10：BBC;20：BC;30：保留; 40：CC
	 */
	private String importMode;
	/**
	 * 订购人电话
	 */
	private String makerTel;
	/**
	 * 订购人
	 */
	private String makerName;
	/**
	 * 订购人注册号
	 */
	private String makerRegisterNo;
	/**
	 * 收件人名称
	 */
	private String receiverName;
	/**
	 * 收件人电话
	 */
	private String receiverTel;
	/**
	 * 收件人国家
	 */
	private String receiverCountry;
	/**
	 * 收件人省份
	 */
	private String receiverProvince;
	/**
	 * 收件人城市
	 */
	private String receiverCity;
	/**
	 * 收件人区
	 */
	private String receiverArea;
	/**
	 * 收货地址
	 */
	private String receiverAddress;
	/**
	 * 订单来源 1:跨境宝推送, 2:导入,3:第e仓 4: 订单100
	 */
	private Integer orderSource;
	/**
	 * 申报方案ID
	 */
	private Long declarePlan;
	/**
	 * 商家名称
	 */
	private String merchantName;
	/**
	 * 商家ID
	 */
	private Long merchantId;
	/**
	 * 国检状态 11：已报国检12：国检放行13：国检审核驳回14：国检抽检15：国检抽检未过
	 */
	private String inspectStatus;
	/**
	 * 海关状态21：已报海关22：海关单证放行23：报海关失败24：海关查验/转人工/挂起等25：海关单证审核不通过26：海关已接受申报，待货物运抵后处理41：海关货物查扣42：海关货物放行
	 */
	private String customsStatus;
	/**
	 * 电商平台编码：第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790
	 */
	private String storePlatformName;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建人
	 */
	private Long creater;
	/**
	 * 创建时间
	 */
	private Timestamp createDate;
	/**
	 * 运费，2位小数
	 */
	private BigDecimal wayFrtFee;
	/**
	 * 保税，2位小数
	 */
	private BigDecimal wayIndFee;
	/**
	 * 税费 单位 元
	 */
	private BigDecimal taxes;
	/**
	 * 货款 元
	 */
	private BigDecimal goodsAmount;
	/**
	 * 订单实付金额
	 */
	private BigDecimal payment;
	/**
	 * 优惠减免金额
	 */
	private BigDecimal discount;
	/**
	 * 制单时间
	 */
	private Timestamp makingTime;
	/**
	 * 修改时间
	 */
	private Timestamp modifyDate;
	/**
	 * 客户id
	 */
	private Long customerId;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 店铺编码
	 */
	private String shopCode;
	/**
	 * 店铺名称
	 */
	private String shopName;
	/**
	 * 总佣金
	 */
	private BigDecimal saleCom;
	/**
	 * 币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
	 */
	private String currency;
	/**
	 * 店铺类型值编码 001:POP; 002:一件代发
	 */
	private String shopTypeCode;
	/**
	 * 平台订单号
	 */
	private String exOrderId;
	/**
	 * 包裹重量
	 */
	private Double weight;

	// 理货单位 00-托盘 01-箱 02-件
	private String tallyingUnit;

	/**
	 * 是否生成暂估费用 0-未生成 1-已生成
	 */
	private String isGenerate;
	/**
	 * 内贸实付金额（不含税）
	 */
	private BigDecimal tradePayment;
	/**
	 * 内贸税额
	 */
	private BigDecimal tradePaymentTax;

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* externalCode get 方法 */
	public String getExternalCode() {
		return externalCode;
	}

	/* externalCode set 方法 */
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}

	/* wayBillNo get 方法 */
	public String getWayBillNo() {
		return wayBillNo;
	}

	/* wayBillNo set 方法 */
	public void setWayBillNo(String wayBillNo) {
		this.wayBillNo = wayBillNo;
	}

	/* tradingDate get 方法 */
	public Timestamp getTradingDate() {
		return tradingDate;
	}

	/* tradingDate set 方法 */
	public void setTradingDate(Timestamp tradingDate) {
		this.tradingDate = tradingDate;
	}

	/* status get 方法 */
	public String getStatus() {
		return status;
	}

	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
	}

	/* depotId get 方法 */
	public Long getDepotId() {
		return depotId;
	}

	/* depotId set 方法 */
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	/* depotName get 方法 */
	public String getDepotName() {
		return depotName;
	}

	/* depotName set 方法 */
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	/* auditDate get 方法 */
	public Timestamp getAuditDate() {
		return auditDate;
	}

	/* auditDate set 方法 */
	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	/* declareDate get 方法 */
	public Timestamp getDeclareDate() {
		return declareDate;
	}

	/* declareDate set 方法 */
	public void setDeclareDate(Timestamp declareDate) {
		this.declareDate = declareDate;
	}

	/* releaseDate get 方法 */
	public Timestamp getReleaseDate() {
		return releaseDate;
	}

	/* releaseDate set 方法 */
	public void setReleaseDate(Timestamp releaseDate) {
		this.releaseDate = releaseDate;
	}

	/* deliverDate get 方法 */
	public Timestamp getDeliverDate() {
		return deliverDate;
	}

	/* deliverDate set 方法 */
	public void setDeliverDate(Timestamp deliverDate) {
		this.deliverDate = deliverDate;
	}

	/* logisticsName get 方法 */
	public String getLogisticsName() {
		return logisticsName;
	}

	/* logisticsName set 方法 */
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	/* importMode get 方法 */
	public String getImportMode() {
		return importMode;
	}

	/* importMode set 方法 */
	public void setImportMode(String importMode) {
		this.importMode = importMode;
	}

	/* makerTel get 方法 */
	public String getMakerTel() {
		return makerTel;
	}

	/* makerTel set 方法 */
	public void setMakerTel(String makerTel) {
		this.makerTel = makerTel;
	}

	/* makerName get 方法 */
	public String getMakerName() {
		return makerName;
	}

	/* makerName set 方法 */
	public void setMakerName(String makerName) {
		this.makerName = makerName;
	}

	/* makerRegisterNo get 方法 */
	public String getMakerRegisterNo() {
		return makerRegisterNo;
	}

	/* makerRegisterNo set 方法 */
	public void setMakerRegisterNo(String makerRegisterNo) {
		this.makerRegisterNo = makerRegisterNo;
	}

	/* receiverName get 方法 */
	public String getReceiverName() {
		return receiverName;
	}

	/* receiverName set 方法 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	/* receiverTel get 方法 */
	public String getReceiverTel() {
		return receiverTel;
	}

	/* receiverTel set 方法 */
	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}

	/* receiverCountry get 方法 */
	public String getReceiverCountry() {
		return receiverCountry;
	}

	/* receiverCountry set 方法 */
	public void setReceiverCountry(String receiverCountry) {
		this.receiverCountry = receiverCountry;
	}

	/* receiverProvince get 方法 */
	public String getReceiverProvince() {
		return receiverProvince;
	}

	/* receiverProvince set 方法 */
	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}

	/* receiverCity get 方法 */
	public String getReceiverCity() {
		return receiverCity;
	}

	/* receiverCity set 方法 */
	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	/* receiverArea get 方法 */
	public String getReceiverArea() {
		return receiverArea;
	}

	/* receiverArea set 方法 */
	public void setReceiverArea(String receiverArea) {
		this.receiverArea = receiverArea;
	}

	/* receiverAddress get 方法 */
	public String getReceiverAddress() {
		return receiverAddress;
	}

	/* receiverAddress set 方法 */
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	/* orderSource get 方法 */
	public Integer getOrderSource() {
		return orderSource;
	}

	/* orderSource set 方法 */
	public void setOrderSource(Integer orderSource) {
		this.orderSource = orderSource;
	}

	/* declarePlan get 方法 */
	public Long getDeclarePlan() {
		return declarePlan;
	}

	/* declarePlan set 方法 */
	public void setDeclarePlan(Long declarePlan) {
		this.declarePlan = declarePlan;
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/* inspectStatus get 方法 */
	public String getInspectStatus() {
		return inspectStatus;
	}

	/* inspectStatus set 方法 */
	public void setInspectStatus(String inspectStatus) {
		this.inspectStatus = inspectStatus;
	}

	/* customsStatus get 方法 */
	public String getCustomsStatus() {
		return customsStatus;
	}

	/* customsStatus set 方法 */
	public void setCustomsStatus(String customsStatus) {
		this.customsStatus = customsStatus;
	}

	/* storePlatformName get 方法 */
	public String getStorePlatformName() {
		return storePlatformName;
	}

	/* storePlatformName set 方法 */
	public void setStorePlatformName(String storePlatformName) {
		this.storePlatformName = storePlatformName;
	}

	/* remark get 方法 */
	public String getRemark() {
		return remark;
	}

	/* remark set 方法 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* wayFrtFee get 方法 */
	public BigDecimal getWayFrtFee() {
		return wayFrtFee;
	}

	/* wayFrtFee set 方法 */
	public void setWayFrtFee(BigDecimal wayFrtFee) {
		this.wayFrtFee = wayFrtFee;
	}

	/* wayIndFee get 方法 */
	public BigDecimal getWayIndFee() {
		return wayIndFee;
	}

	/* wayIndFee set 方法 */
	public void setWayIndFee(BigDecimal wayIndFee) {
		this.wayIndFee = wayIndFee;
	}

	/* taxes get 方法 */
	public BigDecimal getTaxes() {
		return taxes;
	}

	/* taxes set 方法 */
	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	/* goodsAmount get 方法 */
	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}

	/* goodsAmount set 方法 */
	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	/* payment get 方法 */
	public BigDecimal getPayment() {
		return payment;
	}

	/* payment set 方法 */
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	/* discount get 方法 */
	public BigDecimal getDiscount() {
		return discount;
	}

	/* discount set 方法 */
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	/* makingTime get 方法 */
	public Timestamp getMakingTime() {
		return makingTime;
	}

	/* makingTime set 方法 */
	public void setMakingTime(Timestamp makingTime) {
		this.makingTime = makingTime;
	}

	/* modifyDate get 方法 */
	public Timestamp getModifyDate() {
		return modifyDate;
	}

	/* modifyDate set 方法 */
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	/* customerId get 方法 */
	public Long getCustomerId() {
		return customerId;
	}

	/* customerId set 方法 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/* customerName get 方法 */
	public String getCustomerName() {
		return customerName;
	}

	/* customerName set 方法 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/* shopCode get 方法 */
	public String getShopCode() {
		return shopCode;
	}

	/* shopCode set 方法 */
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	/* shopName get 方法 */
	public String getShopName() {
		return shopName;
	}

	/* shopName set 方法 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	/* saleCom get 方法 */
	public BigDecimal getSaleCom() {
		return saleCom;
	}

	/* saleCom set 方法 */
	public void setSaleCom(BigDecimal saleCom) {
		this.saleCom = saleCom;
	}

	/* currency get 方法 */
	public String getCurrency() {
		return currency;
	}

	/* currency set 方法 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/* shopTypeCode get 方法 */
	public String getShopTypeCode() {
		return shopTypeCode;
	}

	/* shopTypeCode set 方法 */
	public void setShopTypeCode(String shopTypeCode) {
		this.shopTypeCode = shopTypeCode;
	}

	/* exOrderId get 方法 */
	public String getExOrderId() {
		return exOrderId;
	}

	/* exOrderId set 方法 */
	public void setExOrderId(String exOrderId) {
		this.exOrderId = exOrderId;
	}

	/* weight get 方法 */
	public Double getWeight() {
		return weight;
	}

	/* weight set 方法 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	public String getIsGenerate() {
		return isGenerate;
	}

	public void setIsGenerate(String isGenerate) {
		this.isGenerate = isGenerate;
	}

	public BigDecimal getTradePayment() {
		return tradePayment;
	}

	public void setTradePayment(BigDecimal tradePayment) {
		this.tradePayment = tradePayment;
	}

	public BigDecimal getTradePaymentTax() {
		return tradePaymentTax;
	}

	public void setTradePaymentTax(BigDecimal tradePaymentTax) {
		this.tradePaymentTax = tradePaymentTax;
	}

}
