package com.topideal.common.enums;

/**
 * 推送众邦云仓状态枚举
 * @author Guobs
 *
 */
public enum PushYwmsTypeEnum {

	CGRK("CGRK", "entryorder.create", "采购订单指令下推") ,
	JYCK("JYCK", "deliveryorder.create", "发货单创建接口") ,
	SSKC("SSKC", "inventory.query", "库存查询接口") ;
	
	//type
	private String type;
	//method
	private String method ;
	//value
	private String value;
	
	PushYwmsTypeEnum(String type, String method, String value){
		this.type = type ;
		this.value = value ;
		this.method = method ;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	
}
