package com.topideal.entity.vo.bill;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

public class TocSettlementReceiveBillAuditItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 应收账单Id
    */
    private Long billId;
    /**
    * 提交人ID
    */
    private Long submitId;
    /**
    * 提交人名称
    */
    private String submitter;
    /**
    * 提交时间
    */
    private Timestamp submitDate;
    /**
    * 审批人ID
    */
    private Long auditId;
    /**
    * 审批人名称
    */
    private String auditor;
    /**
    * 审批时间
    */
    private Timestamp auditDate;
    /**
    * 审批结果 00-审批通过 01-驳回
    */
    private String auditResult;
    private String auditResultLabel;
    /**
    * 审批备注
    */
    private String auditRemark;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 提交备注
    */
    private String submitRemark;
    /**
    * 审批类型 0-应收审核 1-作废审核
    */
    private String auditType;
    private String auditTypeLabel;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*billId get 方法 */
    public Long getBillId(){
    return billId;
    }
    /*billId set 方法 */
    public void setBillId(Long  billId){
    this.billId=billId;
    }
    /*submitId get 方法 */
    public Long getSubmitId(){
    return submitId;
    }
    /*submitId set 方法 */
    public void setSubmitId(Long  submitId){
    this.submitId=submitId;
    }
    /*submitter get 方法 */
    public String getSubmitter(){
    return submitter;
    }
    /*submitter set 方法 */
    public void setSubmitter(String  submitter){
    this.submitter=submitter;
    }
    /*submitDate get 方法 */
    public Timestamp getSubmitDate(){
    return submitDate;
    }
    /*submitDate set 方法 */
    public void setSubmitDate(Timestamp  submitDate){
    this.submitDate=submitDate;
    }
    /*auditId get 方法 */
    public Long getAuditId(){
    return auditId;
    }
    /*auditId set 方法 */
    public void setAuditId(Long  auditId){
    this.auditId=auditId;
    }
    /*auditor get 方法 */
    public String getAuditor(){
    return auditor;
    }
    /*auditor set 方法 */
    public void setAuditor(String  auditor){
    this.auditor=auditor;
    }
    /*auditDate get 方法 */
    public Timestamp getAuditDate(){
    return auditDate;
    }
    /*auditDate set 方法 */
    public void setAuditDate(Timestamp  auditDate){
    this.auditDate=auditDate;
    }
    /*auditResult get 方法 */
    public String getAuditResult(){
    return auditResult;
    }
    /*auditResult set 方法 */
    public void setAuditResult(String  auditResult){
    this.auditResult=auditResult;
        this.auditResultLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocReceiveBillAudit_auditResultList, auditResult);
    }
    /*auditRemark get 方法 */
    public String getAuditRemark(){
    return auditRemark;
    }
    /*auditRemark set 方法 */
    public void setAuditRemark(String  auditRemark){
    this.auditRemark=auditRemark;
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
    /*submitRemark get 方法 */
    public String getSubmitRemark(){
    return submitRemark;
    }
    /*submitRemark set 方法 */
    public void setSubmitRemark(String  submitRemark){
    this.submitRemark=submitRemark;
    }
    /*auditType get 方法 */
    public String getAuditType(){
    return auditType;
    }
    /*auditType set 方法 */
    public void setAuditType(String  auditType){
    this.auditType=auditType;
        this.auditTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocReceiveBillAudit_auditTypeList, auditType);
    }

    public String getAuditResultLabel() {
        return auditResultLabel;
    }

    public void setAuditResultLabel(String auditResultLabel) {
        this.auditResultLabel = auditResultLabel;
    }

    public String getAuditTypeLabel() {
        return auditTypeLabel;
    }

    public void setAuditTypeLabel(String auditTypeLabel) {
        this.auditTypeLabel = auditTypeLabel;
    }
}
