package com.topideal.entity.dto.purchase;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class ProjectQuotaWarningDTO extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    @ApiModelProperty("记录ID")
    private Long id;
    @ApiModelProperty("记录ID,多个以‘,’隔开")
    private String ids;
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
    * 母品牌ID
    */
    @ApiModelProperty("母品牌ID")
    private Long superiorParentBrandId;
    /**
    * 母品牌名
    */
    @ApiModelProperty("母品牌名")
    private String superiorParentBrand;
    /**
    * 项目额度
    */
    @ApiModelProperty("项目额度")
    private BigDecimal projectQuota;
    @ApiModelProperty("项目额度千位符字符串")
    private String projectQuotaStr;
    /**
    * 额度币种
    */
    @ApiModelProperty("额度币种")
    private String currency;
    /**
    * 额度类型 1-品牌额度
    */
    @ApiModelProperty("额度类型 1-品牌额度")
    private String quotaType;
    private String quotaTypeLabel;

    @ApiModelProperty("累计采购冻结金额")
    private BigDecimal purchaseAmount;
    @ApiModelProperty("采购金额千位符字符串")
    private String purchaseAmountStr;
    /**
    * 销售已回款金额
    */
    @ApiModelProperty("销售已回款金额")
    private BigDecimal salesCollectedAmount;
    @ApiModelProperty("销售已回款金额字符串")
    private String salesCollectedAmountStr;
    /**
    * 可用额度
    */
    @ApiModelProperty("可用额度")
    private BigDecimal availableAmount;
    @ApiModelProperty("可用额度字符串")
    private String availableAmountStr;
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

    private List<Long> buIds ;

    /**
     * 期初已使用额度
     */
    @ApiModelProperty("期初已使用额度")
    private BigDecimal periodQuota;

    @ApiModelProperty("累计付款金额")
    private BigDecimal addPaymentAmount;

    @ApiModelProperty("累计付款金额字符串")
    private String addPaymentAmountStr;

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
    this.quotaTypeLabel= DERP_ORDER.getLabelByKey(DERP_ORDER.projectquotaconfig_quotaTypeList, quotaType) ;
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

    public String getQuotaTypeLabel() {
        return quotaTypeLabel;
    }

    public void setQuotaTypeLabel(String quotaTypeLabel) {
        this.quotaTypeLabel = quotaTypeLabel;
    }

    public List<Long> getBuIds() {
        return buIds;
    }

    public void setBuIds(List<Long> buIds) {
        this.buIds = buIds;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getProjectQuotaStr() {
        return projectQuotaStr;
    }

    public void setProjectQuotaStr(String projectQuotaStr) {
        this.projectQuotaStr = projectQuotaStr;
    }

    public String getPurchaseAmountStr() {
        return purchaseAmountStr;
    }

    public void setPurchaseAmountStr(String purchaseAmountStr) {
        this.purchaseAmountStr = purchaseAmountStr;
    }

    public String getSalesCollectedAmountStr() {
        return salesCollectedAmountStr;
    }

    public void setSalesCollectedAmountStr(String salesCollectedAmountStr) {
        this.salesCollectedAmountStr = salesCollectedAmountStr;
    }

    public String getAvailableAmountStr() {
        return availableAmountStr;
    }

    public void setAvailableAmountStr(String availableAmountStr) {
        this.availableAmountStr = availableAmountStr;
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

    public String getAddPaymentAmountStr() {
        return addPaymentAmountStr;
    }

    public void setAddPaymentAmountStr(String addPaymentAmountStr) {
        this.addPaymentAmountStr = addPaymentAmountStr;
    }
}
