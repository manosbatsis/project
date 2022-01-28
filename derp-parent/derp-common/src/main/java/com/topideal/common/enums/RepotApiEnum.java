package com.topideal.common.enums;

/**
 * 报表Api枚举
 * @author 杨创
 * 2019.04.01 
 */
public enum RepotApiEnum {

	
	//单据类型编码（单个）002销售出库  004销售退货出库 005电商订单出库  007调拨出库 008月结损益出  
	//010销毁出 011其他出 014盘亏出 015好/坏品出 017货号变更出 019效期调整出
	//单据类型编码 001采购入库 003销售退货入库 006调拨入  009月结损益入 012其他入 013盘盈入 016好/坏品入 018货号变更入 020效期调整入	
	CGRK("001","采购入库"),
	XSCK("002","销售出库"),
	XSTR("003","销售退货入库"),
	XSTC("004","销售退货出库"),
	DSDD("005","电商订单出库"),
	DBRK("006","调拨入库"),
	DBCK("007","调拨出库"),
	YJSYC("008","月结损益出"),
	YJSYR("009","月结损益入"),	
	XHC("010","销毁出"),
	QTC("011","其他出"),
	QTR("012","其他入"),
	PYR("013","盘盈入"),
	PKC("014","盘亏出"),
	HHPC("015","好坏品出"),
	HHPR("016","好坏品入"),
	HHBGC("017","货号变更出"),
	HHBGR("018","货号变更入"),
	XQTZC("019","效期调整出"),
	XQTZR("020","效期调整入"),
	DHLHC("021","大货理货出"),
	DHLHR("022","大货理货入"),
	WPHCR("023","唯品红冲入"),
	CGO("101", "采购订单"),
	XSO("102", "销售订单"),
	DSO("103", "电商订单");
	
	
	
	//key
	private String key;
	//value
	private String value;
	
	RepotApiEnum(String  key, String value) {
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
