package com.topideal.mongo.entity;

import java.math.BigDecimal;

/**
 * 商品信息
 * @author lchenxing
 */
public class MerchandiseInfoMogo {

	// 商品货号
	private String goodsNo;
	// 自编码
	private String code;
	// 产品Id
	private Long productId;
	// 修改人
	private Long modifier;
	// 备注
	private String remark;
	// 数据来源 1主数据
	private String source;
	// 最小安全库存
	private Integer minStock;
	// 唯一标识
	private String unique;
	// 商品名称
	private String name;
	// 最大安全库存
	private Integer maxStock;
	// id
	private Long merchandiseId;
	// 商品编码
	private String goodsCode;
	// 描述
	private String describe;
	// 备案价格
	private BigDecimal filingPrice;
	// 工厂编码
	private String factoryNo;
	// 包装方式
	private String packType;
	// 预警类型 1、分仓预警 2全仓预警
	private String warningType;
	// 创建人
	private Long creater;
	// 商家名称
	private String merchantName;
	// 商家ID
	private Long merchantId;
	// 是否备案(1-是，0-否)
	private String isRecord;
	// 是否组合(1-是，0-否)
	private String isGroup;
	// 条形码
	private String barcode;
	 //状态(1使用中,0已禁用)
    private String status;
    //标准条码
    private String commbarcode;
    //一箱多少件
    private Integer boxToUnit;
    //一托多少件
    private Integer torrToUnit;

    //外仓统一码
    private String outDepotFlag;
    
    private String englishGoodsName ;
    
    
    private Long customsAreaId ;//关区ID
    private String hsCode;//HS编码
    private Long brandId;//品牌Brand id
    private String productTypeName0;//产品一级分类名称
    private Long productTypeId0; //产品一级分类id
    private String productTypeName ;//产品二级分类名称
    private Long productTypeId;//产品二级分类id
    private Double grossWeight;//毛重
    private Double netWeight;//净重
    private String spec;//规格型号
    private Integer shelfLifeDays;//保质天数
    private Long unit;//标准单位
    private String component;//产品成分
    private String manufacturer;//生产厂家
    private String manufacturerAddr ;//生产厂家地址
    private Long countyId;//原产国
    private String countyArea;//原产国地区
    private String declareFactor;//申报要素
    private String customsGoodsRecordNo;//海关商品备案号
	private String unitNameOne;//第一法定单位
	private String unitNameTwo;//第二法定单位
    
    
	private String isSelf;
	private Double length;
	private Double width;
	private Double height;
	private Double volume;
	private String imageUrl1;
	private String imageUrl2;
	private String imageUrl3;
	private String imageUrl4;
	private String imageUrl5;
	
	private String createName;
	private String updateName;	
	private String productImg01;
	private String color;
	private Double size;
	//租户编码
	private String tenantCode;
	//版本号
	private Integer version;
		//来源商品id
	private Long sourceGoodsId;

	public Long getCustomsAreaId() {
		return customsAreaId;
	}

	public void setCustomsAreaId(Long customsAreaId) {
		this.customsAreaId = customsAreaId;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getProductTypeName0() {
		return productTypeName0;
	}

	public void setProductTypeName0(String productTypeName0) {
		this.productTypeName0 = productTypeName0;
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

	public Long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
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

	public Integer getShelfLifeDays() {
		return shelfLifeDays;
	}

	public void setShelfLifeDays(Integer shelfLifeDays) {
		this.shelfLifeDays = shelfLifeDays;
	}

	public Long getUnit() {
		return unit;
	}

	public void setUnit(Long unit) {
		this.unit = unit;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
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

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
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

	public String getCustomsGoodsRecordNo() {
		return customsGoodsRecordNo;
	}

	public void setCustomsGoodsRecordNo(String customsGoodsRecordNo) {
		this.customsGoodsRecordNo = customsGoodsRecordNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	public String getUnique() {
		return unique;
	}

	public void setUnique(String unique) {
		this.unique = unique;
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

	public Long getMerchandiseId() {
		return merchandiseId;
	}

	public void setMerchandiseId(Long merchandiseId) {
		this.merchandiseId = merchandiseId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
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

	

	public String getWarningType() {
		return warningType;
	}

	public void setWarningType(String warningType) {
		this.warningType = warningType;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
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

	public String getPackType() {
		return packType;
	}

	public void setPackType(String packType) {
		this.packType = packType;
	}

	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
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

	public String getOutDepotFlag() {
		return outDepotFlag;
	}

	public void setOutDepotFlag(String outDepotFlag) {
		this.outDepotFlag = outDepotFlag;
	}

	public String getEnglishGoodsName() {
		return englishGoodsName;
	}

	public void setEnglishGoodsName(String englishGoodsName) {
		this.englishGoodsName = englishGoodsName;
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

	public String getIsSelf() {
		return isSelf;
	}

	public void setIsSelf(String isSelf) {
		this.isSelf = isSelf;
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

	public String getImageUrl1() {
		return imageUrl1;
	}

	public void setImageUrl1(String imageUrl1) {
		this.imageUrl1 = imageUrl1;
	}

	public String getImageUrl2() {
		return imageUrl2;
	}

	public void setImageUrl2(String imageUrl2) {
		this.imageUrl2 = imageUrl2;
	}

	public String getImageUrl3() {
		return imageUrl3;
	}

	public void setImageUrl3(String imageUrl3) {
		this.imageUrl3 = imageUrl3;
	}

	public String getImageUrl4() {
		return imageUrl4;
	}

	public void setImageUrl4(String imageUrl4) {
		this.imageUrl4 = imageUrl4;
	}

	public String getImageUrl5() {
		return imageUrl5;
	}

	public void setImageUrl5(String imageUrl5) {
		this.imageUrl5 = imageUrl5;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public String getProductImg01() {
		return productImg01;
	}

	public void setProductImg01(String productImg01) {
		this.productImg01 = productImg01;
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
	
	
}
