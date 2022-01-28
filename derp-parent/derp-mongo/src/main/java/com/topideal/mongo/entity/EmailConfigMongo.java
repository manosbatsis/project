package com.topideal.mongo.entity;

/**
 * 邮件发送配置表
 * @author 杨创
 *2019-06-18
 */
public class EmailConfigMongo {
	
    private Long emailId;//邮件发送配置id
    private Long merchantId;//商家ID
    private String merchantName;//商家名称
    private Long customerId;//供应商id
    private String customerName;//供应商名称
    private Integer accountPeriodDay;//付款账期 天数
    private String advanceReminderDay;//提前提醒天数  多个用英文逗号隔开
    private String accountUnitType;//账期单位 1 自然日, 2工作日
    private String reminderUnitType;//提醒单位 1 自然日, 2工作日
    private String reminderType;//提醒类型 1付款提醒
    private String baseTimeType;//基准时间类型 1发票时间
    private String status;//状态(1启用,0禁用)
    
    
    public Long getEmailId() {
		return emailId;
	}
	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Integer getAccountPeriodDay() {
		return accountPeriodDay;
	}
	public void setAccountPeriodDay(Integer accountPeriodDay) {
		this.accountPeriodDay = accountPeriodDay;
	}
	public String getAdvanceReminderDay() {
		return advanceReminderDay;
	}
	public void setAdvanceReminderDay(String advanceReminderDay) {
		this.advanceReminderDay = advanceReminderDay;
	}
	public String getAccountUnitType() {
		return accountUnitType;
	}
	public void setAccountUnitType(String accountUnitType) {
		this.accountUnitType = accountUnitType;
	}
	public String getReminderUnitType() {
		return reminderUnitType;
	}
	public void setReminderUnitType(String reminderUnitType) {
		this.reminderUnitType = reminderUnitType;
	}
	public String getReminderType() {
		return reminderType;
	}
	public void setReminderType(String reminderType) {
		this.reminderType = reminderType;
	}
	public String getBaseTimeType() {
		return baseTimeType;
	}
	public void setBaseTimeType(String baseTimeType) {
		this.baseTimeType = baseTimeType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
    
    
    

}
