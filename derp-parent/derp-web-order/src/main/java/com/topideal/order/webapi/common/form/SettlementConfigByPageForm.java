package com.topideal.order.webapi.common.form;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 费用配置分页和导出
 * @author 杨创
 *
 */
public class SettlementConfigByPageForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = false)
    private String token;

    @ApiModelProperty(value = "所属层级", required = false)
    private String level;
    @ApiModelProperty(value = "上级费项名称", required = false)
    private String parentProjectName;
    @ApiModelProperty(value = "本级费项名称", required = false)
    private String projectName;
    @ApiModelProperty(value = "主数据编码", required = false)
    private String inExpCode;
    @ApiModelProperty(value = "NC收支费项", required = false)
    private Long paymentSubjectId;
    @ApiModelProperty(value = "状态", required = false)
    private String status;
    @ApiModelProperty(value = "适用类型 ", required = false)
    private String type;
    @ApiModelProperty(value = "适用模块", required = false)
    private String moduleType;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getParentProjectName() {
		return parentProjectName;
	}
	public void setParentProjectName(String parentProjectName) {
		this.parentProjectName = parentProjectName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getInExpCode() {
		return inExpCode;
	}
	public void setInExpCode(String inExpCode) {
		this.inExpCode = inExpCode;
	}
	public Long getPaymentSubjectId() {
		return paymentSubjectId;
	}
	public void setPaymentSubjectId(Long paymentSubjectId) {
		this.paymentSubjectId = paymentSubjectId;
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
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

}
