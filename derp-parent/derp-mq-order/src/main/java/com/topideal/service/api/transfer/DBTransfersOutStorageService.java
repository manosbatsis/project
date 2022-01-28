package com.topideal.service.api.transfer;
/**
 * 调拨出库
 * @author YUCAIFU
 */
public interface DBTransfersOutStorageService {
	/**
	 * 调拨出库
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean saveTransfersOutStorageInfo(String json,String keys,String topics,String tags)throws Exception;

}
