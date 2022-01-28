package com.topideal.service;


/**
 * 抓取经分销采购入库单入库仓库为保税仓、海外仓的入库数据
 */
public interface GrabSettlementPriceWarnconfigService {
	/**
	 * 抓取经分销采购入库单入库仓库为保税仓、海外仓的入库数据
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public boolean saveGrabDerpPurchaseIdepotOrder(String json, String keys, String topics, String tags) throws Exception;
	

}
