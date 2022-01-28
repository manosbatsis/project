package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class LocationAdjustmentOrderModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 库位调整单号
    */
    private String code;
    /**
    * 调整单据类型
    */
    private String orderType;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 仓库id
    */
    private Long depotId;
    /**
    * 仓库名称
    */
    private String depotName;
    /**
    * 客户ID(供应商)
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 平台编号
    */
    private String platformCode;
    /**
    * 平台名称
    */
    private String platformName;
    /**
    * 订单号
    */
    private String orderCode;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 调整数量
    */
    private Integer adjustNum;
    /**
    * 库存类型 0：好品 1：坏品
    */
    private String inventoryType;
    /**
     * 调增库位类型id
     */
    private Long inStockLocationTypeId;
    /**
     * 调增库位类型名称
     */
    private String inStockLocationTypeName;
    /**
     * 调减库位类型id
     */
    private Long outStockLocationTypeId;
    /**
     * 调减库位类型
     */
    private String outStockLocationTypeName;
    /**
    * 归属月份
    */
    private String month;
    /**
    * 状态
    */
    private String status;
    /**
    * 调整原因
    */
    private String reason;
    /**
    * 创建人id
    */
    private Long creater;
    /**
    * 创建人名称
    */
    private String createName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    //理货单位 00-托盘 01-箱 02-件
    private String tallyingUnit;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /*orderType get 方法 */
    public String getOrderType(){
    return orderType;
    }
    /*orderType set 方法 */
    public void setOrderType(String  orderType){
    this.orderType=orderType;
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
    /*platformCode get 方法 */
    public String getPlatformCode(){
    return platformCode;
    }
    /*platformCode set 方法 */
    public void setPlatformCode(String  platformCode){
    this.platformCode=platformCode;
    }
    /*platformName get 方法 */
    public String getPlatformName(){
    return platformName;
    }
    /*platformName set 方法 */
    public void setPlatformName(String  platformName){
    this.platformName=platformName;
    }
    /*orderCode get 方法 */
    public String getOrderCode(){
    return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
    this.orderCode=orderCode;
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

    public Long getInStockLocationTypeId() {
        return inStockLocationTypeId;
    }

    public void setInStockLocationTypeId(Long inStockLocationTypeId) {
        this.inStockLocationTypeId = inStockLocationTypeId;
    }

    public String getInStockLocationTypeName() {
        return inStockLocationTypeName;
    }

    public void setInStockLocationTypeName(String inStockLocationTypeName) {
        this.inStockLocationTypeName = inStockLocationTypeName;
    }

    public Long getOutStockLocationTypeId() {
        return outStockLocationTypeId;
    }

    public void setOutStockLocationTypeId(Long outStockLocationTypeId) {
        this.outStockLocationTypeId = outStockLocationTypeId;
    }

    public String getOutStockLocationTypeName() {
        return outStockLocationTypeName;
    }

    public void setOutStockLocationTypeName(String outStockLocationTypeName) {
        this.outStockLocationTypeName = outStockLocationTypeName;
    }

    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }
}
