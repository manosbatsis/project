package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

/**
 * 进销存汇总表
 * @author lian_
 *
 */
public class InventoryInvoicingModel extends PageModel implements Serializable{

     //仓库名称
     private String depotName;
     //当月实时库存数量
     private Integer currentMonthQty;
     //仓库id
     private Long depotId;
     //工厂编码
     private String factoryNo;
     //商家名称
     private String merchantName;
     //商家ID
     private Long merchantId;
     //单价
     private BigDecimal price;
     //创建人
     private Long creater;
     //id
     private Long id;
     //上月实时库存数量
     private Integer lastMonthQty;
     //商品名称
     private String goodsName;
     //商品货号
     private String barcode;
     //创建时间
     private Timestamp createDate;

    /*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
    /*currentMonthQty get 方法 */
    public Integer getCurrentMonthQty(){
        return currentMonthQty;
    }
    /*currentMonthQty set 方法 */
    public void setCurrentMonthQty(Integer  currentMonthQty){
        this.currentMonthQty=currentMonthQty;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*factoryNo get 方法 */
    public String getFactoryNo(){
        return factoryNo;
    }
    /*factoryNo set 方法 */
    public void setFactoryNo(String  factoryNo){
        this.factoryNo=factoryNo;
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
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*lastMonthQty get 方法 */
    public Integer getLastMonthQty(){
        return lastMonthQty;
    }
    /*lastMonthQty set 方法 */
    public void setLastMonthQty(Integer  lastMonthQty){
        this.lastMonthQty=lastMonthQty;
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

