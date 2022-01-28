package com.topideal.service.epass;

import net.sf.json.JSONObject;
/**
 * 盘点结果推送
 * @author 杨创
 *2018/7/13
 */
public interface InventoryResultsPushService {
	/**
	 * 获取推送盘点结果提供方的json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public JSONObject InventoryResultsPushInfo(String json,Long merchantId)throws Exception;
}
