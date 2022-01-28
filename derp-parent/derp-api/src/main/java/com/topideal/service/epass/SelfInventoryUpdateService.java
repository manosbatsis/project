package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * 自营库存更新
 */
public interface SelfInventoryUpdateService {
	/**
	 * 自营库存更新
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public JSONObject selfInventoryUpdate(String json,Long merchantId)throws Exception;

}
