package com.topideal.service;

import com.topideal.entity.dto.ResponseRoot;

import net.sf.json.JSONObject;
/**
 * 库存管理 -批次库存明细-service实现类
 */
public interface InventoryBatchService {
	
	/**
	 * 批次库存明细列表（分页）
	 * @param jsonData
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	ResponseRoot queryInventoryBatch(JSONObject jsonData)throws Exception;

	
}
