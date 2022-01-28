package com.topideal.order.webapi.bill.form;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.dto.bill.AccountingReminderItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class AccountingReminderConfigAddForm implements Serializable{

    @ApiModelProperty(value = "票据", required = true)
    private String token ;
    /**
    * 记录ID
    */
    @ApiModelProperty("记录ID")
    private Long id;
    /**
    * 客户ID
    */
    @ApiModelProperty("客户ID, 当查询条件同选供应商和客户时，id以‘,’隔开")
    private Long customerId;
    /**
    * 客户名称
    */
    @ApiModelProperty("客户名称")
    private String customerName;
    /**
    * 事业部ID
    */
    @ApiModelProperty("事业部ID")
    private Long buId;
    /**
    * 事业部名称
    */
    @ApiModelProperty("事业部名称")
    private String buName;
    /**
    * 商家ID
    */
    @ApiModelProperty("商家ID")
    private Long merchantId;
    /**
    * 商家名称
    */
    @ApiModelProperty("商家名称")
    private String merchantName;
    /**
    * 提醒类型 1-收款 2-付款
    */
    @ApiModelProperty("提醒类型 1-收款 2-付款")
    private String reminderType;
    @ApiModelProperty("提醒类型中文 1-收款 2-付款")
    private String reminderTypeLabel;
    /**
    * 基准日期 1-发票日期 2-上架日期
    */
    @ApiModelProperty("基准日期 1-发票日期 2-上架日期")
    private String baseDate;
    @ApiModelProperty("基准日期中文 1-发票日期 2-上架日期")
    private String baseDateLabel;
    /**
    * 创建人
    */
    @ApiModelProperty("创建人")
    private Long creater;
    /**
    * 创建人用户名
    */
    @ApiModelProperty("创建人用户名")
    private String createName;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty("修改时间")
    private Timestamp modifyDate;
    /**
    * 审核人id
    */
    @ApiModelProperty("审核人id")
    private Long auditer;
    /**
    * 审核人用户名
    */
    @ApiModelProperty("审核人用户名")
    private String auditName;
    /**
    * 审核时间
    */
    @ApiModelProperty("审核时间")
    private Timestamp auditDate;

    @ApiModelProperty("明细列表")
    private List<AccountingReminderItemDTO> itemList ;

    @ApiModelProperty("状态 0-待审核 1-已审核")
    private String status ;
    @ApiModelProperty("状态中文 0-待审核 1-已审核")
    private String statusLabel ;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
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
    /*reminderType get 方法 */
    public String getReminderType(){
    return reminderType;
    }
    /*reminderType set 方法 */
    public void setReminderType(String  reminderType){
    this.reminderType=reminderType;
    this.reminderTypeLabel=DERP_ORDER.getLabelByKey(DERP_ORDER.accountingReminderConfig_reminderTypeList, reminderType);
    }
    /*baseDate get 方法 */
    public String getBaseDate(){
    return baseDate;
    }
    /*baseDate set 方法 */
    public void setBaseDate(String  baseDate){
    this.baseDate=baseDate;
    this.baseDateLabel= DERP_ORDER.getLabelByKey(DERP_ORDER.accountingReminderConfig_baseDateList, baseDate) ;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
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
    /*auditer get 方法 */
    public Long getAuditer(){
    return auditer;
    }
    /*auditer set 方法 */
    public void setAuditer(Long  auditer){
    this.auditer=auditer;
    }
    /*auditName get 方法 */
    public String getAuditName(){
    return auditName;
    }
    /*auditName set 方法 */
    public void setAuditName(String  auditName){
    this.auditName=auditName;
    }
    /*auditDate get 方法 */
    public Timestamp getAuditDate(){
    return auditDate;
    }
    /*auditDate set 方法 */
    public void setAuditDate(Timestamp  auditDate){
    this.auditDate=auditDate;
    }


    public String getReminderTypeLabel() {
        return reminderTypeLabel;
    }

    public void setReminderTypeLabel(String reminderTypeLabel) {
        this.reminderTypeLabel = reminderTypeLabel;
    }

    public String getBaseDateLabel() {
        return baseDateLabel;
    }

    public void setBaseDateLabel(String baseDateLabel) {
        this.baseDateLabel = baseDateLabel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<AccountingReminderItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<AccountingReminderItemDTO> itemList) {
        this.itemList = itemList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.accountingReminderConfig_statusList, status) ;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }
}
