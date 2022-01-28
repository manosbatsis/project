package com.topideal.webapi.form;

import java.io.Serializable;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModelProperty;

/**
 * 爬虫配置表
 * @author lian_
 */
public class ReptileConfigForm extends PageForm implements Serializable{
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
    @ApiModelProperty(value = "id(编辑必填/新增非必填)")
    private Long id;
    @ApiModelProperty(value = "平台类型 ：1-云集  2-唯品账单",required = true)
    private String platformType;
    @ApiModelProperty(value = "用户名",required = true)
    private String loginName;    
    @ApiModelProperty(value = "爬取账单时点")
    private Integer timePoint;
    @ApiModelProperty(value = "公司",required = true)
    private Long merchantId;    
    @ApiModelProperty(value = "客户 Id",required = true)
    private Long customerId; 
    @ApiModelProperty(value = "出口仓库 Id",required = true)
    private Long outDepotId;    
    @ApiModelProperty(value = "入库仓库 Id",required = true)
    private Long inDepotId;
    @ApiModelProperty(value = "店铺Id")
    private Long shopId;
    
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
	public String getPlatformType() {
		return platformType;
	}
	public void setPlatformType(String platformType) {
		this.platformType = platformType;
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
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}
	public Long getInDepotId() {
		return inDepotId;
	}
	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}




}

