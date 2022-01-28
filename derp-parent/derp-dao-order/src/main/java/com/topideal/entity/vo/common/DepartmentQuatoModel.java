package com.topideal.entity.vo.common;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class DepartmentQuatoModel extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    private Long id;
    /**
    * 部门ID
    */
    private Long departmentId;
    /**
    * 部门名称
    */
    private String departmentName;
    /**
    * 部门总额度
    */
    private BigDecimal departmentQuota;
    /**
    * 额度币种
    */
    private String currency;
    /**
    * 生效日期
    */
    private Timestamp effectiveDate;
    /**
    * 失效日期
    */
    private Timestamp expirationDate;
    /**
    * 已使用额度
    */
    private BigDecimal usedQuota;
    /**
    * 剩余额度
    */
    private BigDecimal surplusQuota;
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
    /*departmentId get 方法 */
    public Long getDepartmentId(){
    return departmentId;
    }
    /*departmentId set 方法 */
    public void setDepartmentId(Long  departmentId){
    this.departmentId=departmentId;
    }
    /*departmentName get 方法 */
    public String getDepartmentName(){
    return departmentName;
    }
    /*departmentName set 方法 */
    public void setDepartmentName(String  departmentName){
    this.departmentName=departmentName;
    }
    /*departmentQuota get 方法 */
    public BigDecimal getDepartmentQuota(){
    return departmentQuota;
    }
    /*departmentQuota set 方法 */
    public void setDepartmentQuota(BigDecimal  departmentQuota){
    this.departmentQuota=departmentQuota;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*effectiveDate get 方法 */
    public Timestamp getEffectiveDate(){
    return effectiveDate;
    }
    /*effectiveDate set 方法 */
    public void setEffectiveDate(Timestamp  effectiveDate){
    this.effectiveDate=effectiveDate;
    }
    /*expirationDate get 方法 */
    public Timestamp getExpirationDate(){
    return expirationDate;
    }
    /*expirationDate set 方法 */
    public void setExpirationDate(Timestamp  expirationDate){
    this.expirationDate=expirationDate;
    }
    /*usedQuota get 方法 */
    public BigDecimal getUsedQuota(){
    return usedQuota;
    }
    /*usedQuota set 方法 */
    public void setUsedQuota(BigDecimal  usedQuota){
    this.usedQuota=usedQuota;
    }
    /*surplusQuota get 方法 */
    public BigDecimal getSurplusQuota(){
    return surplusQuota;
    }
    /*surplusQuota set 方法 */
    public void setSurplusQuota(BigDecimal  surplusQuota){
    this.surplusQuota=surplusQuota;
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