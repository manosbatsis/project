package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 发送邮件配置表
 * @author 杨创
 *
 */
public class EmailConfigModel extends PageModel implements Serializable{

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
    * 供应商id
    */
    private Long customerId;
    /**
    * 供应商名称
    */
    private String customerName;
    /**
    * 付款账期 天数
    */
    private Integer accountPeriodDay;
    /**
    * 提前提醒天数 多个天数用逗号隔开
    */
    private String advanceReminderDay;
    /**
    * 账期单位 1 自然日, 2工作日
    */
    private String accountUnitType;
    /**
    * 提醒单位 1 自然日, 2工作日
    */
    private String reminderUnitType;
    /**
    * 提醒类型 1付款提醒
    */
    private String reminderType;
    /**
    * 基准时间类型 1发票时间
    */
    private String baseTimeType;
    /**
    * 状态(1启用,0禁用)
    */
    private String status;
    /**
    * 创建日期
    */
    private Timestamp createDate;
    /**
    * 修改日期
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
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
    }
    /*accountPeriodDay get 方法 */
    public Integer getAccountPeriodDay(){
    return accountPeriodDay;
    }
    /*accountPeriodDay set 方法 */
    public void setAccountPeriodDay(Integer  accountPeriodDay){
    this.accountPeriodDay=accountPeriodDay;
    }
    /*advanceReminderDay get 方法 */
    public String getAdvanceReminderDay(){
    return advanceReminderDay;
    }
    /*advanceReminderDay set 方法 */
    public void setAdvanceReminderDay(String  advanceReminderDay){
    this.advanceReminderDay=advanceReminderDay;
    }
    /*accountUnitType get 方法 */
    public String getAccountUnitType(){
    return accountUnitType;
    }
    /*accountUnitType set 方法 */
    public void setAccountUnitType(String  accountUnitType){
    this.accountUnitType=accountUnitType;
    }
    /*reminderUnitType get 方法 */
    public String getReminderUnitType(){
    return reminderUnitType;
    }
    /*reminderUnitType set 方法 */
    public void setReminderUnitType(String  reminderUnitType){
    this.reminderUnitType=reminderUnitType;
    }
    /*reminderType get 方法 */
    public String getReminderType(){
    return reminderType;
    }
    /*reminderType set 方法 */
    public void setReminderType(String  reminderType){
    this.reminderType=reminderType;
    }
    /*baseTimeType get 方法 */
    public String getBaseTimeType(){
    return baseTimeType;
    }
    /*baseTimeType set 方法 */
    public void setBaseTimeType(String  baseTimeType){
    this.baseTimeType=baseTimeType;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
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
