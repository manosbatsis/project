package com.topideal.order.webapi.platformdata.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PlatformMerchandiseInfoForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token ;
    /**
    * 商品编码
    */
	@ApiModelProperty(value = "商品编码",required = false)
    private String wareId;
    /**
    * 条形码
    */
	@ApiModelProperty(value = "条形码",required = false)
    private String upc;
    /**
    * 平台名称
    */
	@ApiModelProperty(value = "平台名称",required = false)
    private String platform;
    /**
     * 爬虫平台类型：1-云集  2-唯品 3-京东 4-天猫
     */
	@ApiModelProperty(value = "爬虫平台类型",required = false)
    private String crawlerType;
	
	@ApiModelProperty(value = "品牌",required = false)
    private String brand ;

	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getCrawlerType() {
		return crawlerType;
	}

	public void setCrawlerType(String crawlerType) {
		this.crawlerType = crawlerType;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

    
}