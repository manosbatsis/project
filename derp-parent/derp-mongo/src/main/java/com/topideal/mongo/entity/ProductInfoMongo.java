package com.topideal.mongo.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 货品
 * @author lian_
 */
public class ProductInfoMongo {


	// 产品颜色
	private String color;
	// 修改人
	private Long modifier;
	// 备注
	private String remark;
	// 规格型号
	private String spec;
	// 生产厂家
	private String manufacturer;
	// 原产国
	private Long countyId;
	// 生产厂家地址
	private String manufacturerAddr;
	// 二级类目名称
	private String productTypeName;
	// id
	private Long productId;
	// 产品条形码
	private String barcode;
	// 高
	private Double height;
	// 长
	private Double length;
	// 主图
	private String productImg01;
	// 二级类目id
	private Long productTypeId;
	// 体积
	private Double volume;
	// 标准单位
	private Long unit;
	// 毛重
	private Double grossWeight;
	// 净重
	private Double netWeight;
	// 产品成分
	private String component;
	// HS编码
	private String hsCode;
	// 产品尺寸
	private Double size;
	// 品牌ID
	private Long brandId;
	// 产品名称
	private String name;
	// 高
	private Double width;
	// 创建人
	private Long creater;
	// 保质日期
	private Integer shelfLifeDays;
	// 描述
	private String describe;
	// 一级类目id
	private Long productTypeId0;
	//一级类目名称
	private String productTypeName0;
	//标准条码
	private String commbarcode;
	// 标准品牌ID
	private Long brandParentId;
	
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Long getModifier() {
		return modifier;
	}
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Long getCountyId() {
		return countyId;
	}
	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}
	public String getManufacturerAddr() {
		return manufacturerAddr;
	}
	public void setManufacturerAddr(String manufacturerAddr) {
		this.manufacturerAddr = manufacturerAddr;
	}
	public String getProductTypeName() {
		return productTypeName;
	}
	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getLength() {
		return length;
	}
	public void setLength(Double length) {
		this.length = length;
	}
	public String getProductImg01() {
		return productImg01;
	}
	public void setProductImg01(String productImg01) {
		this.productImg01 = productImg01;
	}
	public Long getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public Long getUnit() {
		return unit;
	}
	public void setUnit(Long unit) {
		this.unit = unit;
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
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getHsCode() {
		return hsCode;
	}
	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public Long getCreater() {
		return creater;
	}
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	public Integer getShelfLifeDays() {
		return shelfLifeDays;
	}
	public void setShelfLifeDays(Integer shelfLifeDays) {
		this.shelfLifeDays = shelfLifeDays;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public Long getProductTypeId0() {
		return productTypeId0;
	}
	public void setProductTypeId0(Long productTypeId0) {
		this.productTypeId0 = productTypeId0;
	}
	public String getProductTypeName0() {
		return productTypeName0;
	}
	public void setProductTypeName0(String productTypeName0) {
		this.productTypeName0 = productTypeName0;
	}
	public String getCommbarcode() {
		return commbarcode;
	}
	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}
	public Long getBrandParentId() {
		return brandParentId;
	}
	public void setBrandParentId(Long brandParentId) {
		this.brandParentId = brandParentId;
	}
	
}
