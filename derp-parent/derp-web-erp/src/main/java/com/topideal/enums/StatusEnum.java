package com.topideal.enums;

/**
 * 订单状态
 * @author zhanghx
 */
public enum StatusEnum {

	DSH("001","待审核"),
	SHZ("002","审核中"),
	YSH("003","已审核"),
	YBH("004","已驳回"),
	YGB("005","已关闭"),
	YSC("006","已删除"),
	YWJ("007","已完结"),
	YQX("008","已取消"),
	
	DQR("009","待确认"),
	YQR("010","已确认"),
	/*采购入库单*/
	DRC("011","待入仓"),
	YRC("012","已入仓"),
	/*调拨订单*/
	DTJ("013","待提交"),
	YTJ("014","已提交"),
	
	DCC("015","待出仓"),
	YCC("016","已出仓"),
	
	DCK("017","待出库"),
	YCK("018","已出库");
	
	
	
	//key
	private String key;
	//value
	private String value;
	
	StatusEnum(String  key, String value) {
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
