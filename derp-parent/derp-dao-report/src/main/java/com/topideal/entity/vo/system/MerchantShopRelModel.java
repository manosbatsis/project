package com.topideal.entity.vo.system;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

public class MerchantShopRelModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 商家id
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 卓志编码
    */
    private String topidealCode;
    /**
    * 店铺名称
    */
    private String shopName;
    /**
    * 店铺编码
    */
    private String shopCode;
    /**
    * 客户ID
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 状态(1使用中,0已禁用)
    */
    private String status;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 操作员
    */
    private String operator;
    /**
    * 仓库id
    */
    private Long depotId;
    /**
    * 仓库名称
    */
    private String depotName;
    /**
    * 订单是否拆解 0-是  1-否
    */
    private String isDismantle;
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
    /**
    * 店铺统一ID
    */
    private String shopunifyId;
    /**
    * 店铺类型值编码：DZD：单主店、WBD：外部店
    */
    private String storeTypeCode;
    /**
    * 店铺类型名称
    */
    private String storeTypeName;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
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

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    /*topidealCode get 方法 */
    public String getTopidealCode(){
    return topidealCode;
    }
    /*topidealCode set 方法 */
    public void setTopidealCode(String  topidealCode){
    this.topidealCode=topidealCode;
    }
    /*shopName get 方法 */
    public String getShopName(){
    return shopName;
    }
    /*shopName set 方法 */
    public void setShopName(String  shopName){
    this.shopName=shopName;
    }
    /*shopCode get 方法 */
    public String getShopCode(){
    return shopCode;
    }
    /*shopCode set 方法 */
    public void setShopCode(String  shopCode){
    this.shopCode=shopCode;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
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
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*operator get 方法 */
    public String getOperator(){
    return operator;
    }
    /*operator set 方法 */
    public void setOperator(String  operator){
    this.operator=operator;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
    }
    /*isDismantle get 方法 */
    public String getIsDismantle(){
    return isDismantle;
    }
    /*isDismantle set 方法 */
    public void setIsDismantle(String  isDismantle){
    this.isDismantle=isDismantle;
    }
    /*storePlatformCode get 方法 */
    public String getStorePlatformCode(){
    return storePlatformCode;
    }
    /*storePlatformCode set 方法 */
    public void setStorePlatformCode(String  storePlatformCode){
    this.storePlatformCode=storePlatformCode;
    }
    /*storePlatformName get 方法 */
    public String getStorePlatformName(){
    return storePlatformName;
    }
    /*storePlatformName set 方法 */
    public void setStorePlatformName(String  storePlatformName){
    this.storePlatformName=storePlatformName;
    }
    /*shopTypeCode get 方法 */
    public String getShopTypeCode(){
    return shopTypeCode;
    }
    /*shopTypeCode set 方法 */
    public void setShopTypeCode(String  shopTypeCode){
    this.shopTypeCode=shopTypeCode;
    }
    /*shopTypeName get 方法 */
    public String getShopTypeName(){
    return shopTypeName;
    }
    /*shopTypeName set 方法 */
    public void setShopTypeName(String  shopTypeName){
    this.shopTypeName=shopTypeName;
    }
    /*dataSourceCode get 方法 */
    public String getDataSourceCode(){
    return dataSourceCode;
    }
    /*dataSourceCode set 方法 */
    public void setDataSourceCode(String  dataSourceCode){
    this.dataSourceCode=dataSourceCode;
    }
    /*dataSourceName get 方法 */
    public String getDataSourceName(){
    return dataSourceName;
    }
    /*dataSourceName set 方法 */
    public void setDataSourceName(String  dataSourceName){
    this.dataSourceName=dataSourceName;
    }
    /*sessionKey get 方法 */
    public String getSessionKey(){
    return sessionKey;
    }
    /*sessionKey set 方法 */
    public void setSessionKey(String  sessionKey){
    this.sessionKey=sessionKey;
    }
    /*appKey get 方法 */
    public String getAppKey(){
    return appKey;
    }
    /*appKey set 方法 */
    public void setAppKey(String  appKey){
    this.appKey=appKey;
    }
    /*appSecret get 方法 */
    public String getAppSecret(){
    return appSecret;
    }
    /*appSecret set 方法 */
    public void setAppSecret(String  appSecret){
    this.appSecret=appSecret;
    }
    /*isSycnMerchandise get 方法 */
    public String getIsSycnMerchandise(){
    return isSycnMerchandise;
    }
    /*isSycnMerchandise set 方法 */
    public void setIsSycnMerchandise(String  isSycnMerchandise){
    this.isSycnMerchandise=isSycnMerchandise;
    }
    /*shopunifyId get 方法 */
    public String getShopunifyId(){
    return shopunifyId;
    }
    /*shopunifyId set 方法 */
    public void setShopunifyId(String  shopunifyId){
    this.shopunifyId=shopunifyId;
    }
    /*storeTypeCode get 方法 */
    public String getStoreTypeCode(){
    return storeTypeCode;
    }
    /*storeTypeCode set 方法 */
    public void setStoreTypeCode(String  storeTypeCode){
    this.storeTypeCode=storeTypeCode;
    }
    /*storeTypeName get 方法 */
    public String getStoreTypeName(){
    return storeTypeName;
    }
    /*storeTypeName set 方法 */
    public void setStoreTypeName(String  storeTypeName){
    this.storeTypeName=storeTypeName;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
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
}
