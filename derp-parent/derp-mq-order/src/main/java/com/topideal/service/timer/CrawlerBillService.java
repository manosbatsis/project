package com.topideal.service.timer;

import java.sql.SQLException;

/**
 * 生成爬虫账单
 */
public interface CrawlerBillService {
	/**
	 * 生成爬虫账单
	 * @param json
	 * @throws SQLException 
	 */
	public void insertVIPCrawlerBill(String json,String keys,String topics,String tags) throws SQLException;
	/**
	 * 生成爬虫账单
	 * @param json
	 * @throws SQLException 
	 */
	public void insertYunJiCrawlerBill(String json,String keys,String topics,String tags) throws SQLException;
}
