package com.topideal.service.timer;

import java.sql.SQLException;

public interface FetchingYunJiInventoryService {

	
	/**
	 * 同步云集爬虫数据
	 * @param json
	 * @param key
	 * @param topics
	 * @param tags
	 * @throws SQLException 
	 */
	public void fetchingData(String json, String key, String topics, String tags) throws SQLException;

}
