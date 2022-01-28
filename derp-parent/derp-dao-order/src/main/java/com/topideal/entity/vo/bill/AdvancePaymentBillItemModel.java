package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class AdvancePaymentBillItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 预付款单Id
    */
    private Long advancePaymentId;
    /**
    * 采购单号ID
    */
    private Long purchaseId;
    /**
    * 采购单号
    */
    private String purchaseCode;
    /**
    * 费项id
    */
    private Long projectId;
    /**
    * 费项名称
    */
    private String projectName;
    /**
    * 父级费项id
    */
    private Long parentProjectId;
    /**
    * 父级费项名称
    */
    private String parentProjectName;
    /**
    * 申请付款金额
    */
    private BigDecimal prepaymentAmount;
    /**
    * 待核销金额
    */
    private BigDecimal purchaseAmount;
    /**
     * 采购金额
     */
    private BigDecimal verificateAmount;
    /**
    * 币种
    */
    private String currency;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
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
    /*advancePaymentId get 方法 */
    public Long getAdvancePaymentId(){
    return advancePaymentId;
    }
    /*advancePaymentId set 方法 */
    public void setAdvancePaymentId(Long  advancePaymentId){
    this.advancePaymentId=advancePaymentId;
    }
    /*purchaseId get 方法 */
    public Long getPurchaseId(){
    return purchaseId;
    }
    /*purchaseId set 方法 */
    public void setPurchaseId(Long  purchaseId){
    this.purchaseId=purchaseId;
    }
    /*purchaseCode get 方法 */
    public String getPurchaseCode(){
    return purchaseCode;
    }
    /*purchaseCode set 方法 */
    public void setPurchaseCode(String  purchaseCode){
    this.purchaseCode=purchaseCode;
    }
    /*projectId get 方法 */
    public Long getProjectId(){
    return projectId;
    }
    /*projectId set 方法 */
    public void setProjectId(Long  projectId){
    this.projectId=projectId;
    }
    /*projectName get 方法 */
    public String getProjectName(){
    return projectName;
    }
    /*projectName set 方法 */
    public void setProjectName(String  projectName){
    this.projectName=projectName;
    }
    /*parentProjectId get 方法 */
    public Long getParentProjectId(){
    return parentProjectId;
    }
    /*parentProjectId set 方法 */
    public void setParentProjectId(Long  parentProjectId){
    this.parentProjectId=parentProjectId;
    }
    /*parentProjectName get 方法 */
    public String getParentProjectName(){
    return parentProjectName;
    }
    /*parentProjectName set 方法 */
    public void setParentProjectName(String  parentProjectName){
    this.parentProjectName=parentProjectName;
    }
    /*prepaymentAmount get 方法 */
    public BigDecimal getPrepaymentAmount(){
    return prepaymentAmount;
    }
    /*prepaymentAmount set 方法 */
    public void setPrepaymentAmount(BigDecimal  prepaymentAmount){
    this.prepaymentAmount=prepaymentAmount;
    }
    /*purchaseAmount get 方法 */
    public BigDecimal getPurchaseAmount(){
    return purchaseAmount;
    }
    /*purchaseAmount set 方法 */
    public void setPurchaseAmount(BigDecimal  purchaseAmount){
    this.purchaseAmount=purchaseAmount;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
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

    public BigDecimal getVerificateAmount() {
        return verificateAmount;
    }

    public void setVerificateAmount(BigDecimal verificateAmount) {
        this.verificateAmount = verificateAmount;
    }
}
