package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 *出库明细
 * @author 杨创
 *2020.06.08
 */
public interface OutInventoryService {
	// 出库明细
	public JSONObject outInventoryDetail (String json,Long merchantId)throws Exception;


}
