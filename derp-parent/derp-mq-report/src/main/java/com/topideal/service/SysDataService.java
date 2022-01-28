package com.topideal.service;

import java.sql.SQLException;

/**
 * 同步数据
 * @author zhanghx
 */
public interface SysDataService {

	/**
	 * 同步数据
	 * @param json
	 * @param topics
	 * @param tags
	 * @throws SQLException
	 */
	void synsData(String json, String keys, String topics, String tags) throws Exception;
}
