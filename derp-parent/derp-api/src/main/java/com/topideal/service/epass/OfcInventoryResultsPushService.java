package com.topideal.service.epass;

import net.sf.json.JSONObject;
/**
 * ofc盘点结果推送
 * @author 杨创
 *2018/11/30
 */
public interface OfcInventoryResultsPushService {
	/**
	 * 获取推送ofc盘点结果提供方的json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public JSONObject getOfcInventoryResultsPushInfo(String json)throws Exception;
}
