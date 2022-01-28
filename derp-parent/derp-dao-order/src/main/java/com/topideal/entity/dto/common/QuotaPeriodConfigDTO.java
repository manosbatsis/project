package com.topideal.entity.dto.common;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class QuotaPeriodConfigDTO extends PageModel implements Serializable{

    /**
    * 记录ID
    */
	@ApiModelProperty(value = "记录ID",required = false)
    private Long id;
    /**
    * 事业部ID
    */
	@ApiModelProperty(value = "事业部ID",required = false)
    private Long buId;
    /**
    * 事业部名称
    */
	@ApiModelProperty(value = "事业部名称",required = false)
    private String buName;
    /**
    * 对象ID
    */
	@ApiModelProperty(value = "对象ID",required = false)
    private Long configObjectId;
    /**
    * 对象名
    */
	@ApiModelProperty(value = "对象名",required = false)
    private String configObjectName;
    /**
    * 额度类型 1-品牌额度 2-客户额度
    */
	@ApiModelProperty(value = "额度类型 1-品牌额度 2-客户额度",required = false)
    private String quotaType;
	private String quotaTypeLabel;
    /**
    * 额度币种
    */
	@ApiModelProperty(value = "额度币种",required = false)
    private String currency;
    /**
    * 期初已使用额度
    */
	@ApiModelProperty(value = "期初已使用额度",required = false)
    private BigDecimal periodQuota;
    /**
    * 期初开始日期
    */
	@ApiModelProperty(value = "期初开始日期",required = false)
    private Timestamp periodDate;
    /**
    * 状态 0-待审核 1-已审核
    */
	@ApiModelProperty(value = "状态 0-待审核 1-已审核",required = false)
	private String status;
	@ApiModelProperty(value = "状态中文 0-待审核 1-已审核",required = false)
    private String statusLabel;
    /**
    * 创建人
    */
	@ApiModelProperty(value = "创建人",required = false)
    private Long creater;
    /**
    * 创建人用户名
    */
	@ApiModelProperty(value = "创建人用户名",required = false)
    private String createName;
    /**
    * 创建时间
    */
	@ApiModelProperty(value = "创建时间",required = false)
    private Timestamp createDate;
    /**
    * 修改时间
    */
	@ApiModelProperty(value = "修改时间",required = false)
    private Timestamp modifyDate;
    /**
    * 审核人id
    */
	@ApiModelProperty(value = "修改时间",required = false)
    private Long auditer;
    /**
    * 审核人用户名
    */
	@ApiModelProperty(value = "审核人用户名",required = false)
    private String auditName;
    /**
    * 审核时间
    */
	@ApiModelProperty(value = "审核时间",required = false)
    private Timestamp auditDate;
	
	private List<Long> buIds;

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
    this.quotaTypeLabel=DERP_ORDER.getLabelByKey(DERP_ORDER.quotaconfig_quotaTypeList, quotaType) ;
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
    this.statusLabel=DERP_ORDER.getLabelByKey(DERP_ORDER.projectquotaconfig_statusList, status) ;
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
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}






}
