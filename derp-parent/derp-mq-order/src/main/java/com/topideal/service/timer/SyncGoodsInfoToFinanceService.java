package com.topideal.service.timer;

/**
 * 销售订单商品同步到金融系统
 * @author Administrator
 *
 */
public interface SyncGoodsInfoToFinanceService {
	/**
	  * 商品同步到金融系统
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @throws Exception
	 */
	public void syncGoodsInfoToFinance(String json,String keys,String topics,String tags) throws Exception;
}
