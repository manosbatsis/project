package com.topideal.order.webapi.purchase.form;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel
public class ProjectQuotaConfigForm extends PageForm implements Serializable{

    @ApiModelProperty("票据")
    private String token;
    /**
    * 记录ID
    */
    @ApiModelProperty("记录ID")
    private Long id;
    /**
     * 记录ID
     */
    @ApiModelProperty("导出多选ID，多个以','隔开")
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
    /**
    * 额度币种
    */
    @ApiModelProperty("额度币种")
    private String currency;
    /**
    * 生效日期
    */
    @ApiModelProperty("生效日期")
    private Timestamp effectiveDate;
    @ApiModelProperty("生效日期字符串")
    private String effectiveDateStr;
    /**
    * 失效日期
    */
    @ApiModelProperty("失效日期")
    private Timestamp expirationDate;
    @ApiModelProperty("失效日期字符串")
    private String expirationDateStr;
    /**
    * 额度类型 1-品牌额度
    */
    @ApiModelProperty("额度类型 1-品牌额度 常量list: projectquotaconfig_quotaTypeList")
    private String quotaType;
    @ApiModelProperty("额度类型中文 1-品牌额度")
    private String quotaTypeLabel;
    /**
    * 状态 0-待审核 1-已审核
    */
    @ApiModelProperty("状态 0-待审核 1-已审核 常量list: projectquotaconfig_statusList")
    private String status;
    @ApiModelProperty("状态中文 0-待审核 1-已审核")
    private String statusLabel;
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
    /*quotaType get 方法 */
    public String getQuotaType(){
    return quotaType;
    }
    /*quotaType set 方法 */
    public void setQuotaType(String  quotaType){
    this.quotaType=quotaType;
    this.quotaTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.projectquotaconfig_quotaTypeList, quotaType) ;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.projectquotaconfig_statusList, status) ;
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

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getQuotaTypeLabel() {
        return quotaTypeLabel;
    }

    public void setQuotaTypeLabel(String quotaTypeLabel) {
        this.quotaTypeLabel = quotaTypeLabel;
    }

    public String getEffectiveDateStr() {
        return effectiveDateStr;
    }

    public void setEffectiveDateStr(String effectiveDateStr) {
        this.effectiveDateStr = effectiveDateStr;
    }

    public String getExpirationDateStr() {
        return expirationDateStr;
    }

    public void setExpirationDateStr(String expirationDateStr) {
        this.expirationDateStr = expirationDateStr;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
