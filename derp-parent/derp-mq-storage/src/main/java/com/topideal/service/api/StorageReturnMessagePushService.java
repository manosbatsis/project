package com.topideal.service.api;
/**
 * 仓储退运信息接
 * @author 杨创
 *2018/7/14
 */
public interface StorageReturnMessagePushService {
	/**
	 * 保存仓储退运信息接
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean saveStorageReturnMessagePushInfo(String json, String keys, String topics, String tags)throws Exception;
}
