package com.topideal.service.api.sale;
/**
 * 电商菜鸟装载交运回推接口
 */
public interface DSRookieLoadingDeliveriesService {
	// 电商菜鸟保存装载交运信息
	public boolean saveRookieLoadingDeliveriesInfo(String json,String keys,String topics,String tags)throws Exception;

}

