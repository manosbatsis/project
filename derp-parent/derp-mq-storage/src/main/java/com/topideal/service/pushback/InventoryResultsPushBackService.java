package com.topideal.service.pushback;
/**
 *  仓储盘点结果库存加减成功回推 
 * @author 杨创
 *2019/02/26
 */
public interface InventoryResultsPushBackService {
	/**
	 * 仓储盘点结果库存加减成功回推 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateinventoryResultsPushBackInfo(String json, String keys, String topics, String tags)throws Exception;
}
