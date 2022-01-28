package com.topideal.service.api.sale;
/**
 * 进仓单状态回推
 * @author 杨创
 *2018/5/25
 */
public interface XSTIntoWarehouseStatusService {
	/**
	 * 进仓单状态回推
	 * @param json 进仓单状态回推报文
	 * @return
	 * @throws Exception
	 */
	public boolean saveIntoWarehouseStatusInfo(String json,String keys,String topics,String tags)throws Exception;
}
