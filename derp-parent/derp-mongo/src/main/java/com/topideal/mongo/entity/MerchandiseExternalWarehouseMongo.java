package com.topideal.mongo.entity;
import java.math.BigDecimal;

public class MerchandiseExternalWarehouseMongo{

    /**
    * ID
    */
    private Long merchandiseExternalWarehouseId;
    /**
    * 公司ID
    */
    private Long merchantId;
    /**
    * 公司名称
    */
    private String merchantName;
    /**
    * 平台商品货号
    */
    private String goodsNo;
    /**
    * 商品条形码
    */
    private String barcode;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品英文名称
    */
    private String englishGoodsName;
    /**
    * 平台备案关区ID
    */
    private Long customsAreaId;
    /**
    * HS编码
    */
    private String hsCode;
    /**
    * 工厂编码
    */
    private String factoryNo;
    /**
    * 品牌Brand id
    */
    private Long brandId;
    /**
    * 
    */
    private String brandName;
    /**
    * 来源商品id
    */
    private Long sourceGoodsId;
    /**
    * 数据来源 2-菜鸟 1-主数据 0-录入/导入
    */
    private String source;
    /**
    * 产品一级分类名称
    */
    private String productTypeName0;
    /**
    * 产品一级分类id
    */
    private Long productTypeId0;
    /**
    * 产品二级分类名称
    */
    private String productTypeName;
    /**
    * 产品二级分类id
    */
    private Long productTypeId;
    /**
    * 毛重
    */
    private Double grossWeight;
    /**
    * 净重
    */
    private Double netWeight;
    /**
    * 规格型号
    */
    private String spec;
    /**
    * 保质天数
    */
    private Integer shelfLifeDays;
    /**
    * 备案价格
    */
    private BigDecimal filingPrice;
    /**
    * 
    */
    private String unit;
    /**
    * 
    */
    private String countyName;
    /**
    * 原产国地区
    */
    private String countyArea;
    /**
    * 生产厂家
    */
    private String manufacturer;
    /**
    * 生产厂家地址
    */
    private String manufacturerAddr;
    /**
    * 海关商品备案号
    */
    private String customsGoodsRecordNo;
    /**
    * 金二项号
    */
    private String jinerxiang;
    /**
    * 第一法定单位
    */
    private String unitNameOne;
    /**
    * 第二法定单位
    */
    private String unitNameTwo;
    /**
    * sku编码
    */
    private String skuCode;
    /**
    * 品牌类型
    */
    private String brandType;
    /**
    * 账册备案料号
    */
    private String materialAccount;
    /**
    * 售卖商品名称（中文）
    */
    private String saleGoodNames;
    /**
    * EMS编码
    */
    private String emsCode;
    /**
    * 商品描述
    */
    private String describe;
    /**
    * 申报要素
    */
    private String declareFactor;
    /**
    * 产品成分
    */
    private String component;
    /**
    * 一箱多少件
    */
    private Integer boxToUnit;
    /**
    * 一托多少件
    */
    private Integer torrToUnit;
    /**
    * 长
    */
    private Double length;
    /**
    * 宽
    */
    private Double width;
    /**
    * 高
    */
    private Double height;
    /**
    * 体积
    */
    private Double volume;
    /**
    * 包装方式
    */
    private String packType;
    /**
    * 平台备案关区名字
    */
    private String customsAreaName;
    /**
     * 经分销商品货号
     */
    private String derpGoodsNo;
	 /**
	 * 经分销商品id
	 */
    private Long derpMerchandisId;
   
