package com.topideal.entity.dto.main;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 发送邮件配置表
 * @author 杨创
 *
 */
public class EmailConfigDTO extends PageModel implements Serializable{


	@ApiModelProperty(value = "id")
    private Long id;
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
	@ApiModelProperty(value = "供应商id")
    private Long customerId;
	@ApiModelProperty(value = "供应商名称")
    private String customerName;
	@ApiModelProperty(value = "付款账期 天数")
    private Integer accountPeriodDay;
	@ApiModelProperty(value = "提前提醒天数 多个天数用逗号隔开")
    private String advanceReminderDay;
	@ApiModelProperty(value = "账期单位 1 自然日, 2工作日")
    private String accountUnitType;
	@ApiModelProperty(value = "accountUnitTypeLabel")
    private String accountUnitTypeLabel;
	@ApiModelProperty(value = "提醒单位 1 自然日, 2工作日")
    private String reminderUnitType;
	@ApiModelProperty(value = "reminderUnitTypeLabel")
    private String reminderUnitTypeLabel;
	@ApiModelProperty(value = "reminderType")
    private String reminderType;
	@ApiModelProperty(value = "reminderTypeLabel")
    private String reminderTypeLabel;
	@ApiModelProperty(value = "基准时间类型 1发票时间")
    private String baseTimeType;
	@ApiModelProperty(value = "baseTimeTypeLabel")
    private String baseTimeTypeLabel;
	@ApiModelProperty(value = "状态(1启用,0禁用)")
    private String status;
	@ApiModelProperty(value = "statusLabel")
    private String statusLabel;
	@ApiModelProperty(value = "创建日期")
    private Timestamp createDate;
	@ApiModelProperty(value = "修改日期")
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
    	if(accountUnitType != null) {
    		this.accountUnitTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.emailConfig_unitTypeList, accountUnitType);
    	}
    }
    /*reminderUnitType get 方法 */
    public String getReminderUnitType(){
    return reminderUnitType;
    }
    /*reminderUnitType set 方法 */
    public void setReminderUnitType(String  reminderUnitType){
    	this.reminderUnitType=reminderUnitType;
    	if(reminderUnitType != null) {
    		this.reminderUnitTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.emailConfig_unitTypeList, reminderUnitType) ;
    	}
    }
    /*reminderType get 方法 */
    public String getReminderType(){
    return reminderType;
    }
    /*reminderType set 方法 */
    public void setReminderType(String  reminderType){
    	this.reminderType=reminderType;
    	if(reminderType != null) {
    		this.reminderTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.emailConfig_reminderTypeList, reminderType);
    	}
    }
    /*baseTimeType get 方法 */
    public String getBaseTimeType(){
    return baseTimeType;
    }
    /*baseTimeType set 方法 */
    public void setBaseTimeType(String  baseTimeType){
    	this.baseTimeType=baseTimeType;
    	if(baseTimeType != null) {
    		this.baseTimeTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.emailConfig_baseTimeTypeList, baseTimeType);
    	}
    }
    /*status get 方法 */
    public String getStatus(){
    	return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    	this.status=status;
    	if(status != null) {
    		this.statusLabel = DERP_SYS.getLabelByKey(DERP_SYS.emailConfig_statusList, status);
    	}
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
	public String getAccountUnitTypeLabel() {
		return accountUnitTypeLabel;
	}
	public void setAccountUnitTypeLabel(String accountUnitTypeLabel) {
		this.accountUnitTypeLabel = accountUnitTypeLabel;
	}
	public String getReminderUnitTypeLabel() {
		return reminderUnitTypeLabel;
	}
	public void setReminderUnitTypeLabel(String reminderUnitTypeLabel) {
		this.reminderUnitTypeLabel = reminderUnitTypeLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getReminderTypeLabel() {
		return reminderTypeLabel;
	}
	public void setReminderTypeLabel(String reminderTypeLabel) {
		this.reminderTypeLabel = reminderTypeLabel;
	}
	public String getBaseTimeTypeLabel() {
		return baseTimeTypeLabel;
	}
	public void setBaseTimeTypeLabel(String baseTimeTypeLabel) {
		this.baseTimeTypeLabel = baseTimeTypeLabel;
	}

    
}
