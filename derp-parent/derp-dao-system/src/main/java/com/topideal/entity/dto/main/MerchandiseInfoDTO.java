package com.topideal.entity.dto.main;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 商品信息
 * 
 * @author lchenxing
 */
public class MerchandiseInfoDTO extends PageModel implements Serializable {

	@ApiModelProperty(value = "商品货号")
	private String goodsNo;
	@ApiModelProperty(value = "自编码")
	private String code;
	@ApiModelProperty(value = "修改时间")
	private Timestamp modifyDate;
	@ApiModelProperty(value = "产品Id")
	private Long productId;
	@ApiModelProperty(value = "修改人")
	private Long modifier;
	@ApiModelProperty(value = "备注")
	private String remark; 
	@ApiModelProperty(value = "数据来源 1主数据")
	private String source;
	@ApiModelProperty(value = "sourceLabel")
	private String sourceLabel;
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
	@ApiModelProperty(value = "描述")
	private String describe;	
	@ApiModelProperty(value = "创建时间")
	private Timestamp createDate;
	@ApiModelProperty(value = "备案价格")
	private BigDecimal filingPrice;
	@ApiModelProperty(value = "工厂编码")
	private String factoryNo;
	@ApiModelProperty(value = "包装方式")
	private String packType;
	@ApiModelProperty(value = "预警类型  1、分仓预警 2全仓预警")
	private String warningType;
	@ApiModelProperty(value = "warningTypeLabel")
	private String warningTypeLabel;
	@ApiModelProperty(value = "创建人")
	private Long creater;
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
	@ApiModelProperty(value = "是否备案(1-是，0-否)")
    private String isRecord;
	@ApiModelProperty(value = "isRecordLabel")
    private String isRecordLabel;
	@ApiModelProperty(value = "是否组合(1-是，0-否)")
    private String isGroup;
    @ApiModelProperty(value = "isGroupLabel")
    private String isGroupLabel;
    //修改人用户名
    @ApiModelProperty(value = "修改人用户名")
    private String updateName;
    @ApiModelProperty(value = "创建人用户名")
    private String createName;
    @ApiModelProperty(value = "是否自有(1-是，0-否)")
    private String isSelf;
    @ApiModelProperty(value = "isSelfLabel")
    private String isSelfLabel;
    @ApiModelProperty(value = "商品条形码")
    private String barcode;
    @ApiModelProperty(value = "状态(1使用中,0已禁用)")
    private String status;
    @ApiModelProperty(value = "statusLabel")
    private String statusLabel;
    @ApiModelProperty(value = "图片修改日期")
    private Timestamp imageModifyDate;
    @ApiModelProperty(value = "商品图片")
    private String merchandiseImage;
    @ApiModelProperty(value = "图片修改人id")
    private Long imageId;
    @ApiModelProperty(value = "图片修改人名称")
    private String imageName;
    @ApiModelProperty(value = "标准条码")
    private String commbarcode;
    @ApiModelProperty(value = "一箱多少件")
    private Integer boxToUnit;
    @ApiModelProperty(value = "一托多少件")
    private Integer torrToUnit;
    @ApiModelProperty(value = "英文名称")
    private String englishGoodsName;

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
    @ApiModelProperty(value = "商品描述")
	private String spec;
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
    @ApiModelProperty(value = "主图")
	private String productImg01;
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
    @ApiModelProperty(value = "产品备注")
	private String productRemark;
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
    @ApiModelProperty(value = "创建人名称")
	private String createrName;
    @ApiModelProperty(value = "修改人名称")
	private String modifierName;
    @ApiModelProperty(value = "供应商价目表id")
	private Long supplierId;
    @ApiModelProperty(value = "外仓统一码")
	private String outDepotFlag;
    @ApiModelProperty(value = "outDepotFlagLabel")
	private String outDepotFlagLabel;
    @ApiModelProperty(value = "租户编码")
	private String tenantCode;
    @ApiModelProperty(value = "版本号")
	private Integer version;
    @ApiModelProperty(value = "来源商品id")
	private Long sourceGoodsId;
    @ApiModelProperty(value = "关区ID")
	private Long customsAreaId;
    @ApiModelProperty(value = "原产国地区")
	private String countyArea;
    @ApiModelProperty(value = "申报要素")
	private String declareFactor;
    @ApiModelProperty(value = "海关商品备案号")
	private String customsGoodsRecordNo;
    @ApiModelProperty(value = "申报系数")
	private Double priceDeclareRatio;
    @ApiModelProperty(value = "第一法定单位")
	private String unitNameOne;
    @ApiModelProperty(value = "第二法定单位")
	private String unitNameTwo;
    @ApiModelProperty(value = "商品图片1")
	private String imageUrl1;
    @ApiModelProperty(value = "商品图片2")
	private String imageUrl2;
    @ApiModelProperty(value = "商品图片3")
	private String imageUrl3;
    @ApiModelProperty(value = "商品图片4")
	private String imageUrl4;
    @ApiModelProperty(value = "商品图片5")
	private String imageUrl5;
    @ApiModelProperty(value = "关区名称")
	private String customsAreaNames;
    @ApiModelProperty(value = "原产国")
	private String countryName;
    @ApiModelProperty(value = "需要展示的商品id")
    private String needIds;
    @ApiModelProperty(value = "商品仓库关系表仓库id 多个用逗号隔开（页面回显）")
    private String depotIds;
    @ApiModelProperty(value = "商品仓库关系表仓库名称 多个用逗号隔开（页面回显）")
    private String depotNames;
	@ApiModelProperty(value = "条码是否模糊查询：0-是 1-否")
	private String isBlur;

