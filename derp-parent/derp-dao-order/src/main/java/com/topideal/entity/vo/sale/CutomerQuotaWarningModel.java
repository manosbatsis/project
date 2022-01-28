package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class CutomerQuotaWarningModel extends PageModel implements Serializable{

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
    * 客户ID
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 客户额度
    */
    private BigDecimal customerQuota;
    /**
    * 额度币种
    */
    private String currency;
    /**
    * 额度类型 1-品牌额度
    */
    private String quotaType;
    /**
    * 销售在途金额
    */
    private BigDecimal saleNoshelfAmount;
    /**
    * 待出账单金额
    */
    private BigDecimal nobillAmount;
    /**
    * 待确认账单金额
    */
    private BigDecimal noconfirmAmount;
    /**
    * 待开票金额
    */
    private BigDecimal noinvoiceAmount;
    /**
    * 待回款金额
    */
    private BigDecimal noreturnAmount;
    /**
    * 可用额度金额
    */
    private BigDecimal availableAmount;
    /**
    * 期初已使用额度
    */
    private BigDecimal periodQuota;
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
    /*customerQuota get 方法 */
    public BigDecimal getCustomerQuota(){
    return customerQuota;
    }
    /*customerQuota set 方法 */
    public void setCustomerQuota(BigDecimal  customerQuota){
    this.customerQuota=customerQuota;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*quotaType get 方法 */
    public String getQuotaType(){
    return quotaType;
    }
    /*quotaType set 方法 */
    public void setQuotaType(String  quotaType){
    this.quotaType=quotaType;
    }
    /*saleNoshelfAmount get 方法 */
    public BigDecimal getSaleNoshelfAmount(){
    return saleNoshelfAmount;
    }
    /*saleNoshelfAmount set 方法 */
    public void setSaleNoshelfAmount(BigDecimal  saleNoshelfAmount){
    this.saleNoshelfAmount=saleNoshelfAmount;
    }
    /*nobillAmount get 方法 */
    public BigDecimal getNobillAmount(){
    return nobillAmount;
    }
    /*nobillAmount set 方法 */
    public void setNobillAmount(BigDecimal  nobillAmount){
    this.nobillAmount=nobillAmount;
    }
    /*noconfirmAmount get 方法 */
    public BigDecimal getNoconfirmAmount(){
    return noconfirmAmount;
    }
    /*noconfirmAmount set 方法 */
    public void setNoconfirmAmount(BigDecimal  noconfirmAmount){
    this.noconfirmAmount=noconfirmAmount;
    }
    /*noinvoiceAmount get 方法 */
    public BigDecimal getNoinvoiceAmount(){
    return noinvoiceAmount;
    }
    /*noinvoiceAmount set 方法 */
    public void setNoinvoiceAmount(BigDecimal  noinvoiceAmount){
    this.noinvoiceAmount=noinvoiceAmount;
    }
    /*noreturnAmount get 方法 */
    public BigDecimal getNoreturnAmount(){
    return noreturnAmount;
    }
    /*noreturnAmount set 方法 */
    public void setNoreturnAmount(BigDecimal  noreturnAmount){
    this.noreturnAmount=noreturnAmount;
    }
    /*availableAmount get 方法 */
    public BigDecimal getAvailableAmount(){
    return availableAmount;
    }
    /*availableAmount set 方法 */
    public void setAvailableAmount(BigDecimal  availableAmount){
    this.availableAmount=availableAmount;
    }
    /*periodQuota get 方法 */
    public BigDecimal getPeriodQuota(){
    return periodQuota;
    }
    /*periodQuota set 方法 */
    public void setPeriodQuota(BigDecimal  periodQuota){
    this.periodQuota=periodQuota;
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
