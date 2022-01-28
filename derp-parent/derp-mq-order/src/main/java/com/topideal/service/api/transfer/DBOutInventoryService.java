package com.topideal.service.api.transfer;
/**
 * 出库明细-调拨模块
 */
public interface DBOutInventoryService {
	/**
	 * 出库明细调拨模块
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean saveDBOutInventoryInfo(String json,String keys,String topics,String tags)throws Exception;
}
