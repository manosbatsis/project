package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * 退运单信息推送Service
 * @author 杨创
 *2018/6/17
 */
public interface ReturnMessagePushService {
	/**
	 * 退运单信息推送
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public JSONObject returnMessagePushInfo (String json, Long merchantId,String isRookie)throws Exception;

}
