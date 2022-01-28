package com.topideal.entity.vo.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class OrderReturnIdepotModel extends PageModel implements Serializable {

	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 订单号(自生成)
	 */
	private String code;
	/**
	 * 来源交易订单号(电商订单的外部单号)
	 */
	private String externalCode;
	/**
	 * 平台售后单号
	 */
	private String returnCode;
	/**
	 * 仓库入库单号
	 */
	private String inDepotCode;
	/**
	 * 原仓库发货单号
	 */
	private String originalDeliveryCode;
	/**
	 * 退货单类型
	 */
	private String returnType;
	/**
	 * 退入仓库id
	 */
	private Long returnInDepotId;
	/**
	 * 退入仓库名称
	 */
	private String returnInDepotName;
	/**
	 * 退货入库仓库自编码
	 */
	private String returnInDepotCode;
	/**
	 * 退货创建时间
	 */
	private Timestamp returnInCreateDate;
	/**
	 * 退货入库时间
	 */
	private Timestamp returnInDate;
	/**
	 * 商品退款总金额
	 */
	private BigDecimal amount;
	/**
	 * 币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
	 */
	private String currency;
	/**
	 * 电商平台编码：第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790
	 */
	private String storePlatformCode;
	/**
	 * 买家姓名
	 */
	private String buyerName;
	/**
	 * 买家手机
	 */
	private String buyerPhone;
	/**
	 * 退货人地址
	 */
	private String returnAddr;
	/**
	 * 邮编
	 */
	private String postcode;
	/**
	 * 物流公司
	 */
	private String logisticsName;
	/**
	 * 物流运单号
	 */
	private String logisticsWayBillNo;
	/**
	 * 店铺编码
	 */
	private String shopCode;
	/**
	 * 店铺名称
	 */
	private String shopName;
	/**
	 * 订单来源 1:导入 2.接口数据
	 */
	private Integer orderReturnSource;
	/**
	 * 商家名称
	 */
	private String merchantName;
	/**
	 * 商家ID
	 */
	private Long merchantId;
	/**
	 * 单据状态：011:待入仓 012.已入仓 028.入仓中 006-已删除
	 */
	private String status;
	/**
	 * 创建人ID
	 */
	private Long creater;
	/**
	 * 创建人名称
	 */
	private String createName;
	/**
	 * 创建时间
	 */
	private Timestamp createDate;
	/**
	 * 修改人ID
	 */
	private Long modifier;
	/**
	 * 修改人名称
	 */
	private String modifierName;
	/**
	 * 修改时间
	 */
	private Timestamp modifyDate;
	/**
	 * 审核人ID
	 */
	private Long auditor;
	/**
	 * 审核人名称
	 */
	private String auditName;
	/**
	 * 审核人时间
	 */
	private Timestamp auditDate;
	/**
	 * 店铺类型值编码
	 */
	private String shopTypeCode;
	/**
	 * 客户id
	 */
	private Long customerId;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 退款完结时间
	 */
	private Timestamp refundEndDate;
	/**
	 * 售后类型 00-仅退款 01-退货退款
	 */
	private String afterSaleType;
	/**
	 * 退款金额
	 */
	private BigDecimal totalRefundAmount;
	/**
	 * 是否退货单 0-否，退款单 1-是，退货单
	 */
	private String returnOrderType;
	/**
	 * 内贸退款金额（不含税）
	 */
	private BigDecimal tradeRefundAmount;
	/**
	 * 内贸税额
	 */
	private BigDecimal tradeRefundTax;

	/**
	 * 关联电商订单id
	 */
	private Long orderId;
	/**
	 * 关联电商订单号
	 */
	private String orderCode;

	/**
	 * 退款原因
	 */
	private String refundRemark;
	/**
    * 是否生成暂估费用 0-未生成 1-已生成
    */
    private String isGenerate;
	/**
	 * 理货单位 00-托盘 01-箱 02-件
	 */
	private String tallyingUnit;

	public String getRefundRemark() {
		return refundRemark;
	}

	public void setRefundRemark(String refundRemark) {
		this.refundRemark = refundRemark;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

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

	/* returnCode get 方法 */
	public String getReturnCode() {
		return returnCode;
	}

	/* returnCode set 方法 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/* inDepotCode get 方法 */
	public String getInDepotCode() {
		return inDepotCode;
	}

	/* inDepotCode set 方法 */
	public void setInDepotCode(String inDepotCode) {
		this.inDepotCode = inDepotCode;
	}

	/* originalDeliveryCode get 方法 */
	public String getOriginalDeliveryCode() {
		return originalDeliveryCode;
	}

	/* originalDeliveryCode set 方法 */
	public void setOriginalDeliveryCode(String originalDeliveryCode) {
		this.originalDeliveryCode = originalDeliveryCode;
	}

	/* returnType get 方法 */
	public String getReturnType() {
		return returnType;
	}

	/* returnType set 方法 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/* returnInDepotId get 方法 */
	public Long getReturnInDepotId() {
		return returnInDepotId;
	}

	/* returnInDepotId set 方法 */
	public void setReturnInDepotId(Long returnInDepotId) {
		this.returnInDepotId = returnInDepotId;
	}

	/* returnInDepotName get 方法 */
	public String getReturnInDepotName() {
		return returnInDepotName;
	}

	/* returnInDepotName set 方法 */
	public void setReturnInDepotName(String returnInDepotName) {
		this.returnInDepotName = returnInDepotName;
	}

	/* returnInDepotCode get 方法 */
	public String getReturnInDepotCode() {
		return returnInDepotCode;
	}

	/* returnInDepotCode set 方法 */
	public void setReturnInDepotCode(String returnInDepotCode) {
		this.returnInDepotCode = returnInDepotCode;
	}

	/* returnInCreateDate get 方法 */
	public Timestamp getReturnInCreateDate() {
		return returnInCreateDate;
	}

	/* returnInCreateDate set 方法 */
	public void setReturnInCreateDate(Timestamp returnInCreateDate) {
		this.returnInCreateDate = returnInCreateDate;
	}

	/* returnInDate get 方法 */
	public Timestamp getReturnInDate() {
		return returnInDate;
	}

	/* returnInDate set 方法 */
	public void setReturnInDate(Timestamp returnInDate) {
		this.returnInDate = returnInDate;
	}

	/* amount get 方法 */
	public BigDecimal getAmount() {
		return amount;
	}

	/* amount set 方法 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/* currency get 方法 */
	public String getCurrency() {
		return currency;
	}

	/* currency set 方法 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/* storePlatformCode get 方法 */
	public String getStorePlatformCode() {
		return storePlatformCode;
	}

	/* storePlatformCode set 方法 */
	public void setStorePlatformCode(String storePlatformCode) {
		this.storePlatformCode = storePlatformCode;
	}

	/* buyerName get 方法 */
	public String getBuyerName() {
		return buyerName;
	}

	/* buyerName set 方法 */
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	/* buyerPhone get 方法 */
	public String getBuyerPhone() {
		return buyerPhone;
	}

	/* buyerPhone set 方法 */
	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}

	/* returnAddr get 方法 */
	public String getReturnAddr() {
		return returnAddr;
	}

	/* returnAddr set 方法 */
	public void setReturnAddr(String returnAddr) {
		this.returnAddr = returnAddr;
	}

	/* postcode get 方法 */
	public String getPostcode() {
		return postcode;
	}

	/* postcode set 方法 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	/* logisticsName get 方法 */
	public String getLogisticsName() {
		return logisticsName;
	}

	/* logisticsName set 方法 */
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	/* logisticsWayBillNo get 方法 */
	public String getLogisticsWayBillNo() {
		return logisticsWayBillNo;
	}

	/* logisticsWayBillNo set 方法 */
	public void setLogisticsWayBillNo(String logisticsWayBillNo) {
		this.logisticsWayBillNo = logisticsWayBillNo;
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

	/* orderReturnSource get 方法 */
	public Integer getOrderReturnSource() {
		return orderReturnSource;
	}

	/* orderReturnSource set 方法 */
	public void setOrderReturnSource(Integer orderReturnSource) {
		this.orderReturnSource = orderReturnSource;
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

	/* status get 方法 */
	public String getStatus() {
		return status;
	}

	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* createName get 方法 */
	public String getCreateName() {
		return createName;
	}

	/* createName set 方法 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* modifier get 方法 */
	public Long getModifier() {
		return modifier;
	}

	/* modifier set 方法 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	/* modifierName get 方法 */
	public String getModifierName() {
		return modifierName;
	}

	/* modifierName set 方法 */
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	/* modifyDate get 方法 */
	public Timestamp getModifyDate() {
		return modifyDate;
	}

	/* modifyDate set 方法 */
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	/* auditor get 方法 */
	public Long getAuditor() {
		return auditor;
	}

	/* auditor set 方法 */
	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}

	/* auditName get 方法 */
	public String getAuditName() {
		return auditName;
	}

	/* auditName set 方法 */
	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	/* auditDate get 方法 */
	public Timestamp getAuditDate() {
		return auditDate;
	}

	/* auditDate set 方法 */
	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	/* shopTypeCode get 方法 */
	public String getShopTypeCode() {
		return shopTypeCode;
	}

	/* shopTypeCode set 方法 */
	public void setShopTypeCode(String shopTypeCode) {
		this.shopTypeCode = shopTypeCode;
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

	/* refundEndDate get 方法 */
	public Timestamp getRefundEndDate() {
		return refundEndDate;
	}

	/* refundEndDate set 方法 */
	public void setRefundEndDate(Timestamp refundEndDate) {
		this.refundEndDate = refundEndDate;
	}

	/* afterSaleType get 方法 */
	public String getAfterSaleType() {
		return afterSaleType;
	}

	/* afterSaleType set 方法 */
	public void setAfterSaleType(String afterSaleType) {
		this.afterSaleType = afterSaleType;
	}

	/* totalRefundAmount get 方法 */
	public BigDecimal getTotalRefundAmount() {
		return totalRefundAmount;
	}

	/* totalRefundAmount set 方法 */
	public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

	/* returnOrderType get 方法 */
	public String getReturnOrderType() {
		return returnOrderType;
	}

	/* returnOrderType set 方法 */
	public void setReturnOrderType(String returnOrderType) {
		this.returnOrderType = returnOrderType;
	}

	public BigDecimal getTradeRefundAmount() {
		return tradeRefundAmount;
	}

	public void setTradeRefundAmount(BigDecimal tradeRefundAmount) {
		this.tradeRefundAmount = tradeRefundAmount;
	}

	public BigDecimal getTradeRefundTax() {
		return tradeRefundTax;
	}

	public void setTradeRefundTax(BigDecimal tradeRefundTax) {
		this.tradeRefundTax = tradeRefundTax;
	}

	public String getIsGenerate() {
		return isGenerate;
	}

	public void setIsGenerate(String isGenerate) {
		this.isGenerate = isGenerate;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}
}
