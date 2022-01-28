package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商家店铺关联表
 * @author lian_
 *
 */
public class MerchantShopRelModel extends PageModel implements Serializable{

    /**  
    * 店铺编码
    */
    private String shopCode;
    /**
    * 修改时间               
    */
    private Timestamp modifyDate;
    /**
    * 店铺名称
    */
    private String shopName;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 操作员
    */
    private String operator;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 卓志编码
    */
    private String topidealCode;
    /**
    * 商家id
    */
    private Long merchantId;
    /**
    * 客户ID
    */
    private Long customerId;
    /**
    * id
    */
    private Long id;
    /**
    * 状态(1使用中,0已禁用)
    */
    private String status;
    /**
    * 创建时间
    */
    private Timestamp createDate;    
    private Long depotId;//仓库id
    private String depotName;// 仓库名称

    /**
     * 电商平台编码
     */
    private String storePlatformCode;
    /**
     * 电商平台名称
     */
    private String storePlatformName;
    /**
     * 运营类型值编码:001-POP、002-一件代发
     */
    private String shopTypeCode;
    /**
     * 运营类型名称
     */
    private String shopTypeName;
    /**
     * 数据来源编码
     */
    private String dataSourceCode;
    /**
     * 数据来源名称
     */
    private String dataSourceName;
    /**
     * 菜鸟查询接口session_key
     */
    private String sessionKey;
    /**
     * 菜鸟查询接口app_key
     */
    private String appKey;
    /**
     * 菜鸟查询接口app_secret
     */
    private String appSecret;
    /**
     * 是否同步菜鸟商品 1-是 0-否
     */
    private String isSycnMerchandise;

    //店铺统一ID
    private String shopUnifyId;

    //店铺类型值编码：DZD：单主店、WBD：外部店
    private String storeTypeCode;


    //店铺类型名称
    private String storeTypeName;

    private String isDismantle;

    //事业部id
    private Integer buId;
    //事业部名称
    private String buName;

    /**
     * 专营母品牌id
     */
    private Long superiorParentBrandId;
    /**
     * 专营母品牌名称
     */
    private String superiorParentBrandName;
    /**
     * 专营母品牌NC编码
     */
    private String superiorParentBrandNcCode;
    /**
	 * 币种
	 */
	 private String currency;

    /**
     * 事业部库位类型ID
     */
    private Long stockLocationTypeId;

    /**
     * 事业部库位类型名称
     */
    private String stockLocationTypeName;

    public String getIsDismantle() {
        return isDismantle;
    }

    public void setIsDismantle(String isDismantle) {
        this.isDismantle = isDismantle;
    }

    /*shopCode get 方法 */
    public String getShopCode(){
        return shopCode;
    }
    /*shopCode set 方法 */
    public void setShopCode(String  shopCode){
        this.shopCode=shopCode;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*shopName get 方法 */
    public String getShopName(){
        return shopName;
    }
    /*shopName set 方法 */
    public void setShopName(String  shopName){
        this.shopName=shopName;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
        return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
        this.customerName=customerName;
    }
    /*operator get 方法 */
    public String getOperator(){
        return operator;
    }
    /*operator set 方法 */
    public void setOperator(String  operator){
        this.operator=operator;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*topidealCode get 方法 */
    public String getTopidealCode(){
        return topidealCode;
    }
    /*topidealCode set 方法 */
    public void setTopidealCode(String  topidealCode){
        this.topidealCode=topidealCode;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
        return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
        this.customerId=customerId;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*status get 方法 */
    public String getStatus(){
        return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

    public String getStorePlatformCode() {
        return storePlatformCode;
    }

    public void setStorePlatformCode(String storePlatformCode) {
        this.storePlatformCode = storePlatformCode;
    }

    public String getStorePlatformName() {
        return storePlatformName;
    }

    public void setStorePlatformName(String storePlatformName) {
        this.storePlatformName = storePlatformName;
    }

    public String getShopTypeCode() {
        return shopTypeCode;
    }

    public void setShopTypeCode(String shopTypeCode) {
        this.shopTypeCode = shopTypeCode;
    }

    public String getShopTypeName() {
        return shopTypeName;
    }

    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }

    public String getDataSourceCode() {
        return dataSourceCode;
    }

    public void setDataSourceCode(String dataSourceCode) {
        this.dataSourceCode = dataSourceCode;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getIsSycnMerchandise() {
        return isSycnMerchandise;
    }

    public void setIsSycnMerchandise(String isSycnMerchandise) {
        this.isSycnMerchandise = isSycnMerchandise;
    }

    public String getShopUnifyId() {
        return shopUnifyId;
    }

    public void setShopUnifyId(String shopUnifyId) {
        this.shopUnifyId = shopUnifyId;
    }

    public String getStoreTypeCode() {
        return storeTypeCode;
    }

    public void setStoreTypeCode(String storeTypeCode) {
        this.storeTypeCode = storeTypeCode;
    }

    public String getStoreTypeName() {
        return storeTypeName;
    }

    public void setStoreTypeName(String storeTypeName) {
        this.storeTypeName = storeTypeName;
    }

    public Integer getBuId() {
        return buId;
    }

    public void setBuId(Integer buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Long getSuperiorParentBrandId() {
        return superiorParentBrandId;
    }

    public void setSuperiorParentBrandId(Long superiorParentBrandId) {
        this.superiorParentBrandId = superiorParentBrandId;
    }

    public String getSuperiorParentBrandName() {
        return superiorParentBrandName;
    }

    public void setSuperiorParentBrandName(String superiorParentBrandName) {
        this.superiorParentBrandName = superiorParentBrandName;
    }

    public String getSuperiorParentBrandNcCode() {
        return superiorParentBrandNcCode;
    }

    public void setSuperiorParentBrandNcCode(String superiorParentBrandNcCode) {
        this.superiorParentBrandNcCode = superiorParentBrandNcCode;
    }
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

    public Long getStockLocationTypeId() {
        return stockLocationTypeId;
    }

    public void setStockLocationTypeId(Long stockLocationTypeId) {
        this.stockLocationTypeId = stockLocationTypeId;
    }

    public String getStockLocationTypeName() {
        return stockLocationTypeName;
    }

    public void setStockLocationTypeName(String stockLocationTypeName) {
        this.stockLocationTypeName = stockLocationTypeName;
    }
}
