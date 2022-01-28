package com.topideal.webapi.form;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品信息
 * 
 * @author lchenxing
 */
public class MerchandiseInfoAddForm implements Serializable {
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	private Long id;
	@ApiModelProperty(value = "商品货号",required = true)
	private String goodsNo;
	@ApiModelProperty(value = "商品条形码",required = true)
    private String barcode;

	@ApiModelProperty(value = "商品名称",required = true)
	private String name;

	@ApiModelProperty(value = "计量单位id",required = true)
	private Long unit;

	@ApiModelProperty(value = "原产国id",required = true)
	private Long countyId;
	@ApiModelProperty(value = "包装方式")
	private String packType;
	@ApiModelProperty(value = "净重")
	private Double netWeight;
	@ApiModelProperty(value = "毛重")
	private Double grossWeight;
	@ApiModelProperty(value = "保质天数")
	private Integer shelfLifeDays;
	@ApiModelProperty(value = "体积")
	private Double volume;
	@ApiModelProperty(value = "长")
	private Double length;
	@ApiModelProperty(value = "宽")
	private Double width;
	@ApiModelProperty(value = "高")
	private Double height;
	@ApiModelProperty(value = "备案价格")
	private BigDecimal filingPrice;
	@ApiModelProperty(value = "生产厂家地址")
	private String manufacturerAddr;
	@ApiModelProperty(value = "hs编码")
	private String hsCode;
	@ApiModelProperty(value = "描述")
	private String describe;
	@ApiModelProperty(value = "商品描述")
	private String spec;
	@ApiModelProperty(value = "生产厂家")
	private String manufacturer;
	@ApiModelProperty(value = "一箱多少件")
    private Integer boxToUnit;
	@ApiModelProperty(value = "一托多少件")
    private Integer torrToUnit;
	@ApiModelProperty(value = "工厂编码")
	private String factoryNo;
	
	@ApiModelProperty(value = "关区id,多个英文逗号隔开")
	private String customsAreaIds;
	@ApiModelProperty(value = "原产地区")
	private String countyArea;
	@ApiModelProperty(value = "申报要素")
	private String declareFactor;
	@ApiModelProperty(value = "成分")
	private String component;
	@ApiModelProperty(value = "第一法定单位")
	private String unitNameOne;
	@ApiModelProperty(value = "第二法定单位")
	private String unitNameTwo;
	@ApiModelProperty(value = "海关商品备案号")
	private String customsGoodsRecordNo;
	
	@ApiModelProperty(value = "品牌id")
	private Long brandId;
	@ApiModelProperty(value = "一级分类iid")
	private Long productTypeId0;
	@ApiModelProperty(value = "二级分类id")
	private Long productTypeId;	
	@ApiModelProperty(value = "仓库id集合,多个用英文逗号隔开")
	private String depotIds;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getUnit() {
		return unit;
	}
	public void setUnit(Long unit) {
		this.unit = unit;
	}
	public Long getCountyId() {
		return countyId;
	}
	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}
	public String getPackType() {
		return packType;
	}
	public void setPackType(String packType) {
		this.packType = packType;
	}
	public Double getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}
	public Double getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}
	public Integer getShelfLifeDays() {
		return shelfLifeDays;
	}
	public void setShelfLifeDays(Integer shelfLifeDays) {
		this.shelfLifeDays = shelfLifeDays;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
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
	public BigDecimal getFilingPrice() {
		return filingPrice;
	}
	public void setFilingPrice(BigDecimal filingPrice) {
		this.filingPrice = filingPrice;
	}
	public String getManufacturerAddr() {
		return manufacturerAddr;
	}
	public void setManufacturerAddr(String manufacturerAddr) {
		this.manufacturerAddr = manufacturerAddr;
	}
	public String getHsCode() {
		return hsCode;
	}
	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
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
	public Integer getBoxToUnit() {
		return boxToUnit;
	}
	public void setBoxToUnit(Integer boxToUnit) {
		this.boxToUnit = boxToUnit;
	}
	public Integer getTorrToUnit() {
		return torrToUnit;
	}
	public void setTorrToUnit(Integer torrToUnit) {
		this.torrToUnit = torrToUnit;
	}
	public String getFactoryNo() {
		return factoryNo;
	}
	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}
	public String getCustomsAreaIds() {
		return customsAreaIds;
	}
	public void setCustomsAreaIds(String customsAreaIds) {
		this.customsAreaIds = customsAreaIds;
	}
	public String getCountyArea() {
		return countyArea;
	}
	public void setCountyArea(String countyArea) {
		this.countyArea = countyArea;
	}
	public String getDeclareFactor() {
		return declareFactor;
	}
	public void setDeclareFactor(String declareFactor) {
		this.declareFactor = declareFactor;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getUnitNameOne() {
		return unitNameOne;
	}
	public void setUnitNameOne(String unitNameOne) {
		this.unitNameOne = unitNameOne;
	}
	public String getUnitNameTwo() {
		return unitNameTwo;
	}
	public void setUnitNameTwo(String unitNameTwo) {
		this.unitNameTwo = unitNameTwo;
	}
	public String getCustomsGoodsRecordNo() {
		return customsGoodsRecordNo;
	}
	public void setCustomsGoodsRecordNo(String customsGoodsRecordNo) {
		this.customsGoodsRecordNo = customsGoodsRecordNo;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getProductTypeId0() {
		return productTypeId0;
	}
	public void setProductTypeId0(Long productTypeId0) {
		this.productTypeId0 = productTypeId0;
	}
	public Long getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
	public String getDepotIds() {
		return depotIds;
	}
	public void setDepotIds(String depotIds) {
		this.depotIds = depotIds;
	}



   
}
