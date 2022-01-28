package com.topideal.entity.vo.order;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class ModelSettingModel extends PageModel implements Serializable{

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
    * 货期（天）
    */
    private Integer deliveryPeriod;
    /**
    * 采购周期（天）
    */
    private Integer purchasingCycle;
    /**
    * 生产周期（天）
    */
    private Integer produceCycle;
    /**
    * 安全库存天数
    */
    private Integer safetyStockDays;
    /**
    * 预警天数
    */
    private Integer warnDays;
    /**
    * 日销统计类型: 0-平常日 1-平常日+促销日
    */
    private String dailyStatisticalType;
    /**
    * 日销统计范围: 0-1个月 1-3个月 2-6个月 3-年初至今
    */
    private String dailyStatisticalRange;
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
    /*deliveryPeriod get 方法 */
    public Integer getDeliveryPeriod(){
    return deliveryPeriod;
    }
    /*deliveryPeriod set 方法 */
    public void setDeliveryPeriod(Integer  deliveryPeriod){
    this.deliveryPeriod=deliveryPeriod;
    }
    /*purchasingCycle get 方法 */
    public Integer getPurchasingCycle(){
    return purchasingCycle;
    }
    /*purchasingCycle set 方法 */
    public void setPurchasingCycle(Integer  purchasingCycle){
    this.purchasingCycle=purchasingCycle;
    }
    /*produceCycle get 方法 */
    public Integer getProduceCycle(){
    return produceCycle;
    }
    /*produceCycle set 方法 */
    public void setProduceCycle(Integer  produceCycle){
    this.produceCycle=produceCycle;
    }
    /*safetyStockDays get 方法 */
    public Integer getSafetyStockDays(){
    return safetyStockDays;
    }
    /*safetyStockDays set 方法 */
    public void setSafetyStockDays(Integer  safetyStockDays){
    this.safetyStockDays=safetyStockDays;
    }
    /*warnDays get 方法 */
    public Integer getWarnDays(){
    return warnDays;
    }
    /*warnDays set 方法 */
    public void setWarnDays(Integer  warnDays){
    this.warnDays=warnDays;
    }
    /*dailyStatisticalType get 方法 */
    public String getDailyStatisticalType(){
    return dailyStatisticalType;
    }
    /*dailyStatisticalType set 方法 */
    public void setDailyStatisticalType(String  dailyStatisticalType){
    this.dailyStatisticalType=dailyStatisticalType;
    }
    /*dailyStatisticalRange get 方法 */
    public String getDailyStatisticalRange(){
    return dailyStatisticalRange;
    }
    /*dailyStatisticalRange set 方法 */
    public void setDailyStatisticalRange(String  dailyStatisticalRange){
    this.dailyStatisticalRange=dailyStatisticalRange;
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
