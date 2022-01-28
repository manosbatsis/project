package com.topideal.entity.dto;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class VipTransferDepotDetailsDTO extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    private Long id;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名
    */
    private String merchantName;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 货号
    */
    private String goodsNo;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 调拨订单
    */
    private String transferOrder;
    /**
    * 调拨出\入库单号
    */
    private String transferDepotOrder;
    /**
    * 调拨数量
    */
    private Integer transferNum;
    /**
    * 调拨类型
    */
    private String transferType;
    /**
    * 调拨时间
    */
    private Timestamp transferTime;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    /**事业部id*/
    private Long buId;
    /**客户ID*/
    private Long customerId;

    private List<Long> buList;

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
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*transferOrder get 方法 */
    public String getTransferOrder(){
    return transferOrder;
    }
    /*transferOrder set 方法 */
    public void setTransferOrder(String  transferOrder){
    this.transferOrder=transferOrder;
    }
    /*transferDepotOrder get 方法 */
    public String getTransferDepotOrder(){
    return transferDepotOrder;
    }
    /*transferDepotOrder set 方法 */
    public void setTransferDepotOrder(String  transferDepotOrder){
    this.transferDepotOrder=transferDepotOrder;
    }
    /*transferNum get 方法 */
    public Integer getTransferNum(){
    return transferNum;
    }
    /*transferNum set 方法 */
    public void setTransferNum(Integer  transferNum){
    this.transferNum=transferNum;
    }
    /*transferType get 方法 */
    public String getTransferType(){
    return transferType;
    }
    /*transferType set 方法 */
    public void setTransferType(String  transferType){
    this.transferType=transferType;
    }
    /*transferTime get 方法 */
    public Timestamp getTransferTime(){
    return transferTime;
    }
    /*transferTime set 方法 */
    public void setTransferTime(Timestamp  transferTime){
    this.transferTime=transferTime;
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

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }
}
