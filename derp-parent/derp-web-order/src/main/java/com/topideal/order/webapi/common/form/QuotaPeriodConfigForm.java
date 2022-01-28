package com.topideal.order.webapi.common.form;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class QuotaPeriodConfigForm extends PageForm {

	@ApiModelProperty(value = "票据",required = false)
    private String token;
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
	@ApiModelProperty(value = "期初开始日期字符串",required = false)
    private String periodDateStr;
    /**
    * 状态 0-待审核 1-已审核
    */
	@ApiModelProperty(value = "状态 0-待审核 1-已审核",required = false)
    private String status;

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
	public String getPeriodDateStr() {
		return periodDateStr;
	}
	public void setPeriodDateStr(String periodDateStr) {
		this.periodDateStr = periodDateStr;
	}



}
