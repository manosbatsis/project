package com.topideal.entity.dto.common;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

public class SettlementConfigDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id", required = false)
    private Long id;

	@ApiModelProperty(value = "本级费项编码", required = false)
    private String projectCode;

	@ApiModelProperty(value = "本级费项名称", required = false)
    private String projectName;

	@ApiModelProperty(value = "层级 1:一级,2:二级", required = false)
    private String level;

	@ApiModelProperty(value = "上级费项名称", required = false)
    private String parentProjectName;

	@ApiModelProperty(value = "上级费项id", required = false)
    private Long parentId;

	@ApiModelProperty(value = "NC收支费项名称", required = false)
    private String paymentSubjectName;

	@ApiModelProperty(value = "NC收支费项id", required = false)
    private Long paymentSubjectId;
    

	@ApiModelProperty(value = "状态(1使用中,0已禁用)", required = false)
    private String status;

	@ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;

	@ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;

	@ApiModelProperty(value = "修改人ID", required = false)
    private Long modifier;

	@ApiModelProperty(value = "修改人名", required = false)
    private String modifierName;

	@ApiModelProperty(value = "创建人ID", required = false)
    private Long creater;

	@ApiModelProperty(value = "创建人名称", required = false)
    private String createrName;

	@ApiModelProperty(value = "层级 1:一级,2:二级", required = false)
    private String levelLabel;

	@ApiModelProperty(value = "状态(1使用中,0已禁用)", required = false)
    private String statusLabel;

	@ApiModelProperty(value = "适用客户: 1通用,2指定客户", required = false)
    private String suitableCustomerLabel;

	@ApiModelProperty(value = "适用客户: 1通用,2指定客户", required = false)
     private String suitableCustomer;

	 @ApiModelProperty(value = "逗号隔开的客户名称", required = false)
     private String  customerNames;


	 @ApiModelProperty(value = "逗号隔开的客户id", required = false)
     private String customerIdsStr;

	 @ApiModelProperty(value = "客户id", required = false)
	 private Long customerId;

	@ApiModelProperty(value = "主数据据编码", required = false)
	private String inExpCode;

	@ApiModelProperty(value = "适用类型:1.To B 2.To C", required = false)
	private String type; 

	@ApiModelProperty(value = "适用类型:1.To B 2.To C", required = false)
	private String typeLabel;

	@ApiModelProperty(value = "适用模块:1.应付 2.应收 3.预付 4.预收", required = false)
	private String moduleType;

	@ApiModelProperty(value = "适用类型集合", required = false)
	private List<String> types;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
		this.levelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.settlementConfig_levelList, level) ;
	}
	public String getParentProjectName() {
		return parentProjectName;
	}
	public void setParentProjectName(String parentProjectName) {
		this.parentProjectName = parentProjectName;
	}
	
	public Long getPaymentSubjectId() {
		return paymentSubjectId;
	}
	public void setPaymentSubjectId(Long paymentSubjectId) {
		this.paymentSubjectId = paymentSubjectId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getPaymentSubjectName() {
		return paymentSubjectName;
	}
	public void setPaymentSubjectName(String paymentSubjectName) {
		this.paymentSubjectName = paymentSubjectName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
		this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.settlementConfig_statusList, status) ;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Long getModifier() {
		return modifier;
	}
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	public String getModifierName() {
		return modifierName;
	}
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
	public Long getCreater() {
		return creater;
	}
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public String getLevelLabel() {
		return levelLabel;
	}
	public void setLevelLabel(String levelLabel) {
		this.levelLabel = levelLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getSuitableCustomer() {
		return suitableCustomer;
	}
	public void setSuitableCustomer(String suitableCustomer) {
		this.suitableCustomer = suitableCustomer;
		this.suitableCustomerLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.settlementConfig_suitableCustomerList, suitableCustomer) ;

	}
	public String getCustomerNames() {
		return customerNames;
	}
	public void setCustomerNames(String customerNames) {
		this.customerNames = customerNames;
	}
	public String getSuitableCustomerLabel() {
		return suitableCustomerLabel;
	}
	public void setSuitableCustomerLabel(String suitableCustomerLabel) {
		this.suitableCustomerLabel = suitableCustomerLabel;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getCustomerIdsStr() {
		return customerIdsStr;
	}
	public void setCustomerIdsStr(String customerIdsStr) {
		this.customerIdsStr = customerIdsStr;
	}

	public String getInExpCode() {
		return inExpCode;
	}

	public void setInExpCode(String inExpCode) {
		this.inExpCode = inExpCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
		this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.settlementConfig_typeList, type) ;

	}
	public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}
}
