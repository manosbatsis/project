package com.topideal.entity.dto.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class OrderReturnIdepotDTO extends PageModel implements Serializable {

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "订单号(自生成)")
	private String code;

	@ApiModelProperty(value = "来源交易订单号(电商订单的外部单号)")
	private String externalCode;

	@ApiModelProperty(value = "退货单号")
	private String returnCode;

	@ApiModelProperty(value = "仓库入库单号")
	private String inDepotCode;

	@ApiModelProperty(value = "原仓库发货单号")
	private String originalDeliveryCode;

	@ApiModelProperty(value = "退货单类型")
	private String returnType;

	@ApiModelProperty(value = "退入仓库id")
	private Long returnInDepotId;

	@ApiModelProperty(value = "退入仓库名称")
	private String returnInDepotName;

	@ApiModelProperty(value = "退货入库仓库自编码")
	private String returnInDepotCode;

	@ApiModelProperty(value = "退货创建时间")
	private Timestamp returnInCreateDate;

	@ApiModelProperty(value = "退货入库时间")
	private Timestamp returnInDate;

	@ApiModelProperty(value = "商品退款总金额")
	private BigDecimal amount;

	@ApiModelProperty(value = "币种 01-人民币 02-日元 03-澳元 04-美元 05-港币 06-欧元  07-英镑")
	private String currency;

	@ApiModelProperty(value = "币种（中文）")
	private String currencyLabel;

	@ApiModelProperty(value = "电商平台编码：第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790")
	private String storePlatformCode;

	@ApiModelProperty(value = "电商平台编码（中文）")
	private String storePlatformCodeLabel;

	@ApiModelProperty(value = "买家姓名")
	private String buyerName;

	@ApiModelProperty(value = "买家手机")
	private String buyerPhone;

	@ApiModelProperty(value = "退货人地址")
	private String returnAddr;

	@ApiModelProperty(value = "邮编")
	private String postcode;

	@ApiModelProperty(value = "物流公司")
	private String logisticsName;

	@ApiModelProperty(value = "物流运单号")
	private String logisticsWayBillNo;

	@ApiModelProperty(value = "店铺编码")
	private String shopCode;

	@ApiModelProperty(value = "店铺名称")
	private String shopName;

	@ApiModelProperty(value = "订单来源  1:导入 2.接口数据")
	private Integer orderReturnSource;
	@ApiModelProperty(value = "订单来源（中文）")
	private String orderReturnSourceLabel;

	@ApiModelProperty(value = "商家名称")
	private String merchantName;

	@ApiModelProperty(value = "商家ID")
	private Long merchantId;

	@ApiModelProperty(value = "单据状态：011:待入仓 012:已入仓 028.入仓中")
	private String status;
	@ApiModelProperty(value = "单据状态（中文）")
	private String statusLabel;

	@ApiModelProperty(value = "创建人ID")
	private Long creater;

	@ApiModelProperty(value = "创建人名称")
	private String createName;

	@ApiModelProperty(value = "创建时间")
	private Timestamp createDate;

	@ApiModelProperty(value = "修改人ID")
	private Long modifier;

	@ApiModelProperty(value = "修改人名称")
	private String modifierName;

	@ApiModelProperty(value = "修改时间")
	private Timestamp modifyDate;

	@ApiModelProperty(value = "审核人ID")
	private Long auditor;

	@ApiModelProperty(value = "审核人名称")
	private String auditName;

	@ApiModelProperty(value = "审核人时间")
	private Timestamp auditDate;

	@ApiModelProperty(value = "表体数据")
	private List<OrderReturnIdepotItemDTO> itemList;
	// 扩展字段
	@ApiModelProperty(value = "电商平台名称")
	private String storePlatformName;
	//
	@ApiModelProperty(value = "退货时间开始", hidden = true)
	private String returnInCreateStartDate;
	//
	@ApiModelProperty(value = "退货时间结束", hidden = true)
	private String returnInCreateEndDate;

	@ApiModelProperty(value = "入库时间开始", hidden = true)
	private String returnInStartDate;

	@ApiModelProperty(value = "入库时间结束", hidden = true)
	private String returnInEndDate;

	@ApiModelProperty(value = "店铺类型值编码")
	private String shopTypeCode;

	@ApiModelProperty(value = "店铺类型值编码（中文）")
	private String shopTypeCodeLabel;

	@ApiModelProperty(value = "客户id")
	private Long customerId;

	@ApiModelProperty(value = "客户名称")
	private String customerName;

	@ApiModelProperty(value = "主键id集合")
	private String ids;

	/**
	 * 退款完结时间
	 */
	@ApiModelProperty(value = "退款完结时间")
	private Timestamp refundEndDate;
	/**
	 * 售后类型 00-仅退款 01-退货退款
	 */
	@ApiModelProperty(value = "售后类型 00-仅退款 01-退货退款")
	private String afterSaleType;
	@ApiModelProperty(value = "售后类型(中文)")
	private String afterSaleTypeLabel;

	@ApiModelProperty(value = "退款金额")
	private BigDecimal totalRefundAmount;

	/**
	 *
	 */
	@ApiModelProperty(value = "售后退货单类型 0-退款单 1-退货单")
	private String returnOrderType;
	@ApiModelProperty(value = "外部订单号集合")
	List<String> externalCodeList;

	/**
	 * 内贸退款金额（不含税）
	 */
	@ApiModelProperty(value = "内贸退款金额（不含税）")
	private BigDecimal tradeRefundAmount;
	/**
	 * 内贸税额
	 */
	@ApiModelProperty(value = "内贸税额")
	private BigDecimal tradeRefundTax;

	/**
	 * 关联电商订单id
	 */
	@ApiModelProperty(value = "关联电商订单id")
	private Long orderId;
	/**
	 * 关联电商订单号
	 */
	@ApiModelProperty(value = "关联电商订单号")
	private String orderCode;

	@ApiModelProperty(hidden = true)
	private String month;

	/**
	 * 退款原因
	 */
	@ApiModelProperty(value = "退款原因")
	private String refundRemark;

	/**
    * 是否生成暂估费用 0-未生成 1-已生成
    */
	@ApiModelProperty(value = "是否生成暂估费用 0-未生成 1-已生成")
    private String isGenerate;

	@ApiModelProperty(value = "平台售后单号集合")
	private List<String> returnCodeList;
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
		if (StringUtils.isNotBlank(currency)) {
			this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
		}
	}

	/* storePlatformCode get 方法 */
	public String getStorePlatformCode() {
		return storePlatformCode;
	}

	/* storePlatformCode set 方法 */
	public void setStorePlatformCode(String storePlatformCode) {
		this.storePlatformCode = storePlatformCode;
		this.storePlatformCodeLabel = DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode);
		this.storePlatformName = DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode);
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
		if (orderReturnSource != null) {
			this.orderReturnSourceLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_returnSourceList,
					String.valueOf(orderReturnSource));
		}
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
		if (StringUtils.isNotBlank(status)) {
			this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_returnStatusList, status);
		}
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

	public List<OrderReturnIdepotItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<OrderReturnIdepotItemDTO> itemList) {
		this.itemList = itemList;
	}

	public String getReturnInCreateStartDate() {
		return returnInCreateStartDate;
	}

	public void setReturnInCreateStartDate(String returnInCreateStartDate) {
		this.returnInCreateStartDate = returnInCreateStartDate;
	}

	public String getReturnInCreateEndDate() {
		return returnInCreateEndDate;
	}

	public void setReturnInCreateEndDate(String returnInCreateEndDate) {
		this.returnInCreateEndDate = returnInCreateEndDate;
	}

	public String getReturnInStartDate() {
		return returnInStartDate;
	}

	public void setReturnInStartDate(String returnInStartDate) {
		this.returnInStartDate = returnInStartDate;
	}

	public String getReturnInEndDate() {
		return returnInEndDate;
	}

	public void setReturnInEndDate(String returnInEndDate) {
		this.returnInEndDate = returnInEndDate;
	}

	public String getStorePlatformName() {
		return storePlatformName;
	}

	public void setStorePlatformName(String storePlatformName) {
		this.storePlatformName = storePlatformName;
	}

	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}

	public String getStorePlatformCodeLabel() {
		return storePlatformCodeLabel;
	}

	public void setStorePlatformCodeLabel(String storePlatformCodeLabel) {
		this.storePlatformCodeLabel = storePlatformCodeLabel;
	}

	public String getOrderReturnSourceLabel() {
		return orderReturnSourceLabel;
	}

	public void setOrderReturnSourceLabel(String orderReturnSourceLabel) {
		this.orderReturnSourceLabel = orderReturnSourceLabel;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public String getShopTypeCode() {
		return shopTypeCode;
	}

	public void setShopTypeCode(String shopTypeCode) {
		this.shopTypeCode = shopTypeCode;
		this.shopTypeCodeLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);
	}

	public String getShopTypeCodeLabel() {
		return shopTypeCodeLabel;
	}

	public void setShopTypeCodeLabel(String shopTypeCodeLabel) {
		this.shopTypeCodeLabel = shopTypeCodeLabel;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Timestamp getRefundEndDate() {
		return refundEndDate;
	}

	public void setRefundEndDate(Timestamp refundEndDate) {
		this.refundEndDate = refundEndDate;
	}

	public String getAfterSaleType() {
		return afterSaleType;
	}

	public void setAfterSaleType(String afterSaleType) {
		this.afterSaleType = afterSaleType;
		this.afterSaleTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_returnAfterSaleTypeList, afterSaleType);
	}

	public BigDecimal getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

	public String getAfterSaleTypeLabel() {
		return afterSaleTypeLabel;
	}

	public void setAfterSaleTypeLabel(String afterSaleTypeLabel) {
		this.afterSaleTypeLabel = afterSaleTypeLabel;
	}

	public String getReturnOrderType() {
		return returnOrderType;
	}

	public void setReturnOrderType(String returnOrderType) {
		this.returnOrderType = returnOrderType;
	}

	public List<String> getExternalCodeList() {
		return externalCodeList;
	}

	public void setExternalCodeList(List<String> externalCodeList) {
		this.externalCodeList = externalCodeList;
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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getIsGenerate() {
		return isGenerate;
	}

	public void setIsGenerate(String isGenerate) {
		this.isGenerate = isGenerate;
	}

	public List<String> getReturnCodeList() {
		return returnCodeList;
	}

	public void setReturnCodeList(List<String> returnCodeList) {
		this.returnCodeList = returnCodeList;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}
}
