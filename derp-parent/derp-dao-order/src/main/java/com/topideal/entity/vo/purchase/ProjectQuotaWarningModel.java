package com.topideal.entity.vo.purchase;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class ProjectQuotaWarningModel extends PageModel implements Serializable{

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
    * 母品牌ID
    */
    private Long superiorParentBrandId;
    /**
    * 母品牌名
    */
    private String superiorParentBrand;
    /**
    * 项目额度
    */
    private BigDecimal projectQuota;
    /**
    * 额度币种
    */
    private String currency;
    /**
    * 额度类型 1-品牌额度
    */
    private String quotaType;
    /**
    * 累计采购冻结金额
    */
    private BigDecimal purchaseAmount;
    /**
    * 销售已回款金额
    */
    private BigDecimal salesCollectedAmount;
    /**
    * 可用额度
    */
    private BigDecimal availableAmount;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 期初已使用额度
     */
    private BigDecimal periodQuota;
    //累计付款金额
    private BigDecimal addPaymentAmount;

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
    /*superiorParentBrandId get 方法 */
    public Long getSuperiorParentBrandId(){
    return superiorParentBrandId;
    }
    /*superiorParentBrandId set 方法 */
    public void setSuperiorParentBrandId(Long  superiorParentBrandId){
    this.superiorParentBrandId=superiorParentBrandId;
    }
    /*superiorParentBrand get 方法 */
    public String getSuperiorParentBrand(){
    return superiorParentBrand;
    }
    /*superiorParentBrand set 方法 */
    public void setSuperiorParentBrand(String  superiorParentBrand){
    this.superiorParentBrand=superiorParentBrand;
    }
    /*projectQuota get 方法 */
    public BigDecimal getProjectQuota(){
    return projectQuota;
    }
    /*projectQuota set 方法 */
    public void setProjectQuota(BigDecimal  projectQuota){
    this.projectQuota=projectQuota;
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
    /*purchaseAmount get 方法 */
    public BigDecimal getPurchaseAmount(){
    return purchaseAmount;
    }
    /*purchaseAmount set 方法 */
    public void setPurchaseAmount(BigDecimal  purchaseAmount){
    this.purchaseAmount=purchaseAmount;
    }
    /*salesCollectedAmount get 方法 */
    public BigDecimal getSalesCollectedAmount(){
    return salesCollectedAmount;
    }
    /*salesCollectedAmount set 方法 */
    public void setSalesCollectedAmount(BigDecimal  salesCollectedAmount){
    this.salesCollectedAmount=salesCollectedAmount;
    }
    /*availableAmount get 方法 */
    public BigDecimal getAvailableAmount(){
    return availableAmount;
    }
    /*availableAmount set 方法 */
    public void setAvailableAmount(BigDecimal  availableAmount){
    this.availableAmount=availableAmount;
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
	public BigDecimal getPeriodQuota() {
		return periodQuota;
	}
	public void setPeriodQuota(BigDecimal periodQuota) {
		this.periodQuota = periodQuota;
	}

    public BigDecimal getAddPaymentAmount() {
        return addPaymentAmount;
    }

    public void setAddPaymentAmount(BigDecimal addPaymentAmount) {
        this.addPaymentAmount = addPaymentAmount;
    }
}
