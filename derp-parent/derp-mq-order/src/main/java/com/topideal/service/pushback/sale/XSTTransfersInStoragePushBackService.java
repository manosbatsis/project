package com.topideal.service.pushback.sale;
/**
 *销售调拨入库库存加减成功回推
 * 2019/02/27
 * 杨创
 */
public interface XSTTransfersInStoragePushBackService {
	/**
	 * 销售调拨入库库存加减成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateXSTTransfersInStoragePushBack(String json,String keys,String topics,String tags)throws Exception;
}
