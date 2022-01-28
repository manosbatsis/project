package com.topideal.service.pushback;

/**
 * ofc盘点结果库存加减成功回推 
 * @author 杨创
 *2019/02/26
 */
public interface OfcInventoryResultsPushBackService {
	/**
	 * 修改仓储ofc盘点结果
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateOfcInventoryResultsPushBackInfo(String json, String keys, String topics, String tags)throws Exception;
}
