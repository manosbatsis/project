package com.topideal.webapi.form;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class InventoryLocationMappingForm implements Serializable{
    @ApiModelProperty(value = "令牌",required = true)
	private String token;
    @ApiModelProperty(value = "id新增非标填,修改必填")
    private Long id;
    @ApiModelProperty(value = "商家id",required = true)
    private Long merchantId;
    @ApiModelProperty(value = "原货号",required = true)
    private String originalGoodsNo;
    @ApiModelProperty(value = "库位货号",required = true)
    private String goodsNo;
    @ApiModelProperty(value = "库位类型：1、常规品 2、赠送品 3、sample",required = true)
    private String type;
    
    
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
	public String getOriginalGoodsNo() {
		return originalGoodsNo;
	}
	public void setOriginalGoodsNo(String originalGoodsNo) {
		this.originalGoodsNo = originalGoodsNo;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

    



}