    public String getNeedIds() {
		return needIds;
	}
	public void setNeedIds(String needIds) {
		this.needIds = needIds;
	}		
    public String getCustomsAreaNames() {
		return customsAreaNames;
	}
	public void setCustomsAreaNames(String customsAreaNames) {
		this.customsAreaNames = customsAreaNames;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
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
	public String getCommbarcode() {
		return commbarcode;
	}
	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}
	public Timestamp getImageModifyDate() {
		return imageModifyDate;
	}
	public void setImageModifyDate(Timestamp imageModifyDate) {
		this.imageModifyDate = imageModifyDate;
	}
	public String getMerchandiseImage() {
		return merchandiseImage;
	}
	public void setMerchandiseImage(String merchandiseImage) {
		this.merchandiseImage = merchandiseImage;
	}
	public Long getImageId() {
		return imageId;
	}
	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	/*status get 方法 */
    public String getStatus(){
        return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
        if(status != null) {
        	this.statusLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchandiseInfo_statusList, status);
        }
    }
	public String getModifierName() {
		return modifierName;
	}
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public Long getCloudMerchantId() {
		return cloudMerchantId;
	}
	public void setCloudMerchantId(Long cloudMerchantId) {
		this.cloudMerchantId = cloudMerchantId;
	}
	public List getMerchantIds() {
		return merchantIds;
	}
	public void setMerchantIds(List merchantIds) {
		this.merchantIds = merchantIds;
	}
	/*createName get 方法 */
    public String getCreateName(){
        return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
        this.createName=createName;
    }
    /*isSelf get 方法 */
    public String getIsSelf(){
        return isSelf;
    }
    /*isSelf set 方法 */
    public void setIsSelf(String  isSelf){
        this.isSelf=isSelf;
        if(isSelf != null) {
        	 this.isSelfLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchandiseInfo_isSelfList,isSelf);
        }
    }
    /*updateName get 方法 */
    public String getUpdateName(){
        return updateName;
    }
    /*updateName set 方法 */
    public void setUpdateName(String  updateName){
        this.updateName=updateName;
    }
	public String getIsRecord() {
		return isRecord;
	}

	public void setIsRecord(String isRecord) {
		this.isRecord = isRecord;
		if(isRecord != null) {
			this.isRecordLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchandiseInfo_isRecordList, isRecord);
		}
	}

	public String getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
		if(isGroupLabel != null) {
			this.isGroupLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchandiseInfo_isGroupList, isGroup);
		}
	}

	public BigDecimal getSupplyMinPrice() {
		return supplyMinPrice;
	}

	public void setSupplyMinPrice(BigDecimal supplyMinPrice) {
		this.supplyMinPrice = supplyMinPrice;
	}

	public List getIds() {
		return ids;
	}

	public void setIds(List ids) {
		this.ids = ids;
	}

	public Long getDepotId() {
		return depotId;
	}

	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getWarningType() {
		return warningType;
	}

	public void setWarningType(String warningType) {
		this.warningType = warningType;
		if(warningType != null) {
			this.warningTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchandiseInfo_warningTypeList, warningType);
		}
	}

	public String getProductRemark() {
		return productRemark;
	}

	public void setProductRemark(String productRemark) {
		this.productRemark = productRemark;
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

	public String getProductImg01() {
		return productImg01;
	}

	public void setProductImg01(String productImg01) {
		this.productImg01 = productImg01;
	}

    /*packType get 方法 */
    public String getPackType(){
        return packType;
    }
    /*packType set 方法 */
    public void setPackType(String  packType){
        this.packType=packType;
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

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
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

	public Long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
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

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
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

	/* goodsNo get 方法 */
	public String getGoodsNo() {
		return goodsNo;
	}

	/* goodsNo set 方法 */
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* modifyDate get 方法 */
	public Timestamp getModifyDate() {
		return modifyDate;
	}

	/* modifyDate set 方法 */
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	/* productId get 方法 */
	public Long getProductId() {
		return productId;
	}

	/* productId set 方法 */
	public void setProductId(Long productId) {
		this.productId = productId;
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

	/* source get 方法 */
	public String getSource() {
		return source;
	}

	/* source set 方法 */
	public void setSource(String source) {
		this.source = source;
		if(source != null) {
			this.sourceLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchandiseInfo_sourceList, source);
		}
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/* minStock get 方法 */
	public Integer getMinStock() {
		return minStock;
	}

	/* minStock set 方法 */
	public void setMinStock(Integer minStock) {
		this.minStock = minStock;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/* unique get 方法 */
	public String getUniques() {
		return uniques;
	}

	/* unique set 方法 */
	public void setUniques(String uniques) {
		this.uniques = uniques;
	}

	/* name get 方法 */
	public String getName() {
		return name;
	}

	/* name set 方法 */
	public void setName(String name) {
		this.name = name;
	}

	/* maxStock get 方法 */
	public Integer getMaxStock() {
		return maxStock;
	}

	/* maxStock set 方法 */
	public void setMaxStock(Integer maxStock) {
		this.maxStock = maxStock;
	}
	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* goodsCode get 方法 */
	public String getGoodsCode() {
		return goodsCode;
	}

	/* goodsCode set 方法 */
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	/* describe get 方法 */
	public String getDescribe() {
		return describe;
	}

	/* describe set 方法 */
	public void setDescribe(String describe) {
		this.describe = describe;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
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

	public String getOutDepotFlag() {
		return outDepotFlag;
	}

	public void setOutDepotFlag(String outDepotFlag) {
		this.outDepotFlag = outDepotFlag;
		if(outDepotFlag != null) {
			this.outDepotFlagLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchandiseInfo_outDepotFlagList, outDepotFlag);
		}
	}
	public String getSourceLabel() {
		return sourceLabel;
	}
	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}
	public String getWarningTypeLabel() {
		return warningTypeLabel;
	}
	public void setWarningTypeLabel(String warningTypeLabel) {
		this.warningTypeLabel = warningTypeLabel;
	}
	public String getIsRecordLabel() {
		return isRecordLabel;
	}
	public void setIsRecordLabel(String isRecordLabel) {
		this.isRecordLabel = isRecordLabel;
	}
	public String getIsGroupLabel() {
		return isGroupLabel;
	}
	public void setIsGroupLabel(String isGroupLabel) {
		this.isGroupLabel = isGroupLabel;
	}
	public String getIsSelfLabel() {
		return isSelfLabel;
	}
	public void setIsSelfLabel(String isSelfLabel) {
		this.isSelfLabel = isSelfLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getOutDepotFlagLabel() {
		return outDepotFlagLabel;
	}
	public void setOutDepotFlagLabel(String outDepotFlagLabel) {
		this.outDepotFlagLabel = outDepotFlagLabel;
	}
	public String getEnglishGoodsName() {
		return englishGoodsName;
	}
	public void setEnglishGoodsName(String englishGoodsName) {
		this.englishGoodsName = englishGoodsName;
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

	public Long getCustomsAreaId() {
		return customsAreaId;
	}

	public void setCustomsAreaId(Long customsAreaId) {
		this.customsAreaId = customsAreaId;
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

	public Double getPriceDeclareRatio() {
		return priceDeclareRatio;
	}

	public void setPriceDeclareRatio(Double priceDeclareRatio) {
		this.priceDeclareRatio = priceDeclareRatio;
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
	public String getDepotIds() {
		return depotIds;
	}
	public void setDepotIds(String depotIds) {
		this.depotIds = depotIds;
	}
	public String getDepotNames() {
		return depotNames;
	}
	public void setDepotNames(String depotNames) {
		this.depotNames = depotNames;
	}

	public String getIsBlur() {
		return isBlur;
	}

	public void setIsBlur(String isBlur) {
		this.isBlur = isBlur;
	}
}
