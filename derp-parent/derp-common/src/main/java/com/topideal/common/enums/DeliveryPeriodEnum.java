package com.topideal.common.enums;
/**
 * 采购执行模型——货期（月）
 * @author wy
 */
public enum DeliveryPeriodEnum {
		 ZERO_MONTH("0","未维护"),
		TWO_MONTHS("2","2个月"),
		FOUR_MONTHS("4", "4个月");
	
	// key
    private String key;
    // value
    private String value;

    DeliveryPeriodEnum(String  key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
	
	
}
