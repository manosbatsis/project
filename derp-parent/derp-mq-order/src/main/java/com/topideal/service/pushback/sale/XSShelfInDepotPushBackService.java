package com.topideal.service.pushback.sale;
/**
 * 销售上架入库库存加减成功回推
 * @author wenyan
 *
 */
public interface XSShelfInDepotPushBackService {
	/**
	 *  销售上架入库库存加减成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateXSShelfInDepotPushPushBack(String json,String keys,String topics,String tags)throws Exception;
}
