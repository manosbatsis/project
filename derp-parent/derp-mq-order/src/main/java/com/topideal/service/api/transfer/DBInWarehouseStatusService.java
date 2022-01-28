package com.topideal.service.api.transfer;
/**
 * 进仓单状态回推
 * @author yucaifu

 */
public interface DBInWarehouseStatusService {
	/**
	 * 进仓单状态回推
	 * @param json 进仓单状态回推报文
	 * @return
	 * @throws Exception
	 */
	public boolean saveIntoWarehouseStatusInfo(String json,String keys,String topics,String tags)throws Exception;
}
