package com.topideal.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 单据类型
 * @author baols
 */
public enum InventoryStatusEnum {

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
	YJSY("0012","月结损益"),
	XH("0013","销毁"),
	QTCR("0014","其他出入"),
	PDJGD("0015","盘点结果单"),
	HHHZ("0016","好坏互转"),
	HHBG("0017","货号变更"),
	XQTZ("0018","效期调整"),
	DHLH("0019","大货理货"),
	WPHC("0020","唯品红冲"),
	CGZX("0021","采购执行"),
	GJCY("0022","国检抽样"),
	ZRGQ("0023","自然过期"),
	WPPD("0024","唯品盘点"),
	WPBF("0025","唯品报废"),
	DSTH("0026","电商订单退"),
	KTRK("0027","客退入库"),
	ZDXSCK("0028","账单销售出库"),
	SJRK("0029","上架入库");


	
	//key
	private String key;
	//value
	private String value;
	
	InventoryStatusEnum(String  key, String value) {
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
	
	
	public static String getInventoryStatusEnumValue(String key){
	    for(InventoryStatusEnum sourceStatusEnum : InventoryStatusEnum.values()){
	      if(StringUtils.equals(key, sourceStatusEnum.getKey())){
	        return sourceStatusEnum.getValue();
	      }
	    }
	    return "";
	  }
	
}
