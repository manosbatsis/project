package com.topideal.service;

/**
 *  删除批次库存余量为0的数据
 * @author baols
 *
 */
public interface InventoryDeleteDataForZeroService {

	/**
	 *  删除批次库存余量为0的数据
	 * @author baols
	 *
	 */
    public boolean deleteInventoryDeleteDataForZero(String json,String keys,String topics,String tags)throws Exception;





}
