package com.topideal.service.pushback.sale;
/**
 * 销售退消费者退货入库库存加减成功回推
 * 2019/07/25
 * 文艳
 */
public interface XSTConsumerInStoragePushBackService {
	/**
	 *  销售退消费者退货入库库存加减成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateXSTConsumerInStoragePushBack(String json,String keys,String topics,String tags)throws Exception;
}
