package com.topideal.service.api;
/**
 * 仓储盘点结果
 * @author 杨创
 *2018/7/14
 */
public interface InventoryResultsPushService {
	/**
	 * 保存仓储盘点结果
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean saveInventoryResultsPushInfo(String json, String keys, String topics, String tags)throws Exception;
}
