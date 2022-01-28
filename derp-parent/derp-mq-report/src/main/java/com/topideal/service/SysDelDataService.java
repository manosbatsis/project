package com.topideal.service;

import java.sql.SQLException;

/**
 * 删除过期数据
 * @author zhanghx
 */
public interface SysDelDataService {

	/**
	 * 删除过期数据
	 * @param json
	 * @param topics
	 * @param tags
	 * @throws SQLException
	 */
	void delData(String json, String keys, String topics, String tags) throws Exception;
}
