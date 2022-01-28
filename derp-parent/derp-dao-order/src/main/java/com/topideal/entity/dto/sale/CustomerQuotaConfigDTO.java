package com.topideal.entity.dto.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class CustomerQuotaConfigDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "主键ID")
    private Long id;
	
	@ApiModelProperty(value = "事业部ID")
    private Long buId;
	
	@ApiModelProperty(value = "事业部名称")
    private String buName;
	
	@ApiModelProperty(value = "客户ID")
    private Long customerId;
	
	@ApiModelProperty(value = "客户名称")
    private String customerName;
	
	@ApiModelProperty(value = "客户额度")
    private BigDecimal customerQuota;
	
	@ApiModelProperty(value = "币种")
    private String currency;
	
	@ApiModelProperty(value = "生效日期")
    private Timestamp effectiveDate;
	
	@ApiModelProperty(value = "失效日期")
    private Timestamp expirationDate;
	
	@ApiModelProperty(value = "状态 0-待审核 1-已审核")
    private String status;
	@ApiModelProperty(value = "状态（中文）")
    private String statusLabel;
	
	@ApiModelProperty(value = "创建人")
    private Long creater;
	
	@ApiModelProperty(value = "创建人用户名")
    private String createName;
	
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
	
	@ApiModelProperty(value = "修改人")
    private Long modifier;
	
	@ApiModelProperty(value = "修改人用户名")
    private String modifyName;
	
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
	
	@ApiModelProperty(value = "审核人id")
    private Long auditer;
	
	@ApiModelProperty(value = "审核人用户名")
    private String auditName;
	
	@ApiModelProperty(value = "审核时间")
    private Timestamp auditDate;
	
	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getCustomerQuota() {
		return customerQuota;
	}

	public void setCustomerQuota(BigDecimal customerQuota) {
		this.customerQuota = customerQuota;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Timestamp getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Timestamp effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.customerQuotaConfig_statusList, status);
	}

	public String getStatusLabel() {
		return statusLabel;
	}
	
	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	public String getModifyName() {
		return modifyName;
	}

	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Long getAuditer() {
		return auditer;
	}

	public void setAuditer(Long auditer) {
		this.auditer = auditer;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public Timestamp getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	public List<Long> getBuList() {
		return buList;
	}

	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}
	
}