    public Long getMerchandiseExternalWarehouseId() {
		return merchandiseExternalWarehouseId;
	}
	public void setMerchandiseExternalWarehouseId(Long merchandiseExternalWarehouseId) {
		this.merchandiseExternalWarehouseId = merchandiseExternalWarehouseId;
	}
	/*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*englishGoodsName get 方法 */
    public String getEnglishGoodsName(){
    return englishGoodsName;
    }
    /*englishGoodsName set 方法 */
    public void setEnglishGoodsName(String  englishGoodsName){
    this.englishGoodsName=englishGoodsName;
    }
    /*customsAreaId get 方法 */
    public Long getCustomsAreaId(){
    return customsAreaId;
    }
    /*customsAreaId set 方法 */
    public void setCustomsAreaId(Long  customsAreaId){
    this.customsAreaId=customsAreaId;
    }
    /*hsCode get 方法 */
    public String getHsCode(){
    return hsCode;
    }
    /*hsCode set 方法 */
    public void setHsCode(String  hsCode){
    this.hsCode=hsCode;
    }
    /*factoryNo get 方法 */
    public String getFactoryNo(){
    return factoryNo;
    }
    /*factoryNo set 方法 */
    public void setFactoryNo(String  factoryNo){
    this.factoryNo=factoryNo;
    }
    /*brandId get 方法 */
    public Long getBrandId(){
    return brandId;
    }
    /*brandId set 方法 */
    public void setBrandId(Long  brandId){
    this.brandId=brandId;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*sourceGoodsId get 方法 */
    public Long getSourceGoodsId(){
    return sourceGoodsId;
    }
    /*sourceGoodsId set 方法 */
    public void setSourceGoodsId(Long  sourceGoodsId){
    this.sourceGoodsId=sourceGoodsId;
    }
    /*source get 方法 */
    public String getSource(){
    return source;
    }
    /*source set 方法 */
    public void setSource(String  source){
    this.source=source;
    }
    /*productTypeName0 get 方法 */
    public String getProductTypeName0(){
    return productTypeName0;
    }
    /*productTypeName0 set 方法 */
    public void setProductTypeName0(String  productTypeName0){
    this.productTypeName0=productTypeName0;
    }
    /*productTypeId0 get 方法 */
    public Long getProductTypeId0(){
    return productTypeId0;
    }
    /*productTypeId0 set 方法 */
    public void setProductTypeId0(Long  productTypeId0){
    this.productTypeId0=productTypeId0;
    }
    /*productTypeName get 方法 */
    public String getProductTypeName(){
    return productTypeName;
    }
    /*productTypeName set 方法 */
    public void setProductTypeName(String  productTypeName){
    this.productTypeName=productTypeName;
    }
    /*productTypeId get 方法 */
    public Long getProductTypeId(){
    return productTypeId;
    }
    /*productTypeId set 方法 */
    public void setProductTypeId(Long  productTypeId){
    this.productTypeId=productTypeId;
    }
    /*grossWeight get 方法 */
    public Double getGrossWeight(){
    return grossWeight;
    }
    /*grossWeight set 方法 */
    public void setGrossWeight(Double  grossWeight){
    this.grossWeight=grossWeight;
    }
    /*netWeight get 方法 */
    public Double getNetWeight(){
    return netWeight;
    }
    /*netWeight set 方法 */
    public void setNetWeight(Double  netWeight){
    this.netWeight=netWeight;
    }
    /*spec get 方法 */
    public String getSpec(){
    return spec;
    }
    /*spec set 方法 */
    public void setSpec(String  spec){
    this.spec=spec;
    }
    /*shelfLifeDays get 方法 */
    public Integer getShelfLifeDays(){
    return shelfLifeDays;
    }
    /*shelfLifeDays set 方法 */
    public void setShelfLifeDays(Integer  shelfLifeDays){
    this.shelfLifeDays=shelfLifeDays;
    }
    /*filingPrice get 方法 */
    public BigDecimal getFilingPrice(){
    return filingPrice;
    }
    /*filingPrice set 方法 */
    public void setFilingPrice(BigDecimal  filingPrice){
    this.filingPrice=filingPrice;
    }
    /*unit get 方法 */
    public String getUnit(){
    return unit;
    }
    /*unit set 方法 */
    public void setUnit(String  unit){
    this.unit=unit;
    }
    /*countyName get 方法 */
    public String getCountyName(){
    return countyName;
    }
    /*countyName set 方法 */
    public void setCountyName(String  countyName){
    this.countyName=countyName;
    }
    /*countyArea get 方法 */
    public String getCountyArea(){
    return countyArea;
    }
    /*countyArea set 方法 */
    public void setCountyArea(String  countyArea){
    this.countyArea=countyArea;
    }
    /*manufacturer get 方法 */
    public String getManufacturer(){
    return manufacturer;
    }
    /*manufacturer set 方法 */
    public void setManufacturer(String  manufacturer){
    this.manufacturer=manufacturer;
    }
    /*manufacturerAddr get 方法 */
    public String getManufacturerAddr(){
    return manufacturerAddr;
    }
    /*manufacturerAddr set 方法 */
    public void setManufacturerAddr(String  manufacturerAddr){
    this.manufacturerAddr=manufacturerAddr;
    }
    /*customsGoodsRecordNo get 方法 */
    public String getCustomsGoodsRecordNo(){
    return customsGoodsRecordNo;
    }
    /*customsGoodsRecordNo set 方法 */
    public void setCustomsGoodsRecordNo(String  customsGoodsRecordNo){
    this.customsGoodsRecordNo=customsGoodsRecordNo;
    }
    /*jinerxiang get 方法 */
    public String getJinerxiang(){
    return jinerxiang;
    }
    /*jinerxiang set 方法 */
    public void setJinerxiang(String  jinerxiang){
    this.jinerxiang=jinerxiang;
    }
    /*unitNameOne get 方法 */
    public String getUnitNameOne(){
    return unitNameOne;
    }
    /*unitNameOne set 方法 */
    public void setUnitNameOne(String  unitNameOne){
    this.unitNameOne=unitNameOne;
    }
    /*unitNameTwo get 方法 */
    public String getUnitNameTwo(){
    return unitNameTwo;
    }
    /*unitNameTwo set 方法 */
    public void setUnitNameTwo(String  unitNameTwo){
    this.unitNameTwo=unitNameTwo;
    }
    /*skuCode get 方法 */
    public String getSkuCode(){
    return skuCode;
    }
    /*skuCode set 方法 */
    public void setSkuCode(String  skuCode){
    this.skuCode=skuCode;
    }
    /*brandType get 方法 */
    public String getBrandType(){
    return brandType;
    }
    /*brandType set 方法 */
    public void setBrandType(String  brandType){
    this.brandType=brandType;
    }
    /*materialAccount get 方法 */
    public String getMaterialAccount(){
    return materialAccount;
    }
    /*materialAccount set 方法 */
    public void setMaterialAccount(String  materialAccount){
    this.materialAccount=materialAccount;
    }
    /*saleGoodNames get 方法 */
    public String getSaleGoodNames(){
    return saleGoodNames;
    }
    /*saleGoodNames set 方法 */
    public void setSaleGoodNames(String  saleGoodNames){
    this.saleGoodNames=saleGoodNames;
    }
    /*emsCode get 方法 */
    public String getEmsCode(){
    return emsCode;
    }
    /*emsCode set 方法 */
    public void setEmsCode(String  emsCode){
    this.emsCode=emsCode;
    }
    /*describe get 方法 */
    public String getDescribe(){
    return describe;
    }
    /*describe set 方法 */
    public void setDescribe(String  describe){
    this.describe=describe;
    }
    /*declareFactor get 方法 */
    public String getDeclareFactor(){
    return declareFactor;
    }
    /*declareFactor set 方法 */
    public void setDeclareFactor(String  declareFactor){
    this.declareFactor=declareFactor;
    }
    /*component get 方法 */
    public String getComponent(){
    return component;
    }
    /*component set 方法 */
    public void setComponent(String  component){
    this.component=component;
    }
    /*boxToUnit get 方法 */
    public Integer getBoxToUnit(){
    return boxToUnit;
    }
    /*boxToUnit set 方法 */
    public void setBoxToUnit(Integer  boxToUnit){
    this.boxToUnit=boxToUnit;
    }
    /*torrToUnit get 方法 */
    public Integer getTorrToUnit(){
    return torrToUnit;
    }
    /*torrToUnit set 方法 */
    public void setTorrToUnit(Integer  torrToUnit){
    this.torrToUnit=torrToUnit;
    }
    /*length get 方法 */
    public Double getLength(){
    return length;
    }
    /*length set 方法 */
    public void setLength(Double  length){
    this.length=length;
    }
    /*width get 方法 */
    public Double getWidth(){
    return width;
    }
    /*width set 方法 */
    public void setWidth(Double  width){
    this.width=width;
    }
    /*height get 方法 */
    public Double getHeight(){
    return height;
    }
    /*height set 方法 */
    public void setHeight(Double  height){
    this.height=height;
    }
    /*volume get 方法 */
    public Double getVolume(){
    return volume;
    }
    /*volume set 方法 */
    public void setVolume(Double  volume){
    this.volume=volume;
    }
    /*packType get 方法 */
    public String getPackType(){
    return packType;
    }
    /*packType set 方法 */
    public void setPackType(String  packType){
    this.packType=packType;
    }
    /*customsAreaName get 方法 */
    public String getCustomsAreaName(){
    return customsAreaName;
    }
    /*customsAreaName set 方法 */
    public void setCustomsAreaName(String  customsAreaName){
    this.customsAreaName=customsAreaName;
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
