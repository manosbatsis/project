package com.topideal.api.sync.sy01;

import java.math.BigDecimal;

/**
 * 同步商品到主数据实体
 * @author 杨创
 *
 */
public class SyncGoodsRequest {
	private Integer syncType;//1
	private String subscriberCode;//JFX 来源
	private String tenantCode;//租户
	private String productName;// 商品名称
	private String class1;// 经分销一级类目
	private String class2;// 经分销二级类目
	private Integer isUpdateProduct;//是否更新产品 // 枚举备注: 0否1是
	private String goodsCode;//经分销商品货号
	private String goodsBusiCode;//经分销商品ERP编码
	private String goodsName;//经分销商品名称
	private String merchantCode;//经分销公司卓志编码
	private String goodsEnName;// 经分销商品英文名称
	private String productCode;//经分销商品标准条码;    
	private String barCode;   //经分销商品条形码
	private String unit;      // 经分销商品计量单位
	private String brand;    // 品牌名称
	private String chinaBrand;//经分销商品品牌中文名称
	private String englishBrand;//经分销商品品牌英文名称
	private String ciqOriginCountry;//经分销商品原产国
	private Double length;//经分销商品长，注意单位转换
	private Double width;//经分销商品宽，注意单位转换；
	private Double height;//经分销商品高，注意单位转换；
	private Double volume;//经分销商品体积，注意单位转换；
	private Double grossWeight;//经分销商品毛重，注意单位转换；
	private Double netWeight;//经分销商品净重，注意单位转换；
	private String spec;//经分销商品规格型号；
	private BigDecimal price; //经分销商品单价；
	private Integer qualityDays;//经分销商品保质期天数
	public Integer getSyncType() {
		return syncType;
	}
	public void setSyncType(Integer syncType) {
		this.syncType = syncType;
	}
	public String getSubscriberCode() {
		return subscriberCode;
	}
	public void setSubscriberCode(String subscriberCode) {
		this.subscriberCode = subscriberCode;
	}
	public String getTenantCode() {
		return tenantCode;
	}
	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getClass1() {
		return class1;
	}
	public void setClass1(String class1) {
		this.class1 = class1;
	}
	public String getClass2() {
		return class2;
	}
	public void setClass2(String class2) {
		this.class2 = class2;
	}
	
	public Integer getIsUpdateProduct() {
		return isUpdateProduct;
	}
	public void setIsUpdateProduct(Integer isUpdateProduct) {
		this.isUpdateProduct = isUpdateProduct;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsBusiCode() {
		return goodsBusiCode;
	}
	public void setGoodsBusiCode(String goodsBusiCode) {
		this.goodsBusiCode = goodsBusiCode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	public String getGoodsEnName() {
		return goodsEnName;
	}
	public void setGoodsEnName(String goodsEnName) {
		this.goodsEnName = goodsEnName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getChinaBrand() {
		return chinaBrand;
	}
	public void setChinaBrand(String chinaBrand) {
		this.chinaBrand = chinaBrand;
	}
	public String getEnglishBrand() {
		return englishBrand;
	}
	public void setEnglishBrand(String englishBrand) {
		this.englishBrand = englishBrand;
	}
	public String getCiqOriginCountry() {
		return ciqOriginCountry;
	}
	public void setCiqOriginCountry(String ciqOriginCountry) {
		this.ciqOriginCountry = ciqOriginCountry;
	}
	public Double getLength() {
		return length;
	}
	public void setLength(Double length) {
		this.length = length;
	}
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public Double getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}
	public Double getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getQualityDays() {
		return qualityDays;
	}
	public void setQualityDays(Integer qualityDays) {
		this.qualityDays = qualityDays;
	}

	
	
	
	
}
