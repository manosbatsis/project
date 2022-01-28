package com.topideal.entity.vo.reporting;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class VipNondifferenceCheckModel extends PageModel implements Serializable{

    /**
    * 
    */
    private Long id;
    /**
    * 月份
    */
    private String month;
    /**
    * 平台
    */
    private String platform;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名
    */
    private String merchantName;
    /**
    * po号
    */
    private String poNo;
    /**
    * 爬虫账单号
    */
    private String crawlerNo;
    /**
    * 处理类型
    */
    private String crawlerType;
    /**
    * 唯品SKU
    */
    private String crawlerGoodsNo;
    /**
    * 出库清单单号
    */
    private String orderCode;
    /**
    * 单据类型
    */
    private String orderType;
    /**
    * 出库清单id
    */
    private Long orderId;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 处理数量
    */
    private Integer account;
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
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*platform get 方法 */
    public String getPlatform(){
    return platform;
    }
    /*platform set 方法 */
    public void setPlatform(String  platform){
    this.platform=platform;
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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*crawlerNo get 方法 */
    public String getCrawlerNo(){
    return crawlerNo;
    }
    /*crawlerNo set 方法 */
    public void setCrawlerNo(String  crawlerNo){
    this.crawlerNo=crawlerNo;
    }
    /*crawlerType get 方法 */
    public String getCrawlerType(){
    return crawlerType;
    }
    /*crawlerType set 方法 */
    public void setCrawlerType(String  crawlerType){
    this.crawlerType=crawlerType;
    }
    /*crawlerGoodsNo get 方法 */
    public String getCrawlerGoodsNo(){
    return crawlerGoodsNo;
    }
    /*crawlerGoodsNo set 方法 */
    public void setCrawlerGoodsNo(String  crawlerGoodsNo){
    this.crawlerGoodsNo=crawlerGoodsNo;
    }
    /*orderCode get 方法 */
    public String getOrderCode(){
    return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
    this.orderCode=orderCode;
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
    /*account get 方法 */
    public Integer getAccount(){
    return account;
    }
    /*account set 方法 */
    public void setAccount(Integer  account){
    this.account=account;
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
