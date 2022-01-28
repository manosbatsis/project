package com.topideal.order.webapi.common.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class SdSaleTypeForm extends PageForm{
	
	@ApiModelProperty(value = "令牌", required = true)
    private String token;
	
	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "销售SD类型")
    private String sdType;

	@ApiModelProperty(value = "销售SD简称")
    private String sdTypeName;

	@ApiModelProperty(value = "映射费项id")
    private Long projectId;
  
	@ApiModelProperty(value = "映射费项名称")
    private String projectName;

	@ApiModelProperty(value = "NC收支费项id")
    private Long paymentSubjectId;

	@ApiModelProperty(value = "NC收支费项名称")
    private String paymentSubjectName;

	@ApiModelProperty(value = "状态 1- 启用 0-禁用")
    private String status;

	@ApiModelProperty(value = "类型 1-单比例 2-多比例")
	private String type;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSdType() {
		return sdType;
	}

	public void setSdType(String sdType) {
		this.sdType = sdType;
	}

	public String getSdTypeName() {
		return sdTypeName;
	}

	public void setSdTypeName(String sdTypeName) {
		this.sdTypeName = sdTypeName;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Long getPaymentSubjectId() {
		return paymentSubjectId;
	}

	public void setPaymentSubjectId(Long paymentSubjectId) {
		this.paymentSubjectId = paymentSubjectId;
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
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
