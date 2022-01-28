package com.topideal.service.api.transfer;
/**
 * 装载交运回推接口
 * @author YUCAIFU
 */
public interface DBLoadingDeliveriesService {
	// 保存装载交运信息
	public boolean saveLoadingDeliveriesInfo(String json,String keys,String topics,String tags)throws Exception;

}
