package com.topideal.entity.vo.common;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class SdPurchaseConfigItemModel extends PageModel implements Serializable{

    /**
    * 自增ID
    */
    private Long id;
    /**
    * sd采购配置ID
    */
    private Long configId;
    /**
    * sd类型ID
    */
    private Long sdConfigId;
    /**
    * sd类型名称
    */
    private String sdConfigName;
    /**
    * sd类型简称
    */
    private String sdConfigSimpleName;
    /**
    * sd类型1-单比例 2-多比例
    */
    private String sdConfigSimpleType;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 商品名
    */
    private String goodsName;
    /**
    * 标准品牌
    */
    private String brandParent;
    /**
    * 比例
    */
    private BigDecimal proportion;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 创建人
    */
    private String creator;
    /**
    * 创建人ID
    */
    private Long creatorId;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*configId get 方法 */
    public Long getConfigId(){
    return configId;
    }
    /*configId set 方法 */
    public void setConfigId(Long  configId){
    this.configId=configId;
    }
    /*sdConfigId get 方法 */
    public Long getSdConfigId(){
    return sdConfigId;
    }
    /*sdConfigId set 方法 */
    public void setSdConfigId(Long  sdConfigId){
    this.sdConfigId=sdConfigId;
    }
    /*sdConfigName get 方法 */
    public String getSdConfigName(){
    return sdConfigName;
    }
    /*sdConfigName set 方法 */
    public void setSdConfigName(String  sdConfigName){
    this.sdConfigName=sdConfigName;
    }
    /*sdConfigSimpleName get 方法 */
    public String getSdConfigSimpleName(){
    return sdConfigSimpleName;
    }
    /*sdConfigSimpleName set 方法 */
    public void setSdConfigSimpleName(String  sdConfigSimpleName){
    this.sdConfigSimpleName=sdConfigSimpleName;
    }
    /*sdConfigSimpleType get 方法 */
    public String getSdConfigSimpleType(){
    return sdConfigSimpleType;
    }
    /*sdConfigSimpleType set 方法 */
    public void setSdConfigSimpleType(String  sdConfigSimpleType){
    this.sdConfigSimpleType=sdConfigSimpleType;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*brandParent get 方法 */
    public String getBrandParent(){
    return brandParent;
    }
    /*brandParent set 方法 */
    public void setBrandParent(String  brandParent){
    this.brandParent=brandParent;
    }
    /*proportion get 方法 */
    public BigDecimal getProportion(){
    return proportion;
    }
    /*proportion set 方法 */
    public void setProportion(BigDecimal  proportion){
    this.proportion=proportion;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*creator get 方法 */
    public String getCreator(){
    return creator;
    }
    /*creator set 方法 */
    public void setCreator(String  creator){
    this.creator=creator;
    }
    /*creatorId get 方法 */
    public Long getCreatorId(){
    return creatorId;
    }
    /*creatorId set 方法 */
    public void setCreatorId(Long  creatorId){
    this.creatorId=creatorId;
    }






}
