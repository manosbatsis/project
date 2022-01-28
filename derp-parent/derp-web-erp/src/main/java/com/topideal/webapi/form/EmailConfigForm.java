package com.topideal.webapi.form;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 发送邮件配置表
 * @author 杨创
 *
 */
public class EmailConfigForm  implements Serializable{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
    @ApiModelProperty(value = "id(编辑必填/新增非必填)")
    private Long id;
    @ApiModelProperty(value = "商家ID",required = true)
    private Long merchantId;
    @ApiModelProperty(value = "商家名称",required = true)
    private String merchantName;
    @ApiModelProperty(value = "供应商id",required = true)
    private Long customerId;
    @ApiModelProperty(value = "供应商名称",required = true)
    private String customerName;
    @ApiModelProperty(value = "付款账期 天数",required = true)
    private Integer accountPeriodDay;
    @ApiModelProperty(value = "提前提醒天数 多个天数用逗号隔开",required = true)
    private String advanceReminderDay;
    @ApiModelProperty(value = "账期单位 1 自然日, 2工作日",required = true)
    private String accountUnitType;
    @ApiModelProperty(value = "提醒单位 1 自然日, 2工作日",required = true)
    private String reminderUnitType;
    @ApiModelProperty(value = "提醒类型 1付款提醒",required = true)
    private String reminderType;
    @ApiModelProperty(value = "基准时间类型 1发票时间",required = true)
    private String baseTimeType;
    
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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




}
