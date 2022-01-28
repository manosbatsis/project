package com.topideal.service;

/**
 * 移动已完结冻结解冻数据到历史表
 * @author zhanghx
 */
public interface InventoryFreezeMoveService {

	/**
	 * 移动
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 */
	void synsMove(String json, String keys, String topics, String tags) throws Exception;
	
}
