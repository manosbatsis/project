package com.topideal.entity.dto.common;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class SdSaleVerifyConfigDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "公司ID")
    private Long merchantId;

	@ApiModelProperty(value = "公司名称")
    private String merchantName;

	@ApiModelProperty(value = "事业部ID")
    private Long buId;

	@ApiModelProperty(value = "事业部名称")
    private String buName;

	@ApiModelProperty(value = "客户ID")
    private Long customerId;
 
	@ApiModelProperty(value = "客户名称")
    private String customerName;

	@ApiModelProperty(value = "核销类型 0-按PO核销 1-按上架核销")
    private String verifyType;
	@ApiModelProperty(value = "核销类型(中文)")
    private String verifyTypeLabel;

	@ApiModelProperty(value = "是否按商品核销 0-否 1-是")
    private String isMerchandiseVerify;

	@ApiModelProperty(value = "备注")
    private String remark;

	@ApiModelProperty(value = "状态 0-未审核 1-已启用 2-已停用")
    private String status;
	@ApiModelProperty(value = "状态(中文)")
    private String statusLabel;

	@ApiModelProperty(value = "创建人id")
    private Long creater;

	@ApiModelProperty(value = "创建人名称")
    private String createName;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改人id")
    private Long modifier;

	@ApiModelProperty(value = "修改时间")
    private String modifiyName;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "审核人")
    private Long auditer;

	@ApiModelProperty(value = " 审核人ID")
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

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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

	public String getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(String verifyType) {
		this.verifyType = verifyType;
		if(StringUtils.isNotEmpty(verifyType)) {
			this.verifyTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.sdSaleVerifyConfig_verifyTypeList, verifyType);
		}
	}

	public String getVerifyTypeLabel() {
		return verifyTypeLabel;
	}

	public String getIsMerchandiseVerify() {
		return isMerchandiseVerify;
	}

	public void setIsMerchandiseVerify(String isMerchandiseVerify) {
		this.isMerchandiseVerify = isMerchandiseVerify;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		if(StringUtils.isNotEmpty(status)) {
			this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.sdSaleVerifyConfig_statusList, status);
		}
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

	public String getModifiyName() {
		return modifiyName;
	}

	public void setModifiyName(String modifiyName) {
		this.modifiyName = modifiyName;
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
