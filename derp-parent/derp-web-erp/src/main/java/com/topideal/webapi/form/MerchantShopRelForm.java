package com.topideal.webapi.form;

import java.io.Serializable;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商家店铺关联表
 * @author lian_
 *
 */
public class MerchantShopRelForm extends PageForm implements Serializable{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
	@ApiModelProperty(value = "店铺名称")
    private String shopName;
	@ApiModelProperty(value = "店铺编码")
    private String shopCode;
	@ApiModelProperty(value = "电商平台编码")
    private String storePlatformCode;
	@ApiModelProperty(value = "状态(1使用中,0已禁用)")
    private String status;
	@ApiModelProperty(value = "数据来源编码")
    private String dataSourceCode;
	@ApiModelProperty(value = "仓库id")
    private Long depotId;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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
	public String getStorePlatformCode() {
		return storePlatformCode;
	}
	public void setStorePlatformCode(String storePlatformCode) {
		this.storePlatformCode = storePlatformCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDataSourceCode() {
		return dataSourceCode;
	}
	public void setDataSourceCode(String dataSourceCode) {
		this.dataSourceCode = dataSourceCode;
	}
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
    
	
	
  
  
}
