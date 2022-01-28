package com.topideal.service.timer;

import java.sql.SQLException;

/**
 * 同步京东爬虫商品-每日销量库存
 */
public interface CrawlerJDGoodsAndStocksaleService {
	/**
	 * 同步京东爬虫商品-每日销量库存
	 * @param json
	 * @throws SQLException
	 */
	public void synsJDGoodsAndStocksale(String json, String keys, String topics, String tags) throws Exception;

}
