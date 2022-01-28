package com.topideal.webapi.form;


import java.io.Serializable;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModelProperty;
/**
 * api密钥配置 Form
 * @author 杨创
 *
 */
public class ApiSecretConfigForm extends PageForm implements Serializable{
    @ApiModelProperty(value = "令牌",required = true)
	private String token;
    @ApiModelProperty(value = "id(编辑必填/新增非必填)")
    private Long id;
    @ApiModelProperty(value = "公司id",required = true)
    private Long merchantId;
    @ApiModelProperty(value = "app_key",required = true)
    private String appKey;
    @ApiModelProperty(value = "app_secret)",required = true)
    private String appSecret;
    @ApiModelProperty(value = "备注")
    private String remark;
    
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
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
     
     



}

