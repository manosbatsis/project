package com.topideal.mongo.entity;

import java.sql.Timestamp;

/**
 * 爬虫配置表
 * @author lian_
 *
 */
public class ReptileConfigMongo {
	
    //商家ID
    private Long merchantId;
    //登陆用户名
    private String loginName;
    //爬虫账单时点
    private Integer timePoint;
    //id
    private Long reptileId;
    //使用平台
    private String platformName;
    //客户ID
    private Long customerId;
    //是否代理 0商家 1 代理商家
    private String isProxy;
    //代理商家Id
    private Long proxyId;
    //商家名称
    private String merchantName;
    //客户名称
    private String customerName;
	//平台类型 ：1-云集  2-唯品 3-京东 4-天猫
	private String platformType;
	/**
     * 出仓库id
     */
    private Long outDepotId;
    /**
     * 出仓库名称
     */
    private String outDepotName;
    /**
     * 入仓库id
     */
    private Long inDepotId;
    /**
     * 入仓库名称
     */
    private String inDepotName;
    /**
     * 事业部id
     */
    private Long buId;
    /**
     * 事业部名称
     */
    private String buName;
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 店铺编码
     */
    private String shopCode;
    
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Integer getTimePoint() {
		return timePoint;
	}
	public void setTimePoint(Integer timePoint) {
		this.timePoint = timePoint;
	}

	public Long getReptileId() {
		return reptileId;
	}
	public void setReptileId(Long reptileId) {
		this.reptileId = reptileId;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getIsProxy() {
		return isProxy;
	}
	public void setIsProxy(String isProxy) {
		this.isProxy = isProxy;
	}
	public Long getProxyId() {
		return proxyId;
	}
	public void setProxyId(Long proxyId) {
		this.proxyId = proxyId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
	public Long getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}
	public String getOutDepotName() {
		return outDepotName;
	}
	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}
	public Long getInDepotId() {
		return inDepotId;
	}
	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}
	public String getInDepotName() {
		return inDepotName;
	}
	public void setInDepotName(String inDepotName) {
		this.inDepotName = inDepotName;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public String getBuName() {
		return buName;
	}
	public void setBuName(String buName) {
		this.buName = buName;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
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
	
}
