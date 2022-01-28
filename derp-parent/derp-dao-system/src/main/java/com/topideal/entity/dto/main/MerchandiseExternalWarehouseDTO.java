package com.topideal.entity.dto.main;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 外仓备案商品列表
 */
public class MerchandiseExternalWarehouseDTO extends PageModel implements Serializable {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;
    /**
     * 公司ID
     */
    @ApiModelProperty(value = "公司ID")
    private Long merchantId;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String merchantName;
    /**
     * 平台商品货号
     */
    @ApiModelProperty(value = "平台商品货号")
    private String goodsNo;
    /**
     * 商品条形码
     */
    @ApiModelProperty(value = "商品条形码")
    private String barcode;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 商品英文名称
     */
    @ApiModelProperty(value = "商品英文名称")
    private String englishGoodsName;
    /**
     * HS编码
     */
    @ApiModelProperty(value = "HS编码")
    private String hsCode;
    /**
     * 工厂编码
     */
    @ApiModelProperty(value = "工厂编码")
    private String factoryNo;
    /**
     * 品牌Brand id
     */
    @ApiModelProperty(value = "品牌Brand id")
    private Long brandId;
    /**
     * 品牌名字
     */
    @ApiModelProperty(value = "品牌名字")
    private String brandName;
    /**
     * 来源商品id
     */
    @ApiModelProperty(value = "来源商品id")
    private Long sourceGoodsId;
    /**
     * 数据来源 2-菜鸟 1-主数据 0-录入/导入
     */
    @ApiModelProperty(value = "数据来源 2-菜鸟 1-主数据 0-录入/导入")
    private String source;
    private String sourceLabel;
    /**
     * 产品一级分类名称
     */
    @ApiModelProperty(value = "产品一级分类名称")
    private String productTypeName0;

    /**
     * 产品二级分类名称
     */
    @ApiModelProperty(value = "产品二级分类名称")
    private String productTypeName;

    /**
     * 毛重
     */
    @ApiModelProperty(value = "毛重")
    private Double grossWeight;
    /**
     * 净重
     */
    @ApiModelProperty(value = "净重")
    private Double netWeight;
    /**
     * 规格型号
     */
    @ApiModelProperty(value = "规格型号")
    private String spec;
    /**
     * 保质天数
     */
    @ApiModelProperty(value = "保质天数")
    private Integer shelfLifeDays;
    /**
     * 备案价格
     */
    @ApiModelProperty(value = "备案价格")
    private BigDecimal filingPrice;
    /**
     * 标准单位
     */
    @ApiModelProperty(value = "标准单位")
    private String unit;
    /**
     * 原产国
     */
    @ApiModelProperty(value = "原产国")
    private String countyName;
    /**
     * 原产国地区
     */
    @ApiModelProperty(value = "原产国地区")
    private String countyArea;
    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;
    /**
     * 生产厂家地址
     */
    @ApiModelProperty(value = "生产厂家地址")
    private String manufacturerAddr;
    /**
     * 海关商品备案号
     */
    @ApiModelProperty(value = "海关商品备案号")
    private String customsGoodsRecordNo;
    /**
     * 金二项号
     */
    @ApiModelProperty(value = "金二项号")
    private String jinerxiang;
    /**
     * 第一法定单位
     */
    @ApiModelProperty(value = "第一法定单位")
    private String unitNameOne;
    /**
     * 第二法定单位
     */
    @ApiModelProperty(value = "第二法定单位")
    private String unitNameTwo;
    /**
     * sku编码
     */
    @ApiModelProperty(value = "sku编码")
    private String skuCode;
    /**
     * 品牌类型
     */
    @ApiModelProperty(value = "品牌类型")
    private String brandType;
    /**
     * 账册备案料号
     */
    @ApiModelProperty(value = "账册备案料号")
    private String materialAccount;
    /**
     * 售卖商品名称（中文）
     */
    @ApiModelProperty(value = "售卖商品名称（中文）")
    private String saleGoodNames;
    /**
     * EMS编码
     */
    @ApiModelProperty(value = "EMS编码")
    private String emsCode;
    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    private String describe;
    /**
     * 申报要素
     */
    @ApiModelProperty(value = "申报要素")
    private String declareFactor;
    /**
     * 产品成分
     */
    @ApiModelProperty(value = "产品成分")
    private String component;
    /**
     * 一箱多少件
     */
    @ApiModelProperty(value = "一箱多少件")
    private String boxToUnit;
    /**
     * 一托多少件
     */
    @ApiModelProperty(value = "一托多少件")
    private String torrToUnit;
    /**
     * 长
     */
    @ApiModelProperty(value = "长")
    private Double length;
    /**
     * 宽
     */
    @ApiModelProperty(value = "宽")
    private Double width;
    /**
     * 高
     */
    @ApiModelProperty(value = "高")
    private Double height;
    /**
     * 体积
     */
    @ApiModelProperty(value = "体积")
    private String volume;
    /**
     * 包装方式
     */
    @ApiModelProperty(value = "包装方式")
    private String packType;
    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
    private Long creater;
    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
    private String createName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
     * 修改人id
     */
    @ApiModelProperty(value = "修改人id")
    private Long modifier;
    /**
     * 修改人名称
     */
    @ApiModelProperty(value = "修改人名称")
    private String modifiyName;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
    /**
     * 平台备案关区名字
     */
    @ApiModelProperty(value = "平台备案关区名字")
    private String customsAreaName;

