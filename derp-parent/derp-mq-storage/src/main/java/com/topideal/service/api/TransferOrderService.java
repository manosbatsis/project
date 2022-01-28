package com.topideal.service.api;
/**
 * 调拨单回推
 */
public interface TransferOrderService {
	/**
	 * 保存类型调整单
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean saveAdjustmentType(String json, String keys, String topics, String tags)throws Exception;
}
