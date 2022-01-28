package com.topideal.service.timer;

import java.sql.SQLException;

/**
 * 同步京东平台采购订单
 */
public interface CrawlerJDPurchaseOderService {

	/**
	 * 同步京东平台采购订单
	 * @param json
	 * @throws SQLException 
	 */
	public void insertJDPurchaseOder(String json,String keys,String topics,String tags) throws Exception;
	
	
}
