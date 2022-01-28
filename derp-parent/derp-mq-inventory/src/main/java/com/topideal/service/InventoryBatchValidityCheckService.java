package com.topideal.service;

/**
 *  校验批次库存明细商品效期是否已过期
 * @author baols
 *
 */
public interface InventoryBatchValidityCheckService {

	/**
	 *  校验批次库存明细商品效期是否已过期
	 * @author baols
	 *
	 */
    public boolean synsInventoryBatchValidityCheck(String json,String keys,String topics,String tags)throws Exception;





}
