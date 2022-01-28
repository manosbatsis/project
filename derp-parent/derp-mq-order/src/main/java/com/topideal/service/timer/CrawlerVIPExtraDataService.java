package com.topideal.service.timer;

import java.sql.SQLException;

public interface CrawlerVIPExtraDataService {

	void insertVIPCrawlerExtraData(String json, String keys, String topics, String tags) throws SQLException;

	void insertVIPCrawlerFileData(String json, String keys, String topics, String tags) throws SQLException;

}
