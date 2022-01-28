package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * 调拨入库接口
 * @author 杨创
 *2018/6/11
 */
public interface TransfersInStorageService {
	/**
	 * 调拨入库接口
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public JSONObject transfersInStorage(String json,Long merchantId,String isRookie)throws Exception;
}
