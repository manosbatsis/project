package com.topideal.service.api;
/**
 * ofc仓储盘点结果
 * @author 杨创
 *2018/12/3
 */
public interface OfcInventoryResultsPushService {
	/**
	 * 保存仓储ofc盘点结果
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean saveOfcInventoryResultsPushInfo(String json, String keys, String topics, String tags)throws Exception;
}
