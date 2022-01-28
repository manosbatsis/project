package com.topideal.service.order;
/**
 * 获取业务端数据推送库存 
 */
public interface GetBusinessDataPushInventoryService {
	//保存B2C订单
	public boolean getInfo(String json,String keys,String topics,String tags)throws Exception;
}
