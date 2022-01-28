package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * 调拨出库
 * @author 杨创
 *2018/6/11
 */
public interface TransfersOutStorageService {
	/**
	 * 调拨出库
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public JSONObject transfersOutStorageInfo(String json,Long merchantId,String isRookie)throws Exception;

}
