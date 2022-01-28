package com.topideal.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 *  单据
 * @author baols
 */
public enum SourceStatusEnum {


	CGRK("001","采购入库"),
	XSDD("002","销售订单"),
	XSCK("003","销售出库"),
	XSTHDD("004","销售退货订单"),
	XSTHRK("005","销售退货入库"),
	XSTHCK("006","销售退货出库"),
	DSDD("007","电商订单"),
	DSDDCK("008","电商订单出库"),
	DBDD("009","调拨订单"),	
	DBRK("0010","调拨入库"),
	DBCK("0011","调拨出库"),
	KCTZD("0012","库存调整单"),
	PDJGD("0013","盘点结果单"),
	LETZD("0014","类型调整单"),
	DSTH("0015","电商订单退货"),
	YSZD("0016","应收账单"),
	YSKD("0017","预收款单"),
	PTJS("0018","平台结算单"),
	XSJGGL("0019","销售价格管理"),
	CGJGGL("0020","采购价格管理"),
	TOCJSD("0021","TOC结算单");
	
	
	//key
	private String key;
	//value
	private String value;
	
	SourceStatusEnum(String  key, String value) {
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
	
	
	public static String getSourceStatusEnumValue(String key){
	    for(SourceStatusEnum sourceStatusEnum : SourceStatusEnum.values()){
	      if(StringUtils.equals(key, sourceStatusEnum.getKey())){
	        return sourceStatusEnum.getValue();
	      }
	    }
	    return "";
	  }
}
