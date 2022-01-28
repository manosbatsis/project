package com.topideal.common.enums;

/**
 * 店铺类型枚举
 * @author wy
 *
 */
public enum ActionTypeEnum {

	ADD("ADD", "新增"),
	UPDATE("UPDATE", "编辑");

	private String key;
	private String value ;

	ActionTypeEnum(String key, String value) {
		this.key = key;
        this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
