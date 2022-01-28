package com.topideal.common.constant;

import io.swagger.annotations.ApiModelProperty;

/** 
 * 常量模型
 * 
 */
public class DerpBasic {
	@ApiModelProperty(value = "key")
	private Object key;
	@ApiModelProperty(value = "value")
	private String value;
	
	//public DerpBasic(){}
	
	public DerpBasic(Object key, String value){
		this.key = key;
		this.value = value;
	}
	public Object getKey() {
		return key;
	}
	public void setKey(Object key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
