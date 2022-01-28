package com.topideal.entity.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.math.BigDecimal;

@ApiModel
public class PaymentSummaryDTO {

    /**
    * 记录ID
    */
	@ApiModelProperty("记录ID")
    private Long id;
    /**
    * 应付账单
    */
	@ApiModelProperty("应付账单Id")
    private Long paymentId;
    /**
    * 费项id
    */
	@ApiModelProperty("项目id")
    private Long projectId;
    /**
    * 费项名称
    */
	@ApiModelProperty("项目名称")
    private String projectName;
    /**
    * 父级费项id
    */
	@ApiModelProperty("父级费项id")
    private Long parentProjectId;
    /**
    * 父级费项名称
    */
	@ApiModelProperty("父级费项名称")
    private String parentProjectName;
    /**
    * 币种
    */
	@ApiModelProperty("币种")
    private String currency;
    /**
    * 结算金额（不含税）
    */
	@ApiModelProperty("结算金额（不含税）")
    private BigDecimal settlementAmount;
    /**
    * 结算金额（含税）
    */
	@ApiModelProperty("结算金额（含税）")
    private BigDecimal settlementTaxAmount;
    /**
    * 税额
    */
	@ApiModelProperty("税额")
    private BigDecimal tax;
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

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*paymentId get 方法 */
    public Long getPaymentId(){
    return paymentId;
    }
    /*paymentId set 方法 */
    public void setPaymentId(Long  paymentId){
    this.paymentId=paymentId;
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
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*settlementAmount get 方法 */
    public BigDecimal getSettlementAmount(){
    return settlementAmount;
    }
    /*settlementAmount set 方法 */
    public void setSettlementAmount(BigDecimal  settlementAmount){
    this.settlementAmount=settlementAmount;
    }
    /*settlementTaxAmount get 方法 */
    public BigDecimal getSettlementTaxAmount(){
    return settlementTaxAmount;
    }
    /*settlementTaxAmount set 方法 */
    public void setSettlementTaxAmount(BigDecimal  settlementTaxAmount){
    this.settlementTaxAmount=settlementTaxAmount;
    }
    /*tax get 方法 */
    public BigDecimal getTax(){
    return tax;
    }
    /*tax set 方法 */
    public void setTax(BigDecimal  tax){
    this.tax=tax;
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
