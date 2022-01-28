package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class SaleCreditOrderItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 销售赊销单ID
    */
    private Long creditOrderId;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 商品编码
    */
    private String goodsCode;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 销售数量
    */
    private Integer num;
    /**
    * 销售单价
    */
    private BigDecimal price;
    /**
    * 销售金额
    */
    private BigDecimal amount;
    /**
    * 本金
    */
    private BigDecimal principal;
    /**
    * 保证金
    */
    private BigDecimal marginAmount;
    /**
    * 到期单价
    */
    private BigDecimal expirePrice;
    /**
    * 到期金额
    */
    private BigDecimal expireAmount;
    /**
    * 创建时间
    */
    private Timestamp createDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*creditOrderId get 方法 */
    public Long getCreditOrderId(){
    return creditOrderId;
    }
    /*creditOrderId set 方法 */
    public void setCreditOrderId(Long  creditOrderId){
    this.creditOrderId=creditOrderId;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsCode get 方法 */
    public String getGoodsCode(){
    return goodsCode;
    }
    /*goodsCode set 方法 */
    public void setGoodsCode(String  goodsCode){
    this.goodsCode=goodsCode;
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
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
    }
    /*principal get 方法 */
    public BigDecimal getPrincipal(){
    return principal;
    }
    /*principal set 方法 */
    public void setPrincipal(BigDecimal  principal){
    this.principal=principal;
    }
    /*marginAmount get 方法 */
    public BigDecimal getMarginAmount(){
    return marginAmount;
    }
    /*marginAmount set 方法 */
    public void setMarginAmount(BigDecimal  marginAmount){
    this.marginAmount=marginAmount;
    }
    /*expirePrice get 方法 */
    public BigDecimal getExpirePrice(){
    return expirePrice;
    }
    /*expirePrice set 方法 */
    public void setExpirePrice(BigDecimal  expirePrice){
    this.expirePrice=expirePrice;
    }
    /*expireAmount get 方法 */
    public BigDecimal getExpireAmount(){
    return expireAmount;
    }
    /*expireAmount set 方法 */
    public void setExpireAmount(BigDecimal  expireAmount){
    this.expireAmount=expireAmount;
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
