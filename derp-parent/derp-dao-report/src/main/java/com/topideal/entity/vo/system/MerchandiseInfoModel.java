package com.topideal.entity.vo.system;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 商品信息
 * 
 * @author lchenxing
 */
public class MerchandiseInfoModel extends PageModel implements Serializable {

	// 商品货号
	private String goodsNo;
	// 自编码
	private String code;
	// 修改时间
	private Timestamp modifyDate;
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
	private String uniques;
	// 商品名称
	private String name;
	// 最大安全库存
	private Integer maxStock;
	// id
	private Long id;
	// 商品编码
	private String goodsCode;
	// 描述
	private String describe;
	// 创建时间
	private Timestamp createDate;
	// 备案价格
	private BigDecimal filingPrice;
	// 工厂编码
	private String factoryNo;
	// 包装方式
	private String packType;
	// 预警类型  1、分仓预警 2全仓预警
	private String warningType;
	// 创建人
	private Long creater;
	//商家名称
    private String merchantName;
    //商家ID
    private Long merchantId;
    //是否备案(1-是，0-否)
    private String isRecord;
    //是否组合(1-是，0-否)
    private String isGroup;
    //修改人用户名
    private String updateName;
    //创建人用户名
    private String createName;
    //是否自有(1-是，0-否)
    private String isSelf;
    //商品条形码
    private String barcode;
    //状态(1使用中,0已禁用)
    private String status;
    //图片修改日期
    private Timestamp imageModifyDate;
    //商品图片
    private String merchandiseImage;
    //图片修改人id
    private Long imageId;
    //图片修改人名称
    private String imageName;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 一箱多少件
    */
    private Integer boxToUnit;
    /**
    * 一托多少件
    */
    private Integer torrToUnit;
    //英文名称
    private String englishGoodsName;

	// -------------- 拓展字段
	// 产品名称
	private String productName;
	// 品牌id
	private Long brandId;
	// 品牌名称
	private String brandName;
	// 原产国id
	private Long countyId;
	// 原产国
	private String countyName;
	// 计量单位id
	private Long unit;
	// 计量单位名称
	private String unitName;
	// 商品二级分类ID
	private Long productTypeId;
	// 商品一级级分类ID
	private Long productTypeId0;
	// 商品二级分类名称
	private String productTypeName;
	// 商品一级分类名称
	private String productTypeName0;

	// 商品分类名称
	private String goodsClassifyName;
	// 保质天数
	private Integer shelfLifeDays;
	// 商品描述
	private String spec;
	// 毛重
	private Double grossWeight;
	// 净重
	private Double netWeight;
	// hs编码
	private String hsCode;
	// 长
	private Double length;
	// 宽
	private Double width;
	// 高
	private Double height;
	// 体积
	private Double volume;
	// 主图
	private String productImg01;
	// 生产厂家
	private String manufacturer;
	// 生产厂家地址
	private String manufacturerAddr;
	// 产品颜色
	private String color;
	// 产品大小
	private Double size;
	// 产品成分
	private String component;
	// 产品备注
	private String productRemark;
	// 仓库id
	private Long depotId;
	// 储存选择过的id
	private List ids;
	// 储存商家ids
	private List merchantIds;
	// 价目表下限
	private BigDecimal supplyMinPrice;
	// 云集商家id
	private Long cloudMerchantId;
	//创建人名称
	private String createrName;
	//修改人名称
	private String modifierName;
	//供应商价目表id
	private Long supplierId;
	// 组合商品中某个商品的数量
	private Long groupNum;     
	// 组合商品中某个商品的价格
	private Double groupPrice;

	//外仓唯一码
	private String outDepotFlag;

	//租户编码
	private String tenantCode;
	//版本号
	private Integer version;
	//来源商品id
	private Long sourceGoodsId;
	/**
	 * 关区ID
	 */
	private Long customsAreaId;
	/**
	 * 原产国地区
	 */
	private String countyArea;
	/**
	 * 申报要素
	 */
	private String declareFactor;
	/**
	 * 海关商品备案号
	 */
	private String customsGoodsRecordNo;

	/**
	 * 第一法定单位
	 */
	private String unitNameOne;
	/**
	 * 第二法定单位
	 */
	private String unitNameTwo;
	/**
	 * 商品图片1
	 */
	private String imageUrl1;
	/**
	 * 商品图片2
	 */
	private String imageUrl2;
	/**
	 * 商品图片3
	 */
	private String imageUrl3;
	/**
	 * 商品图片4
	 */
	private String imageUrl4;
	/**
	 * 商品图片5
	 */
	private String imageUrl5;

	public Long getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(Long groupNum) {
		this.groupNum = groupNum;
	}
		
	
	
    public Double getGroupPrice() {
			return groupPrice;
		}
		public void setGroupPrice(Double groupPrice) {
			this.groupPrice = groupPrice;
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
	}

	public String getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
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
}
