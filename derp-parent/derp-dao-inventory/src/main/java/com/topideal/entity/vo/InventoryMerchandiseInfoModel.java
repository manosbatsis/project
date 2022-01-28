package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

/**
 * 库存商品信息
 * @author lian_
 */
public class InventoryMerchandiseInfoModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //商品id
     private Long goodsId;
     //工厂编码
     private String factoryNo;
     //是否备案(1-是，0-否)
     private String isRecord;
     //数据来源 1主数据
     private String source;
     //商家名称
     private String merchantName;
     //商家ID
     private Long merchantId;
     //价格
     private BigDecimal price;
     //商品名称
     private String name;
     //id
     private Long id;
     //条形码
     private String barcode;
     //是否自有(1-是，0-否)
     private String isSelf;
     //状态(1启用,0禁用)
     private String status;

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
    /*factoryNo get 方法 */
    public String getFactoryNo(){
        return factoryNo;
    }
    /*factoryNo set 方法 */
    public void setFactoryNo(String  factoryNo){
        this.factoryNo=factoryNo;
    }
    /*isRecord get 方法 */
    public String getIsRecord(){
        return isRecord;
    }
    /*isRecord set 方法 */
    public void setIsRecord(String  isRecord){
        this.isRecord=isRecord;
    }
    /*source get 方法 */
    public String getSource(){
        return source;
    }
    /*source set 方法 */
    public void setSource(String  source){
        this.source=source;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
        return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
        this.price=price;
    }
    /*name get 方法 */
    public String getName(){
        return name;
    }
    /*name set 方法 */
    public void setName(String  name){
        this.name=name;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*barcode get 方法 */
    public String getBarcode(){
        return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
        this.barcode=barcode;
    }
    /*isSelf get 方法 */
    public String getIsSelf(){
        return isSelf;
    }
    /*isSelf set 方法 */
    public void setIsSelf(String  isSelf){
        this.isSelf=isSelf;
    }
    /*status get 方法 */
    public String getStatus(){
        return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
    }






}

