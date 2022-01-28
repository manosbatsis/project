package com.topideal.service.api.transfer;
/**
 * 调拨入库接口
 * @author yucaifu

 */
public interface DBTransfersInStorageService {
	/**
	 * 调拨入库接口
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean saveTransfersInStorage(String json,String keys,String topics,String tags)throws Exception;
}
