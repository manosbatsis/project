package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MerchandiseContrastForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token; 
	
    @ApiModelProperty(value = "平台名称")
    private String platformName;
    
    @ApiModelProperty(value = "爬虫商品货号")
    private String crawlerGoodsNo;
    
    @ApiModelProperty(value = "商家ID")
    private String merchantId;
    
    @ApiModelProperty(value = "状态 0-启用 1-禁用")
    private String status;
    
    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    
    @ApiModelProperty(value = "id集合，多个用逗号隔开")
    private String ids;

	public String getToken() {
		return token;
	}
	

	public void setToken(String token) {
		this.token = token;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getCrawlerGoodsNo() {
		return crawlerGoodsNo;
	}

	public void setCrawlerGoodsNo(String crawlerGoodsNo) {
		this.crawlerGoodsNo = crawlerGoodsNo;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
}