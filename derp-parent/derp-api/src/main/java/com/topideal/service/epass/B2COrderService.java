package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * B2C订单接口(电商交易订单)
 * @author 杨创
 *2018/6/4
 */
public interface B2COrderService {
	//保存B2C订单
	public JSONObject b2COrderInfo(String json,Long merchantId)throws Exception;
}
