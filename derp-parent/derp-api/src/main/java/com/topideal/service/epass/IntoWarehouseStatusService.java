package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * 进仓单状态回推
 * @author 杨创
 *2018/5/25
 */
public interface IntoWarehouseStatusService {
	/**
	 * 进仓单状态回推
	 * @param json 进仓单状态回推报文
	 * @return
	 * @throws Exception
	 */
	public JSONObject intoWarehouseStatusInfo(String json,Long merchantId,String isRookie,String contractNoTag)throws Exception;
}
