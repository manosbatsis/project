package com.topideal.service.api.purchase;

/**
 * 进仓单状态回推
 * @author zhanghx
 * 2018/7/16
 */
public interface PurchaseIntoWarehouseStatusService {
	
	/**
	 * 进仓单状态回推
	 * @param json 进仓单状态回推报文
	 * @return
	 * @throws Exception
	 */
	public boolean saveIntoWarehouseStatusInfo(String json,String keys,String topics,String tags) throws Exception;
	
}
