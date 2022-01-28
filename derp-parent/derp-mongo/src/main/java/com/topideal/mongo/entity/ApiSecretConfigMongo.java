package com.topideal.mongo.entity;

import java.sql.Timestamp;

/**
 * api密钥配置
 * @author liuhanguang
 *
 */
public class ApiSecretConfigMongo {
	
    //APP_Key
    private String appKey;
    //密钥
    private String appSecret;
    //备注
    private String remark;
    //平台名称
    private String platformName;
    //状态(1-启用,0-禁用)
    private String status;
    //商家ID
    private Long merchantId;
    //卓志编码
    private String topidealCode;
    
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getTopidealCode() {
		return topidealCode;
	}
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}
}
