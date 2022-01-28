package com.topideal.entity.vo.order;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

public class PromotionRecordModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 促销名称
    */
    private String promotionName;
    /**
    * 促销年份
    */
    private String promotionYear;
    /**
    * 开始日期
    */
    private Date startDate;
    /**
    * 结束日期
    */
    private Date endDate;
    /**
    * 天数
    */
    private Integer dayNum;
    /**
    * 促销投入
    */
    private BigDecimal promotionInvestment;
    /**
    * 是否删除：0-否 1-是
    */
    private String isDelete;
    /**
    * 电商平台编码：第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790
    */
    private String storePlatformCode;
    /**
    * 电商平台名称:第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790
    */
    private String storePlatformName;
    /**
    * 
    */
    private Timestamp createDate;
    /**
    * 
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
    /*promotionName get 方法 */
    public String getPromotionName(){
    return promotionName;
    }
    /*promotionName set 方法 */
    public void setPromotionName(String  promotionName){
    this.promotionName=promotionName;
    }
    /*promotionYear get 方法 */
    public String getPromotionYear(){
    return promotionYear;
    }
    /*promotionYear set 方法 */
    public void setPromotionYear(String  promotionYear){
    this.promotionYear=promotionYear;
    }
    /*startDate get 方法 */
    public Date getStartDate(){
    return startDate;
    }
    /*startDate set 方法 */
    public void setStartDate(Date  startDate){
    this.startDate=startDate;
    }
    /*endDate get 方法 */
    public Date getEndDate(){
    return endDate;
    }
    /*endDate set 方法 */
    public void setEndDate(Date  endDate){
    this.endDate=endDate;
    }
    /*dayNum get 方法 */
    public Integer getDayNum(){
    return dayNum;
    }
    /*dayNum set 方法 */
    public void setDayNum(Integer  dayNum){
    this.dayNum=dayNum;
    }
    /*promotionInvestment get 方法 */
    public BigDecimal getPromotionInvestment(){
    return promotionInvestment;
    }
    /*promotionInvestment set 方法 */
    public void setPromotionInvestment(BigDecimal  promotionInvestment){
    this.promotionInvestment=promotionInvestment;
    }
    /*isDelete get 方法 */
    public String getIsDelete(){
    return isDelete;
    }
    /*isDelete set 方法 */
    public void setIsDelete(String  isDelete){
    this.isDelete=isDelete;
    }
    /*storePlatformCode get 方法 */
    public String getStorePlatformCode(){
    return storePlatformCode;
    }
    /*storePlatformCode set 方法 */
    public void setStorePlatformCode(String  storePlatformCode){
    this.storePlatformCode=storePlatformCode;
    }
    /*storePlatformName get 方法 */
    public String getStorePlatformName(){
    return storePlatformName;
    }
    /*storePlatformName set 方法 */
    public void setStorePlatformName(String  storePlatformName){
    this.storePlatformName=storePlatformName;
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
