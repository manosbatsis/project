package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * 订单取消状态回推接口
 * @author 杨创
 *2018/6/28
 */
public interface OrderCancelStatusService {
	/**
	 * 保存订单取消信息
	 * @param json
	 * @return
	 */
	public JSONObject orderCancelInfo(String json,Long merchantId)throws Exception;

}
