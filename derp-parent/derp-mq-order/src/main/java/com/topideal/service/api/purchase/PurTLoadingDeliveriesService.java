package com.topideal.service.api.purchase;
/**
 * 装载交运-采购退货回推接口
 */
public interface PurTLoadingDeliveriesService {
	// 保存装载交运-采购退货信息
	public boolean saveLoadingDeliveriesInfo(String json,String keys,String topics,String tags)throws Exception;

}