    @ApiModelProperty(value = "平台备案关区ID")
    private Long customsAreaId;

    /*
     *  产品一级分类id
     */
    @ApiModelProperty(value = "产品一级分类id")
    private Long productTypeId0;
    /**
     * 产品二级分类id
     */
    @ApiModelProperty(value = "产品二级分类id")
    private Long productTypeId;

    /**
     * 经分销商品货号
     */
    @ApiModelProperty(value = "经分销商品货号")
    private String derpGoodsNo;

    /**
     * 经分销商品Id
     */
    @ApiModelProperty(value = "经分销商品Id")
    private Long derpMerchandisId;

    private String ids;

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

    public Long getCustomsAreaId() {
        return customsAreaId;
    }

    public void setCustomsAreaId(Long customsAreaId) {
        this.customsAreaId = customsAreaId;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getEnglishGoodsName() {
        return englishGoodsName;
    }

    public void setEnglishGoodsName(String englishGoodsName) {
        this.englishGoodsName = englishGoodsName;
    }


    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getFactoryNo() {
        return factoryNo;
    }

    public void setFactoryNo(String factoryNo) {
        this.factoryNo = factoryNo;
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

    public Long getSourceGoodsId() {
        return sourceGoodsId;
    }

    public void setSourceGoodsId(Long sourceGoodsId) {
        this.sourceGoodsId = sourceGoodsId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
        this.sourceLabel= DERP_SYS.getLabelByKey(DERP_SYS.merchandiseWarehouse_sourceList,source);
    }

    public String getSourceLabel() {
        return sourceLabel;
    }

    public void setSourceLabel(String sourceLabel) {
        this.sourceLabel = sourceLabel;
    }

    public String getProductTypeName0() {
        return productTypeName0;
    }

    public void setProductTypeName0(String productTypeName0) {
        this.productTypeName0 = productTypeName0;
    }


    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
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

    public BigDecimal getFilingPrice() {
        return filingPrice;
    }

    public void setFilingPrice(BigDecimal filingPrice) {
        this.filingPrice = filingPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyArea() {
        return countyArea;
    }

    public void setCountyArea(String countyArea) {
        this.countyArea = countyArea;
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

    public String getCustomsGoodsRecordNo() {
        return customsGoodsRecordNo;
    }

    public void setCustomsGoodsRecordNo(String customsGoodsRecordNo) {
        this.customsGoodsRecordNo = customsGoodsRecordNo;
    }

    public String getJinerxiang() {
        return jinerxiang;
    }

    public void setJinerxiang(String jinerxiang) {
        this.jinerxiang = jinerxiang;
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

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getBrandType() {
        return brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }

    public String getMaterialAccount() {
        return materialAccount;
    }

    public void setMaterialAccount(String materialAccount) {
        this.materialAccount = materialAccount;
    }

    public String getSaleGoodNames() {
        return saleGoodNames;
    }

    public void setSaleGoodNames(String saleGoodNames) {
        this.saleGoodNames = saleGoodNames;
    }

    public String getEmsCode() {
        return emsCode;
    }

    public void setEmsCode(String emsCode) {
        this.emsCode = emsCode;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public String getBoxToUnit() {
        return boxToUnit;
    }

    public void setBoxToUnit(String boxToUnit) {
        this.boxToUnit = boxToUnit;
    }

    public String getTorrToUnit() {
        return torrToUnit;
    }

    public void setTorrToUnit(String torrToUnit) {
        this.torrToUnit = torrToUnit;
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

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public String getModifiyName() {
        return modifiyName;
    }

    public void setModifiyName(String modifiyName) {
        this.modifiyName = modifiyName;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCustomsAreaName() {
        return customsAreaName;
    }

    public void setCustomsAreaName(String customsAreaName) {
        this.customsAreaName = customsAreaName;
    }

    public String getDerpGoodsNo() {
        return derpGoodsNo;
    }

    public void setDerpGoodsNo(String derpGoodsNo) {
        this.derpGoodsNo = derpGoodsNo;
    }

    public Long getDerpMerchandisId() {
        return derpMerchandisId;
    }

    public void setDerpMerchandisId(Long derpMerchandisId) {
        this.derpMerchandisId = derpMerchandisId;
    }
}
