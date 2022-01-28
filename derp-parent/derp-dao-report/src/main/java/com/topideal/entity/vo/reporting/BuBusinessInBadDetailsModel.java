package com.topideal.entity.vo.reporting;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

public class BuBusinessInBadDetailsModel extends PageModel implements Serializable{

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
    * 调入仓库id
    */
    private Long inDepotId;
    /**
    * 调入仓库名称
    */
    private String inDepotName;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 1-采购入库 2-调拨入库 3-退货入库 4-上架入库单
    */
    private String orderType;
    /**
    * 订单id
    */
    private Long orderId;
    /**
    * 订单编码
    */
    private String orderCode;
    /**
    * 入库库订单id
    */
    private Long inOrderId;
    /**
    * 入库订单编码
    */
    private String inOrderCode;
    /**
    * 入库时间
    */
    private Timestamp deliverDate;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品条码
    */
    private String barcode;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 0-托盘，1-箱，2-件
    */
    private String tallyingUnit;
    /**
    * 归属月份
    */
    private String month;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 上级母品牌
     */
    private String superiorParentBrand;

    /**
     * 库位类型id
     */
    private Long stockLocationTypeId;
    /**
     * 库位类型名称
     */
    private String stockLocationTypeName;

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
    /*inDepotId get 方法 */
    public Long getInDepotId(){
    return inDepotId;
    }
    /*inDepotId set 方法 */
    public void setInDepotId(Long  inDepotId){
    this.inDepotId=inDepotId;
    }
    /*inDepotName get 方法 */
    public String getInDepotName(){
    return inDepotName;
    }
    /*inDepotName set 方法 */
    public void setInDepotName(String  inDepotName){
    this.inDepotName=inDepotName;
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
    /*orderType get 方法 */
    public String getOrderType(){
    return orderType;
    }
    /*orderType set 方法 */
    public void setOrderType(String  orderType){
    this.orderType=orderType;
    }
    /*orderId get 方法 */
    public Long getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
    this.orderId=orderId;
    }
    /*orderCode get 方法 */
    public String getOrderCode(){
    return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
    this.orderCode=orderCode;
    }
    /*inOrderId get 方法 */
    public Long getInOrderId(){
    return inOrderId;
    }
    /*inOrderId set 方法 */
    public void setInOrderId(Long  inOrderId){
    this.inOrderId=inOrderId;
    }
    /*inOrderCode get 方法 */
    public String getInOrderCode(){
    return inOrderCode;
    }
    /*inOrderCode set 方法 */
    public void setInOrderCode(String  inOrderCode){
    this.inOrderCode=inOrderCode;
    }
    /*deliverDate get 方法 */
    public Timestamp getDeliverDate(){
    return deliverDate;
    }
    /*deliverDate set 方法 */
    public void setDeliverDate(Timestamp  deliverDate){
    this.deliverDate=deliverDate;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
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
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    this.tallyingUnit=tallyingUnit;
    }
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
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

    public String getSuperiorParentBrand() {
        return superiorParentBrand;
    }

    public void setSuperiorParentBrand(String superiorParentBrand) {
        this.superiorParentBrand = superiorParentBrand;
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
