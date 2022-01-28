package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * 装载交运回推接口
 * @author 杨创
 *2018/6/5
 */
public interface LoadingDeliveriesService {
	// 保存装载交运信息
	public JSONObject loadingDeliveriesInfo(String json,Long merchantId,String isRookie)throws Exception;
	/**
	 * 菜鸟仓库装载交运
	 * @param json
	 * @param merchantId
	 * @param isRookie
	 * @return
	 * @throws Exception
	 */
/*	public JSONObject rookieLoadingDeliveriesInfo(String json,Long merchantId,String isRookie)throws Exception;*/

}
