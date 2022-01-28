package com.topideal.service.pushback.sale;

/**
 * 进仓单状态回推库存加减成功回推 
 * 2019/02/27
 * 杨创
 */
public interface XSTIntoWarehouseStatusPushBackService {
	/**
	 * 进仓单状态回推库存加减成功回推 
	 * @param json 进仓单状态回推报文
	 * @return
	 * @throws Exception
	 */
	public boolean updatexSTIntoWarehouseStatusPushBackInfo(String json,String keys,String topics,String tags)throws Exception;
}
