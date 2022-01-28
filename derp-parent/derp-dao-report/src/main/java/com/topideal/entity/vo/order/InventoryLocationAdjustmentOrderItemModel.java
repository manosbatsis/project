package com.topideal.entity.vo.order;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class InventoryLocationAdjustmentOrderItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 库位调整单id
    */
    private Long inventoryLocationId;
    /**
    * 关联外部单号
    */
    private String externalCode;
    /**
    * 系统订单号
    */
    private String orderCode;
    /**
    * 调增商品ID
    */
    private Long increaseGoodsId;
    /**
    * 调增商品货号
    */
    private String increaseGoodsNo;
    /**
    * 调增商品编码
    */
    private String increaseGoodsCode;
    /**
    * 调增商品名称
    */
    private String increaseGoodsName;
    /**
    * 调减商品ID
    */
    private Long reduceGoodsId;
    /**
    * 调减商品货号
    */
    private String reduceGoodsNo;
    /**
    * 调减商品编码
    */
    private String reduceGoodsCode;
    /**
    * 调减商品名称
    */
    private String reduceGoodsName;
    /**
    * 调整数量
    */
    private Integer adjustNum;
    /**
    * 库存类型 0：好品 1：坏品
    */
    private String inventoryType;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    /**
     * 平台编号
     */
    private String platformCode;
    /**
     * 平台名称
     */
    private String platformName;
    /**
     * 店铺编号
     */
    private String shopCode;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 理货单位 00-托盘 01-箱 02-件
     */
    private String tallyingUnit;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*inventoryLocationId get 方法 */
    public Long getInventoryLocationId(){
    return inventoryLocationId;
    }
    /*inventoryLocationId set 方法 */
    public void setInventoryLocationId(Long  inventoryLocationId){
    this.inventoryLocationId=inventoryLocationId;
    }
    /*externalCode get 方法 */
    public String getExternalCode(){
    return externalCode;
    }
    /*externalCode set 方法 */
    public void setExternalCode(String  externalCode){
    this.externalCode=externalCode;
    }
    /*orderCode get 方法 */
    public String getOrderCode(){
    return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
    this.orderCode=orderCode;
    }
    /*increaseGoodsId get 方法 */
    public Long getIncreaseGoodsId(){
    return increaseGoodsId;
    }
    /*increaseGoodsId set 方法 */
    public void setIncreaseGoodsId(Long  increaseGoodsId){
    this.increaseGoodsId=increaseGoodsId;
    }
    /*increaseGoodsNo get 方法 */
    public String getIncreaseGoodsNo(){
    return increaseGoodsNo;
    }
    /*increaseGoodsNo set 方法 */
    public void setIncreaseGoodsNo(String  increaseGoodsNo){
    this.increaseGoodsNo=increaseGoodsNo;
    }
    /*increaseGoodsCode get 方法 */
    public String getIncreaseGoodsCode(){
    return increaseGoodsCode;
    }
    /*increaseGoodsCode set 方法 */
    public void setIncreaseGoodsCode(String  increaseGoodsCode){
    this.increaseGoodsCode=increaseGoodsCode;
    }
    /*increaseGoodsName get 方法 */
    public String getIncreaseGoodsName(){
    return increaseGoodsName;
    }
    /*increaseGoodsName set 方法 */
    public void setIncreaseGoodsName(String  increaseGoodsName){
    this.increaseGoodsName=increaseGoodsName;
    }
    /*reduceGoodsId get 方法 */
    public Long getReduceGoodsId(){
    return reduceGoodsId;
    }
    /*reduceGoodsId set 方法 */
    public void setReduceGoodsId(Long  reduceGoodsId){
    this.reduceGoodsId=reduceGoodsId;
    }
    /*reduceGoodsNo get 方法 */
    public String getReduceGoodsNo(){
    return reduceGoodsNo;
    }
    /*reduceGoodsNo set 方法 */
    public void setReduceGoodsNo(String  reduceGoodsNo){
    this.reduceGoodsNo=reduceGoodsNo;
    }
    /*reduceGoodsCode get 方法 */
    public String getReduceGoodsCode(){
    return reduceGoodsCode;
    }
    /*reduceGoodsCode set 方法 */
    public void setReduceGoodsCode(String  reduceGoodsCode){
    this.reduceGoodsCode=reduceGoodsCode;
    }
    /*reduceGoodsName get 方法 */
    public String getReduceGoodsName(){
    return reduceGoodsName;
    }
    /*reduceGoodsName set 方法 */
    public void setReduceGoodsName(String  reduceGoodsName){
    this.reduceGoodsName=reduceGoodsName;
    }
    /*adjustNum get 方法 */
    public Integer getAdjustNum(){
    return adjustNum;
    }
    /*adjustNum set 方法 */
    public void setAdjustNum(Integer  adjustNum){
    this.adjustNum=adjustNum;
    }
    /*inventoryType get 方法 */
    public String getInventoryType(){
    return inventoryType;
    }
    /*inventoryType set 方法 */
    public void setInventoryType(String  inventoryType){
    this.inventoryType=inventoryType;
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
    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
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

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }
}
