package com.topideal.service.timer;

import java.sql.SQLException;

/**
 * 同步天猫平台采购订单
 */
public interface CrawlerTmallPurchaseOderService {

	/**
	 * 同步天猫平台采购订单
	 * @param json
	 * @throws SQLException 
	 */
	public void insertTmallPurchaseOder(String json,String keys,String topics,String tags) throws Exception;
	
	
}
