package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 采销报价
 * @author lian_
 */
public class MiningMarketingPriceModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //品牌名称
     private String brandName;
     //报价生效时间
     private Timestamp effectDate;
     //修改时间
     private Timestamp modifyDate;
     //商品id
     private Long goodsId;
     //备注
     private String remark;
     //报价失效时间
     private Timestamp invalidDate;
     //供给客户名称
     private String customerName;
     //规格型号
     private String goodsSpec;
     //创建人
     private String createrName;
     //供货价
     private BigDecimal price;
     //供给客户id
     private Long customerId;
     //供货报价币种
     private String currency;
     //id
     private Long id;
     //修改人
     private String modifierName;
     //商品名称
     private String goodsName;
     //商品条形码
     private String barcode;
     //创建时间
     private Timestamp createDate;

    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*brandName get 方法 */
    public String getBrandName(){
        return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
        this.brandName=brandName;
    }
    /*effectDate get 方法 */
    public Timestamp getEffectDate(){
        return effectDate;
    }
    /*effectDate set 方法 */
    public void setEffectDate(Timestamp  effectDate){
        this.effectDate=effectDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*remark get 方法 */
    public String getRemark(){
        return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
        this.remark=remark;
    }
    /*invalidDate get 方法 */
    public Timestamp getInvalidDate(){
        return invalidDate;
    }
    /*invalidDate set 方法 */
    public void setInvalidDate(Timestamp  invalidDate){
        this.invalidDate=invalidDate;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
        return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
        this.customerName=customerName;
    }
    /*goodsSpec get 方法 */
    public String getGoodsSpec(){
        return goodsSpec;
    }
    /*goodsSpec set 方法 */
    public void setGoodsSpec(String  goodsSpec){
        this.goodsSpec=goodsSpec;
    }
    /*createrName get 方法 */
    public String getCreaterName(){
        return createrName;
    }
    /*createrName set 方法 */
    public void setCreaterName(String  createrName){
        this.createrName=createrName;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
        return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
        this.price=price;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
        return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
        this.customerId=customerId;
    }
    /*currency get 方法 */
    public String getCurrency(){
        return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
        this.currency=currency;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*modifierName get 方法 */
    public String getModifierName(){
        return modifierName;
    }
    /*modifierName set 方法 */
    public void setModifierName(String  modifierName){
        this.modifierName=modifierName;
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
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }






}

