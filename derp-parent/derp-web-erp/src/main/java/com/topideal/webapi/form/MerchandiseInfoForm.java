package com.topideal.webapi.form;

import java.io.Serializable;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品信息
 * 
 * @author lchenxing
 */
public class MerchandiseInfoForm extends PageForm implements Serializable {
    @ApiModelProperty(value = "令牌",required = true)
	private String token;
    @ApiModelProperty(value = "商品编码")
	private String goodsCode;
    @ApiModelProperty(value = "商品名称")
	private String name;
    @ApiModelProperty(value = "商品货号")
	private String goodsNo;	
    @ApiModelProperty(value = "商品条形码")
    private String barcode;
    @ApiModelProperty(value = "工厂编码")
	private String factoryNo;
    @ApiModelProperty(value = "数据来源 1主数据")
	private String source;
    @ApiModelProperty(value = "品牌id")
	private Long brandId;
    @ApiModelProperty(value = "商品二级分类ID")
	private Long productTypeId;
    @ApiModelProperty(value = "是否备案(1-是，0-否)")
    private String isRecord;
    @ApiModelProperty(value = "外仓统一码")
	private String outDepotFlag;
	
    
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getFactoryNo() {
		return factoryNo;
	}
	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
	public String getIsRecord() {
		return isRecord;
	}
	public void setIsRecord(String isRecord) {
		this.isRecord = isRecord;
	}
	public String getOutDepotFlag() {
		return outDepotFlag;
	}
	public void setOutDepotFlag(String outDepotFlag) {
		this.outDepotFlag = outDepotFlag;
	}
	
	
	

}
