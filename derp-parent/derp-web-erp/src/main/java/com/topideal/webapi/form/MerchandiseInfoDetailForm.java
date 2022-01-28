package com.topideal.webapi.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品信息
 * 
 * @author lchenxing
 */
public class MerchandiseInfoDetailForm extends PageForm implements Serializable {

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "商品货号")
	private String goodsNo;
	@ApiModelProperty(value = "商品编码")
	private String code;
	@ApiModelProperty(value = "产品Id")
	private Long productId;
	@ApiModelProperty(value = "数据来源 1主数据")
	private String source;
	@ApiModelProperty(value = "最小安全库存")
	private Integer minStock;
	@ApiModelProperty(value = "唯一标识")
	private String uniques;
	@ApiModelProperty(value = "商品名称")
	private String name;
	@ApiModelProperty(value = "最大安全库存")
	private Integer maxStock;
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "商品编码")
	private String goodsCode;
	@ApiModelProperty(value = "备案价格")
	private BigDecimal filingPrice;
	@ApiModelProperty(value = "工厂编码")
	private String factoryNo;
	@ApiModelProperty(value = "包装方式")
	private String packType;
	@ApiModelProperty(value = "预警类型  1、分仓预警 2全仓预警")
	private String warningType;
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
	@ApiModelProperty(value = "是否备案(1-是，0-否)")
    private String isRecord;
	@ApiModelProperty(value = "是否组合(1-是，0-否)")
    private String isGroup;
    @ApiModelProperty(value = "是否自有(1-是，0-否)")
    private String isSelf;
    @ApiModelProperty(value = "商品条形码")
    private String barcode;
    @ApiModelProperty(value = "状态(1使用中,0已禁用)")
    private String status;
    @ApiModelProperty(value = "标准条码")
    private String commbarcode;
    @ApiModelProperty(value = "英文名称")
    private String englishGoodsName;
	// -------------- 拓展字段
    @ApiModelProperty(value = "产品名称")
	private String productName;
    @ApiModelProperty(value = "品牌id")
	private Long brandId;
    @ApiModelProperty(value = "品牌名称")
	private String brandName;
    @ApiModelProperty(value = "原产国id")
	private Long countyId;
    @ApiModelProperty(value = "原产国")
	private String countyName;
    @ApiModelProperty(value = "计量单位id")
	private Long unit;
    @ApiModelProperty(value = "计量单位名称")
	private String unitName;
    @ApiModelProperty(value = "商品二级分类ID")
	private Long productTypeId;
    @ApiModelProperty(value = "商品一级级分类ID")
	private Long productTypeId0;
    @ApiModelProperty(value = "商品二级分类名称")
	private String productTypeName;
    @ApiModelProperty(value = "商品一级分类名称")
	private String productTypeName0;
    @ApiModelProperty(value = "商品分类名称")
	private String goodsClassifyName;
    @ApiModelProperty(value = "保质天数")
	private Integer shelfLifeDays;
    @ApiModelProperty(value = "毛重")
	private Double grossWeight;
    @ApiModelProperty(value = "净重")
	private Double netWeight;
    @ApiModelProperty(value = "hs编码")
	private String hsCode;
    @ApiModelProperty(value = "长")
	private Double length;
    @ApiModelProperty(value = "宽")
	private Double width;
    @ApiModelProperty(value = "高")
	private Double height;
    @ApiModelProperty(value = "体积")
	private Double volume;
    @ApiModelProperty(value = "生产厂家")
	private String manufacturer;
    @ApiModelProperty(value = "生产厂家地址")
	private String manufacturerAddr;
    @ApiModelProperty(value = "产品颜色")
	private String color;
    @ApiModelProperty(value = "产品大小")
	private Double size;
    @ApiModelProperty(value = "产品成分")
	private String component;
    @ApiModelProperty(value = "仓库id")
	private Long depotId;
    @ApiModelProperty(value = "储存选择过的id")
	private List ids;
    @ApiModelProperty(value = "储存商家ids")
	private List merchantIds;
    @ApiModelProperty(value = "价目表下限")
	private BigDecimal supplyMinPrice;
    @ApiModelProperty(value = "云集商家id")
	private Long cloudMerchantId;
    @ApiModelProperty(value = "供应商价目表id")
	private Long supplierId;
    @ApiModelProperty(value = "外仓统一码")
	private String outDepotFlag;
    @ApiModelProperty(value = "租户编码")
	private String tenantCode;
    @ApiModelProperty(value = "版本号 ")
	private Integer version;
    @ApiModelProperty(value = "来源商品id")
	private Long sourceGoodsId;
    
    @ApiModelProperty(value = "已选择的商品id")
    private String unNeedIds;
    @ApiModelProperty(value = "商品选品限制")
    private String productRestriction;
    @ApiModelProperty(value = "仓库id")
    private Long outDepotId;
    @ApiModelProperty(value = "popupType")
    private String popupType;
    @ApiModelProperty(value = "需要展示的商品id")
    private String needIds;
	@ApiModelProperty(value = "条码是否模糊查询：0-是 1-否")
    private String isBlur;
    
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Integer getMinStock() {
		return minStock;
	}
	public void setMinStock(Integer minStock) {
		this.minStock = minStock;
	}
	public String getUniques() {
		return uniques;
	}
	public void setUniques(String uniques) {
		this.uniques = uniques;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getMaxStock() {
		return maxStock;
	}
	public void setMaxStock(Integer maxStock) {
		this.maxStock = maxStock;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public BigDecimal getFilingPrice() {
		return filingPrice;
	}
	public void setFilingPrice(BigDecimal filingPrice) {
		this.filingPrice = filingPrice;
	}
	public String getFactoryNo() {
		return factoryNo;
	}
	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}
	public String getPackType() {
		return packType;
	}
	public void setPackType(String packType) {
		this.packType = packType;
	}
	public String getWarningType() {
		return warningType;
	}
	public void setWarningType(String warningType) {
		this.warningType = warningType;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getIsRecord() {
		return isRecord;
	}
	public void setIsRecord(String isRecord) {
		this.isRecord = isRecord;
	}
	public String getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}
	public String getIsSelf() {
		return isSelf;
	}
	public void setIsSelf(String isSelf) {
		this.isSelf = isSelf;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCommbarcode() {
		return commbarcode;
	}
	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}
	public String getEnglishGoodsName() {
		return englishGoodsName;
	}
	public void setEnglishGoodsName(String englishGoodsName) {
		this.englishGoodsName = englishGoodsName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Long getCountyId() {
		return countyId;
	}
	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public Long getUnit() {
		return unit;
	}
	public void setUnit(Long unit) {
		this.unit = unit;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Long getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
	public Long getProductTypeId0() {
		return productTypeId0;
	}
	public void setProductTypeId0(Long productTypeId0) {
		this.productTypeId0 = productTypeId0;
	}
	public String getProductTypeName() {
		return productTypeName;
	}
	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}
	public String getProductTypeName0() {
		return productTypeName0;
	}
	public void setProductTypeName0(String productTypeName0) {
		this.productTypeName0 = productTypeName0;
	}
	public String getGoodsClassifyName() {
		return goodsClassifyName;
	}
	public void setGoodsClassifyName(String goodsClassifyName) {
		this.goodsClassifyName = goodsClassifyName;
	}
	public Integer getShelfLifeDays() {
		return shelfLifeDays;
	}
	public void setShelfLifeDays(Integer shelfLifeDays) {
		this.shelfLifeDays = shelfLifeDays;
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
	public String getHsCode() {
		return hsCode;
	}
	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
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
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getManufacturerAddr() {
		return manufacturerAddr;
	}
	public void setManufacturerAddr(String manufacturerAddr) {
		this.manufacturerAddr = manufacturerAddr;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	public List getIds() {
		return ids;
	}
	public void setIds(List ids) {
		this.ids = ids;
	}
	public List getMerchantIds() {
		return merchantIds;
	}
	public void setMerchantIds(List merchantIds) {
		this.merchantIds = merchantIds;
	}
	public BigDecimal getSupplyMinPrice() {
		return supplyMinPrice;
	}
	public void setSupplyMinPrice(BigDecimal supplyMinPrice) {
		this.supplyMinPrice = supplyMinPrice;
	}
	public Long getCloudMerchantId() {
		return cloudMerchantId;
	}
	public void setCloudMerchantId(Long cloudMerchantId) {
		this.cloudMerchantId = cloudMerchantId;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getOutDepotFlag() {
		return outDepotFlag;
	}
	public void setOutDepotFlag(String outDepotFlag) {
		this.outDepotFlag = outDepotFlag;
	}
	public String getTenantCode() {
		return tenantCode;
	}
	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Long getSourceGoodsId() {
		return sourceGoodsId;
	}
	public void setSourceGoodsId(Long sourceGoodsId) {
		this.sourceGoodsId = sourceGoodsId;
	}
	public String getUnNeedIds() {
		return unNeedIds;
	}
	public void setUnNeedIds(String unNeedIds) {
		this.unNeedIds = unNeedIds;
	}
	public String getProductRestriction() {
		return productRestriction;
	}
	public void setProductRestriction(String productRestriction) {
		this.productRestriction = productRestriction;
	}
	public Long getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}
	public String getPopupType() {
		return popupType;
	}
	public void setPopupType(String popupType) {
		this.popupType = popupType;
	}
	public String getNeedIds() {
		return needIds;
	}
	public void setNeedIds(String needIds) {
		this.needIds = needIds;
	}

	public String getIsBlur() {
		return isBlur;
	}

	public void setIsBlur(String isBlur) {
		this.isBlur = isBlur;
	}
}
