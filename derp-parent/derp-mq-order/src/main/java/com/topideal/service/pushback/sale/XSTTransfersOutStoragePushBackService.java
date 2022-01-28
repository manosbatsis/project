package com.topideal.service.pushback.sale;
/**
 * 调拨出库库存加减成功回推
 * 2019/02/27
 * 杨创
 */
public interface XSTTransfersOutStoragePushBackService {
	/**
	 * 调拨出库库存加减成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateXSTTransfersOutStoragePushBackInfo(String json,String keys,String topics,String tags)throws Exception;

}
