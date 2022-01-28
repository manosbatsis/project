package com.topideal.order.webapi.common.form;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class DepartmentQuatoForm extends PageForm implements Serializable{

	@ApiModelProperty(value = "票据",required = false)
    private String token;
    /**
    * 记录ID
    */
	@ApiModelProperty("记录ID")
    private Long id;
    /**
    * 部门ID
    */
	@ApiModelProperty("部门ID")
    private Long departmentId;
    /**
    * 部门名称
    */
	@ApiModelProperty("部门名称")
    private String departmentName;
    /**
    * 部门总额度
    */
	@ApiModelProperty("部门总额度")
    private BigDecimal departmentQuota;
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
    * 已使用额度
    */
	@ApiModelProperty("已使用额度")
    private BigDecimal usedQuota;
    /**
    * 剩余额度
    */
	@ApiModelProperty("剩余额度")
    private BigDecimal surplusQuota;
    /**
    * 状态 0-待审核 1-已审核
    */
	@ApiModelProperty("状态 0-待审核 1-已审核")
    private String status;
	@ApiModelProperty("状态中文 0-待审核 1-已审核")
    private String statusLabel;
	
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
    this.statusLabel=DERP_ORDER.getLabelByKey(DERP_ORDER.projectquotaconfig_statusList, status) ;
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
    






}
