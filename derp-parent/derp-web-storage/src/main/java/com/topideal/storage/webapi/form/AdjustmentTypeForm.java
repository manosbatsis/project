package com.topideal.storage.webapi.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 类型调整查询条件form
 * @date 2021-02-04
 */
@ApiModel
public class AdjustmentTypeForm extends PageForm {

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "类型调整单号")
	private String code;

	@ApiModelProperty(value = "仓库Id")
	private String depotId;

	@ApiModelProperty(value = "状态 （常量集合adjustmentType_statusList）")
	private String status;

	@ApiModelProperty(value = "业务类别 （常量集合adjustmentType_modelList）")
	private String model;

	@ApiModelProperty(value = "单据来源 （常量集合adjustmentType_sourceList）")
	private String source;

	@ApiModelProperty(value = "调整开始时间 yyyy-MM-dd HH:mm:ss")
	private String adjustmentStartTime;

	@ApiModelProperty(value = "调整结束时间 yyyy-MM-dd HH:mm:ss")
	private String adjustmentEndTime;

	@ApiModelProperty(value = "单据id集合，多个用逗号隔开")
	private String ids;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDepotId() {
		return depotId;
	}

	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAdjustmentStartTime() {
		return adjustmentStartTime;
	}

	public void setAdjustmentStartTime(String adjustmentStartTime) {
		this.adjustmentStartTime = adjustmentStartTime;
	}

	public String getAdjustmentEndTime() {
		return adjustmentEndTime;
	}

	public void setAdjustmentEndTime(String adjustmentEndTime) {
		this.adjustmentEndTime = adjustmentEndTime;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
}
