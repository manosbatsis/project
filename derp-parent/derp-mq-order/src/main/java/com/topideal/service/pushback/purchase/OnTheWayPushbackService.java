package com.topideal.service.pushback.purchase;

import java.sql.SQLException;

/**
 * 在途仓入库 回推
 * @author zhanghx
 */
public interface OnTheWayPushbackService {

	/**
	 * 在途仓入库 回推
	 * @throws SQLException
	 */
	void modifyStatus(String json, String keys, String topics, String tags) throws SQLException;

}
