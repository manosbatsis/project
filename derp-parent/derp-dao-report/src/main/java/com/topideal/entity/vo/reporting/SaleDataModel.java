package com.topideal.entity.vo.reporting;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class SaleDataModel extends PageModel implements Serializable {

	/**
	 * 自增ID
	 */
	private Long id;
	/**
	 * 事业部ID
	 */
	private Long buId;
	/**
	 * 事业部名称
	 */
	private String buName;
	/**
	 * 公司id
	 */
	private Long merchantId;
	/**
	 * 公司名称
	 */
	private String merchantName;
	/**
	 * 出库仓库id
	 */
	private Long outDepotId;
	/**
	 * 出库仓库名称
	 */
	private String outDepotName;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 客户ID
	 */
	private Long customerId;
	/**
	 * 月份
	 */
	private String month;
	/**
	 * 商品货号
	 */
	private String goodsNo;
	/**
	 * 标准条码
	 */
	private String commbarcode;
	/**
	 * 标准品牌
	 */
	private String brandParent;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 电商平台编码
	 */
	private String storePlatformCode;
	/**
	* 
	*/
	private String storePlatformName;
	/**
	 * 店铺编码
	 */
	private String shopCode;
	/**
	 * 店铺编码
	 */
	private String shopName;
	/**
	 * 数量
	 */
	private Integer num;
	/**
	 * 创建时间
	 */
	private Timestamp createDate;
	/**
	 * 修改时间
	 */
	private Timestamp modifyDate;

	/**
	 * 销售币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
	 */
	private String saleCurrency;
	/**
	 * 销售金额
	 */
	private BigDecimal saleAmount;
	/**
	 * 人民币汇率
	 */
	private Double cnyRate;
	/**
	 * 人民币金额
	 */
	private BigDecimal cnyAmount;
	/**
	 * 销售类型：1-购销A 2-购销B 3-一件代发 4-POP 5-代销
	 */
	private String orderType;
	/**
	 * 渠道类型 To B,To C
	 */
	private String channelType;
	/**
	 * 客户是否为内部公司 1-是 0-否
	 */
	private String innerMerchantType;
	// 归属日期
	private Date reportDate;
	/**
	 * 客户类型(1内部,2外部)
	 */
	private String customerType;

	/**
	 * 部门ID
	 */
	private Long departmentId;

	/**
	 * 部门名称
	 */
	private String departmentName;

	/**
	 * 母品牌ID
	 */
	private Long parentBrandId;

	/**
	 * 母品牌编码
	 */
	private String parentBrandCode;

	/**
	 * 母品牌名称
	 */
	private String parentBrandName;

	/**
	 * 港币汇率
	 */
	private Double hkRate;

	/**
	 * 港币金额
	 */
	private BigDecimal hkAmount;

	/**
	 * 标准品牌ID
	 */
	private Long brandParentId;

	public Long getBrandParentId() {
		return brandParentId;
	}

	public void setBrandParentId(Long brandParentId) {
		this.brandParentId = brandParentId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getParentBrandId() {
		return parentBrandId;
	}

	public void setParentBrandId(Long parentBrandId) {
		this.parentBrandId = parentBrandId;
	}

	public String getParentBrandName() {
		return parentBrandName;
	}

	public void setParentBrandName(String parentBrandName) {
		this.parentBrandName = parentBrandName;
	}

	public Double getHkRate() {
		return hkRate;
	}

	public void setHkRate(Double hkRate) {
		this.hkRate = hkRate;
	}

	public BigDecimal getHkAmount() {
		return hkAmount;
	}

	public void setHkAmount(BigDecimal hkAmount) {
		this.hkAmount = hkAmount;
	}

	public String getParentBrandCode() {
		return parentBrandCode;
	}

	public void setParentBrandCode(String parentBrandCode) {
		this.parentBrandCode = parentBrandCode;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* buId get 方法 */
	public Long getBuId() {
		return buId;
	}

	/* buId set 方法 */
	public void setBuId(Long buId) {
		this.buId = buId;
	}

	/* buName get 方法 */
	public String getBuName() {
		return buName;
	}

	/* buName set 方法 */
	public void setBuName(String buName) {
		this.buName = buName;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/* outDepotId get 方法 */
	public Long getOutDepotId() {
		return outDepotId;
	}

	/* outDepotId set 方法 */
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	/* outDepotName get 方法 */
	public String getOutDepotName() {
		return outDepotName;
	}

	/* outDepotName set 方法 */
	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}

	/* customerName get 方法 */
	public String getCustomerName() {
		return customerName;
	}

	/* customerName set 方法 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/* customerId get 方法 */
	public Long getCustomerId() {
		return customerId;
	}

	/* customerId set 方法 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/* month get 方法 */
	public String getMonth() {
		return month;
	}

	/* month set 方法 */
	public void setMonth(String month) {
		this.month = month;
	}

	/* goodsNo get 方法 */
	public String getGoodsNo() {
		return goodsNo;
	}

	/* goodsNo set 方法 */
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	/* commbarcode get 方法 */
	public String getCommbarcode() {
		return commbarcode;
	}

	/* commbarcode set 方法 */
	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}

	/* brandParent get 方法 */
	public String getBrandParent() {
		return brandParent;
	}

	/* brandParent set 方法 */
	public void setBrandParent(String brandParent) {
		this.brandParent = brandParent;
	}

	/* type get 方法 */
	public String getType() {
		return type;
	}

	/* type set 方法 */
	public void setType(String type) {
		this.type = type;
	}

	/* storePlatformCode get 方法 */
	public String getStorePlatformCode() {
		return storePlatformCode;
	}

	/* storePlatformCode set 方法 */
	public void setStorePlatformCode(String storePlatformCode) {
		this.storePlatformCode = storePlatformCode;
	}

	/* storePlatformName get 方法 */
	public String getStorePlatformName() {
		return storePlatformName;
	}

	/* storePlatformName set 方法 */
	public void setStorePlatformName(String storePlatformName) {
		this.storePlatformName = storePlatformName;
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

	/* num get 方法 */
	public Integer getNum() {
		return num;
	}

	/* num set 方法 */
	public void setNum(Integer num) {
		this.num = num;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* modifyDate get 方法 */
	public Timestamp getModifyDate() {
		return modifyDate;
	}

	/* modifyDate set 方法 */
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getSaleCurrency() {
		return saleCurrency;
	}

	public void setSaleCurrency(String saleCurrency) {
		this.saleCurrency = saleCurrency;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public Double getCnyRate() {
		return cnyRate;
	}

	public void setCnyRate(Double cnyRate) {
		this.cnyRate = cnyRate;
	}

	public BigDecimal getCnyAmount() {
		return cnyAmount;
	}

	public void setCnyAmount(BigDecimal cnyAmount) {
		this.cnyAmount = cnyAmount;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getInnerMerchantType() {
		return innerMerchantType;
	}

	public void setInnerMerchantType(String innerMerchantType) {
		this.innerMerchantType = innerMerchantType;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	
	
}
