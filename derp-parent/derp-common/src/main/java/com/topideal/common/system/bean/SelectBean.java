package com.topideal.common.system.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

// 前端使用 用于展示下拉框
@ApiModel
public class SelectBean implements Serializable {

	@ApiModelProperty(value = "显示的内容")
	private String label;

	@ApiModelProperty(value = "值")
	private String value;

	public SelectBean() {

	}

	public SelectBean(String label, String value) {
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
