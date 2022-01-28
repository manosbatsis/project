package com.topideal.service;

import java.sql.SQLException;

/**
 * 删除月结明细数据 
 */
public interface DelReportDataService {

	/**
	 * 删除报表数据
	 * @param json
	 * @param topics
	 * @param tags
	 * @throws SQLException
	 */
	void delReportData(String json, String keys, String topics, String tags) throws Exception;
}
