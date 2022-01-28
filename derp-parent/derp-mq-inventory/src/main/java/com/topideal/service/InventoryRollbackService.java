package com.topideal.service;

/**
 * 库存回滚 
 * @author 杨创
 *2019-07-09
 */
public interface InventoryRollbackService {

	/**
	 *库存回滚
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 */
	void saveinventoryRollbackInfo(String json, String keys, String topics, String tags) throws Exception;
	
}
