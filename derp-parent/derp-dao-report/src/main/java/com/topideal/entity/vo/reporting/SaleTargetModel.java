package com.topideal.entity.vo.reporting;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;

public class SaleTargetModel extends PageModel implements Serializable{

    /**
    * 自增ID
    */
    private Long id;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 月份
    */
    private String month;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 标准品牌
    */
    private String brandParent;
    /**
    * 品类
    */
    private String typeName;
    /**
    * To B销量
    */
    private Integer toBNum;
    /**
    * To C销量
    */
    private Integer toCNum;
    /**
    * 类型 1-按销售类型计划 2-按平台计划
    */
    private String type;
    /**
    * 电商平台编码
    */
    private String storePlatformName;
    /**
    * 平台计划销量
    */
    private Integer storePlatformNum;
    /**
    * 创建人ID
    */
    private Long creatorId;
    /**
    * 创建人
    */
    private String creator;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    // 当前登录用户绑定的事业部集合
    private List<Long> buIds;

    /**
     * 店铺编码
     */
    private String shopCode;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 店铺计划量
     */
    private Integer shopNum;

    public List<Long> getBuIds() {
        return buIds;
    }

    public void setBuIds(List<Long> buIds) {
        this.buIds = buIds;
    }

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*brandParent get 方法 */
    public String getBrandParent(){
    return brandParent;
    }
    /*brandParent set 方法 */
    public void setBrandParent(String  brandParent){
    this.brandParent=brandParent;
    }
    /*typeName get 方法 */
    public String getTypeName(){
    return typeName;
    }
    /*typeName set 方法 */
    public void setTypeName(String  typeName){
    this.typeName=typeName;
    }
    /*toBNum get 方法 */
    public Integer getToBNum(){
    return toBNum;
    }
    /*toBNum set 方法 */
    public void setToBNum(Integer  toBNum){
    this.toBNum=toBNum;
    }
    /*toCNum get 方法 */
    public Integer getToCNum(){
    return toCNum;
    }
    /*toCNum set 方法 */
    public void setToCNum(Integer  toCNum){
    this.toCNum=toCNum;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    }
    /*storePlatformName get 方法 */
    public String getStorePlatformName(){
    return storePlatformName;
    }
    /*storePlatformName set 方法 */
    public void setStorePlatformName(String  storePlatformName){
    this.storePlatformName=storePlatformName;
    }
    /*storePlatformNum get 方法 */
    public Integer getStorePlatformNum(){
    return storePlatformNum;
    }
    /*storePlatformNum set 方法 */
    public void setStorePlatformNum(Integer  storePlatformNum){
    this.storePlatformNum=storePlatformNum;
    }
    /*creatorId get 方法 */
    public Long getCreatorId(){
    return creatorId;
    }
    /*creatorId set 方法 */
    public void setCreatorId(Long  creatorId){
    this.creatorId=creatorId;
    }
    /*creator get 方法 */
    public String getCreator(){
    return creator;
    }
    /*creator set 方法 */
    public void setCreator(String  creator){
    this.creator=creator;
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

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getShopNum() {
        return shopNum;
    }

    public void setShopNum(Integer shopNum) {
        this.shopNum = shopNum;
    }
}
