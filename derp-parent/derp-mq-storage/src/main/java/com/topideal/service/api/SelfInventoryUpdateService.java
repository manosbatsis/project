package com.topideal.service.api;
/**
 * 自营库存更新
 */
public interface SelfInventoryUpdateService {
	/**
	 * 保存类型调整单
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean saveAdjustmentType(String json, String keys, String topics, String tags)throws Exception;
}
