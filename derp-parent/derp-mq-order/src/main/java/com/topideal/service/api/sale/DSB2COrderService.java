package com.topideal.service.api.sale;
/**
 * B2C订单接口(电商交易订单)
 */
public interface DSB2COrderService {
	//保存B2C订单
	public boolean saveB2COrderInfo(String json,String keys,String topics,String tags)throws Exception;
}
