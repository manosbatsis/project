package com.topideal.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 *  事物类型
 * @author baols
 */
public enum InventoryThingStatusEnum {

	
    CGRK("001","采购入库"),
    XSCK("002","销售出库"),
    XSTHRK("003","销售退货入库"),
    XSTHCK("004","销售退货出库"),
    DSDDCK("005","电商订单出库"),
    DBRK("006","调拨入库"),
    DBCK("007","调拨出库"),
    PYR("008","盘盈入"),
    PYC("009","盘亏出"),
    XH("0010","销毁"),
    QTC("0011","其他出"),
    QTR("0012","其他入"),
    YJPY("0013","月结盘盈"),
    YJPK("0014","月结盘亏"),
    HHPR("0015","好/坏品入"),
    HHPC("0016","好/坏品出"),
    XXTZR("0017","效期调整入"),
    XXTZC("0018","效期调整出"),
    HHBGR("0019","货号变更入"),
    HHBGC("0020","货号变更出"),
    DHLHC("0021","大货理货出"),
    DHLHR("0022","大货理货入"),
    WPHCR("0023","唯品红冲入"),
    CGZXR("0024","采购执行入"),
    CGZXC("0025","采购执行出"),
    GJCYC("0026","国检抽样出"),
	ZRGQC("0027","自然过期出"),
	ZRGQR("0028","自然过期入"),
	WPPDR("0029","唯品盘点入"),
	WPPDC("0030","唯品盘点出"),
	WPBFC("0031","唯品报废出"),
	DSTHR("0032","电商订单退货入"),
	KTRK("0033","客退入库"),
	ZDXSCK("0034","账单销售出库"),
	SJRK("0035","上架入库");

                           
	
	//key
	private String key;
	//value
	private String value;
	
	InventoryThingStatusEnum(String  key, String value) {
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
	
	
	public static String getInventoryThingStatusEnumValue(String key){
	    for(InventoryThingStatusEnum sourceStatusEnum : InventoryThingStatusEnum.values()){
	      if(StringUtils.equals(key, sourceStatusEnum.getKey())){
	        return sourceStatusEnum.getValue();
	      }
	    }
	    return "";
	  }
	
}
