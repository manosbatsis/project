package com.topideal.json.inventory.j04;

import java.io.Serializable;
import java.util.Map;

/**
 *回调通知业务端扣减库存成功实体
 * */
public class BackRootJson implements Serializable{
   
	private String code;//扣减库存单号

	private String merchantName;//商家名称
	
	private Map<String, Object> customParam;//自定义回调参数

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, Object> getCustomParam() {
		return customParam;
	}

	public void setCustomParam(Map<String, Object> customParam) {
		this.customParam = customParam;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
}
