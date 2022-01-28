package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class InventoryAdjustmentDetailModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 单号
    */
    private String adjustmentInventoryCode;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 业务类别 4-唯品红冲 5-国检抽样 6-唯品报废  7-库存盘亏 8-库存盘盈
    */
    private String model;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 仓库id
    */
    private Long depotId;
    /**
    * 仓库名称
    */
    private String depotName;
    /**
    * 归属月份
    */
    private String months;
    /**
    * 调整时间
    */
    private Timestamp adjustmentTime;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    //po单时间
    private Timestamp poDate;

    //库存/减库存类型：0：减库存1：加库存
    private String type;

    //创建人ID
    private Long creater;

    //创建人用户名
    private String createName;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*adjustmentInventoryCode get 方法 */
    public String getAdjustmentInventoryCode(){
    return adjustmentInventoryCode;
    }
    /*adjustmentInventoryCode set 方法 */
    public void setAdjustmentInventoryCode(String  adjustmentInventoryCode){
    this.adjustmentInventoryCode=adjustmentInventoryCode;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*model get 方法 */
    public String getModel(){
    return model;
    }
    /*model set 方法 */
    public void setModel(String  model){
    this.model=model;
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
    /*months get 方法 */
    public String getMonths(){
    return months;
    }
    /*months set 方法 */
    public void setMonths(String  months){
    this.months=months;
    }
    /*adjustmentTime get 方法 */
    public Timestamp getAdjustmentTime(){
    return adjustmentTime;
    }
    /*adjustmentTime set 方法 */
    public void setAdjustmentTime(Timestamp  adjustmentTime){
    this.adjustmentTime=adjustmentTime;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
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
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
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

    public Timestamp getPoDate() {
        return poDate;
    }

    public void setPoDate(Timestamp poDate) {
        this.poDate = poDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
