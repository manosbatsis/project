package com.topideal.webapi.form;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class InventoryLocationMappingDetailForm implements Serializable{
    @ApiModelProperty(value = "令牌",required = true)
	private String token;
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "ids")
    private String ids ;
    @ApiModelProperty(value = "商家ID")
    private Long merchantId;
    @ApiModelProperty(value = "商家名")
    private String merchantName;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "库位货号")
    private String goodsNo;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "原货号")
    private String originalGoodsNo;
    @ApiModelProperty(value = "库位类型：1、常规品 2、赠送品 3、sample")
    private String type;
    @ApiModelProperty(value = "原货号ID")
    private Long originalGoodsId;
    @ApiModelProperty(value = "指定出入库商品 1. 已指定 0, 未指定")
    private String isorRappoint;
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
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getOriginalGoodsNo() {
		return originalGoodsNo;
	}
	public void setOriginalGoodsNo(String originalGoodsNo) {
		this.originalGoodsNo = originalGoodsNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getOriginalGoodsId() {
		return originalGoodsId;
	}
	public void setOriginalGoodsId(Long originalGoodsId) {
		this.originalGoodsId = originalGoodsId;
	}
	public String getIsorRappoint() {
		return isorRappoint;
	}
	public void setIsorRappoint(String isorRappoint) {
		this.isorRappoint = isorRappoint;
	}


    

}
