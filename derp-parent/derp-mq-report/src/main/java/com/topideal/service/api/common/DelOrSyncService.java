package com.topideal.service.api.common;
/**
 * 删除/同步数据 分发方法
 */
public interface DelOrSyncService {
	/**
	 *删除/同步数据 分发方法
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public boolean getDelOrSync(String json, String keys, String topics, String tags)throws Exception;
}
