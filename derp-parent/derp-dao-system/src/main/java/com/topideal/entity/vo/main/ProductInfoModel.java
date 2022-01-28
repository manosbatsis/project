package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 产品表
 * @author lianchenxing
 */
public class ProductInfoModel extends PageModel implements Serializable {

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
	// 产品二级分类名称
	private String productTypeName;
	// id
	private Long id;
	// 产品条形码
	private String barcode;
	// 高
	private Double height;
	// 创建时间
	private Timestamp createDate;
	// 修改时间
	private Timestamp modifyDate;
	// 长
	private Double length;
	// 主图
	private String productImg01;
	// 产品二级分类id
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
    /**
    * 产品一级分类id
    */
    private Long productTypeId0;
    /**
    * 产品一级分类名称
    */
    private String productTypeName0;
    /**
    * 标准条码
    */
    private String commbarcode;
	
	
	//拓展字段
	// 计量单位名称
	private String unitName;
	// 商品分类名称
	private String goodsClassifyName;
	// 原产国
	private String countyName;
	// 品牌名称
	private String brandName;
	//商品ID
	private Long merchandiesId;
	//标准品牌ID
	private Long brandParentId;


	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
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

	public Long getMerchandiesId() {
		return merchandiesId;
	}

	public void setMerchandiesId(Long merchandiesId) {
		this.merchandiesId = merchandiesId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getGoodsClassifyName() {
		return goodsClassifyName;
	}

	public void setGoodsClassifyName(String goodsClassifyName) {
		this.goodsClassifyName = goodsClassifyName;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/* color get 方法 */
	public String getColor() {
		return color;
	}

	/* color set 方法 */
	public void setColor(String color) {
		this.color = color;
	}

	/* modifier get 方法 */
	public Long getModifier() {
		return modifier;
	}

	/* modifier set 方法 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	/* remark get 方法 */
	public String getRemark() {
		return remark;
	}

	/* remark set 方法 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/* spec get 方法 */
	public String getSpec() {
		return spec;
	}

	/* spec set 方法 */
	public void setSpec(String spec) {
		this.spec = spec;
	}

	/* manufacturer get 方法 */
	public String getManufacturer() {
		return manufacturer;
	}

	/* manufacturer set 方法 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/* countyId get 方法 */
	public Long getCountyId() {
		return countyId;
	}

	/* countyId set 方法 */
	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	/* manufacturerAddr get 方法 */
	public String getManufacturerAddr() {
		return manufacturerAddr;
	}

	/* manufacturerAddr set 方法 */
	public void setManufacturerAddr(String manufacturerAddr) {
		this.manufacturerAddr = manufacturerAddr;
	}

	/* productTypeName get 方法 */
	public String getProductTypeName() {
		return productTypeName;
	}

	/* productTypeName set 方法 */
	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* barcode get 方法 */
	public String getBarcode() {
		return barcode;
	}

	/* barcode set 方法 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/* height get 方法 */
	public Double getHeight() {
		return height;
	}

	/* height set 方法 */
	public void setHeight(Double height) {
		this.height = height;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* modifyDate get 方法 */
	public Timestamp getModifyDate() {
		return modifyDate;
	}

	/* modifyDate set 方法 */
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	/* length get 方法 */
	public Double getLength() {
		return length;
	}

	/* length set 方法 */
	public void setLength(Double length) {
		this.length = length;
	}

	/* productImg01 get 方法 */
	public String getProductImg01() {
		return productImg01;
	}

	/* productImg01 set 方法 */
	public void setProductImg01(String productImg01) {
		this.productImg01 = productImg01;
	}

	/* productTypeId get 方法 */
	public Long getProductTypeId() {
		return productTypeId;
	}

	/* productTypeId set 方法 */
	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}

	/* volume get 方法 */
	public Double getVolume() {
		return volume;
	}

	/* volume set 方法 */
	public void setVolume(Double volume) {
		this.volume = volume;
	}

	/* unit get 方法 */
	public Long getUnit() {
		return unit;
	}

	/* unit set 方法 */
	public void setUnit(Long unit) {
		this.unit = unit;
	}

	/* grossWeight get 方法 */
	public Double getGrossWeight() {
		return grossWeight;
	}

	/* grossWeight set 方法 */
	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	/* netWeight get 方法 */
	public Double getNetWeight() {
		return netWeight;
	}

	/* netWeight set 方法 */
	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	/* component get 方法 */
	public String getComponent() {
		return component;
	}

	/* component set 方法 */
	public void setComponent(String component) {
		this.component = component;
	}

	/* hsCode get 方法 */
	public String getHsCode() {
		return hsCode;
	}

	/* hsCode set 方法 */
	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	/* size get 方法 */
	public Double getSize() {
		return size;
	}

	/* size set 方法 */
	public void setSize(Double size) {
		this.size = size;
	}

	/* brandId get 方法 */
	public Long getBrandId() {
		return brandId;
	}

	/* brandId set 方法 */
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	/* name get 方法 */
	public String getName() {
		return name;
	}

	/* name set 方法 */
	public void setName(String name) {
		this.name = name;
	}

	/* width get 方法 */
	public Double getWidth() {
		return width;
	}

	/* width set 方法 */
	public void setWidth(Double width) {
		this.width = width;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* shelfLifeDays get 方法 */
	public Integer getShelfLifeDays() {
		return shelfLifeDays;
	}

	/* shelfLifeDays set 方法 */
	public void setShelfLifeDays(Integer shelfLifeDays) {
		this.shelfLifeDays = shelfLifeDays;
	}

	/* describe get 方法 */
	public String getDescribe() {
		return describe;
	}

	/* describe set 方法 */
	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Long getBrandParentId() {
		return brandParentId;
	}

	public void setBrandParentId(Long brandParentId) {
		this.brandParentId = brandParentId;
	}
}
