package com.topideal.common.constant;

import io.swagger.annotations.ApiModelProperty;

/** 
 * 常量模型
 * 
 */
public class LogPointBasic {

	// 日志编码
	private String point;
	//接口名称
	private String label;
	//模块
	private String modelCode;
	//是否巡检 0 -否， 1-是
	private String isInspected;

	public LogPointBasic(String point, String label, String modelCode, String isInspected) {
		this.point = point;
		this.label = label;
		this.modelCode = modelCode;
		this.isInspected = isInspected;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getIsInspected() {
		return isInspected;
	}

	public void setIsInspected(String isInspected) {
		this.isInspected = isInspected;
	}
}
