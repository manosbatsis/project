package com.topideal.entity.vo.common;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class QuotaPeriodConfigModel extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    private Long id;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 对象ID
    */
    private Long configObjectId;
    /**
    * 对象名
    */
    private String configObjectName;
    /**
    * 额度类型 1-品牌额度 2-客户额度
    */
    private String quotaType;
    /**
    * 额度币种
    */
    private String currency;
    /**
    * 期初已使用额度
    */
    private BigDecimal periodQuota;
    /**
    * 期初开始日期
    */
    private Timestamp periodDate;
    /**
    * 状态 0-待审核 1-已审核
    */
    private String status;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建人用户名
    */
    private String createName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 审核人id
    */
    private Long auditer;
    /**
    * 审核人用户名
    */
    private String auditName;
    /**
    * 审核时间
    */
    private Timestamp auditDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    /*configObjectId get 方法 */
    public Long getConfigObjectId(){
    return configObjectId;
    }
    /*configObjectId set 方法 */
    public void setConfigObjectId(Long  configObjectId){
    this.configObjectId=configObjectId;
    }
    /*configObjectName get 方法 */
    public String getConfigObjectName(){
    return configObjectName;
    }
    /*configObjectName set 方法 */
    public void setConfigObjectName(String  configObjectName){
    this.configObjectName=configObjectName;
    }
    /*quotaType get 方法 */
    public String getQuotaType(){
    return quotaType;
    }
    /*quotaType set 方法 */
    public void setQuotaType(String  quotaType){
    this.quotaType=quotaType;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*periodQuota get 方法 */
    public BigDecimal getPeriodQuota(){
    return periodQuota;
    }
    /*periodQuota set 方法 */
    public void setPeriodQuota(BigDecimal  periodQuota){
    this.periodQuota=periodQuota;
    }
    /*periodDate get 方法 */
    public Timestamp getPeriodDate(){
    return periodDate;
    }
    /*periodDate set 方法 */
    public void setPeriodDate(Timestamp  periodDate){
    this.periodDate=periodDate;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
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






}
