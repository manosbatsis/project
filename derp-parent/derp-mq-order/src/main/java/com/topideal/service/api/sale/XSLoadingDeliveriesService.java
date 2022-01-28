package com.topideal.service.api.sale;
/**
 * 装载交运回推接口
 */
public interface XSLoadingDeliveriesService {
	// 保存装载交运信息
	public boolean saveLoadingDeliveriesInfo(String json,String keys,String topics,String tags)throws Exception;

}
