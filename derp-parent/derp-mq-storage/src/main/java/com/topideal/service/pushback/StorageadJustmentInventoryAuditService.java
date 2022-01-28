package com.topideal.service.pushback;
/**
 * 库存调整单页面审核 库存加减成功回推
 * 2019/02/27
 * 杨创
 */
public interface StorageadJustmentInventoryAuditService {
	/**
	 * 库存调整单页面审核 库存加减成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateAdjustmentType(String json, String keys, String topics, String tags)throws Exception;
}
