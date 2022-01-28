package com.topideal.entity.vo.main;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class MerchandiseCustomsRelModel extends PageModel implements Serializable{

    /**
    * 自增主键
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
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 关区ID
    */
    private Long customsAreaId;
    /**
    * 关区代码
    */
    private String customsAreaCode;
    /**
    * 关区名称
    */
    private String customsAreaName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

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
    /*customsAreaId get 方法 */
    public Long getCustomsAreaId(){
    return customsAreaId;
    }
    /*customsAreaId set 方法 */
    public void setCustomsAreaId(Long  customsAreaId){
    this.customsAreaId=customsAreaId;
    }
    /*customsAreaCode get 方法 */
    public String getCustomsAreaCode(){
    return customsAreaCode;
    }
    /*customsAreaCode set 方法 */
    public void setCustomsAreaCode(String  customsAreaCode){
    this.customsAreaCode=customsAreaCode;
    }
    /*customsAreaName get 方法 */
    public String getCustomsAreaName(){
    return customsAreaName;
    }
    /*customsAreaName set 方法 */
    public void setCustomsAreaName(String  customsAreaName){
    this.customsAreaName=customsAreaName;
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






}
