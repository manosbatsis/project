package com.topideal.common.enums;
/**
 * 店铺类型枚举
 * @author wy
 *
 */
public enum ShopTypeEnum {
	
	DPLX_POP("001","POP"),
	DPLX_YJDF("002","一件代发");
		
	private String key ; 
	
	private String value ;
	
	ShopTypeEnum(String key , String value) {
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
	
		public static String getValueByKey(String key) {
				String value = "" ;
				for(ShopTypeEnum shopType : ShopTypeEnum.values()) {
					if(shopType.getKey().equals(key)) {
						value = shopType.getValue() ;
						break ;
					}
				}
				return value ;
			}
}
