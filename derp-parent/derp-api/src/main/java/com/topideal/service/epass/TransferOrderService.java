package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * 调拨单回推
 */
public interface TransferOrderService {
	/**
	 * 调拨单回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public JSONObject transferOrderPush(String json,Long merchantId)throws Exception;

}
