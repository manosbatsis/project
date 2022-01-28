package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * 大货理货接口
 * 2019.04.01
 * 
 * 杨创
 */
public interface BigCargoTallyService {
	/**
	 * 大货理货接口
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public JSONObject bigCargoTally(String json,Long merchantId)throws Exception;

}
