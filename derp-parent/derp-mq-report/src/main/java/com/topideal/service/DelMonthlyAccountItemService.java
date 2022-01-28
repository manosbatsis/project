package com.topideal.service;

import java.sql.SQLException;

/**
 * 删除月结明细数据
 */
public interface DelMonthlyAccountItemService {

	/**
	 * 删除月结明细数据
	 * @param json
	 * @param topics
	 * @param tags
	 * @throws SQLException
	 */
	void delMonthlyAccountItemData(String json, String keys, String topics, String tags) throws Exception;
}
