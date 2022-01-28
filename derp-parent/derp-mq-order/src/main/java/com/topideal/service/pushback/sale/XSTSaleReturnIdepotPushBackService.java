package com.topideal.service.pushback.sale;
/**
 * 销售退消费者退货入库库存加减成功回推
 * 2020/10/25
 * 杨创
 */
public interface XSTSaleReturnIdepotPushBackService {
	/**
	 *  销售退消费者退货入库库存加减成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateXSTSaleReturnIdepotPushBack(String json,String keys,String topics,String tags)throws Exception;
}
