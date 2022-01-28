package com.topideal.service.pushback.sale;

import java.sql.SQLException;

/**
 * 领料出库 回推
 */
public interface MaterialOutDepotPushbackService {

	/**
	 *领料出库 回推
	 * @throws SQLException
	 */
	void modifyStatus(String json, String keys, String topics, String tags) throws Exception;
	
}
