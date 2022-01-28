package com.topideal.service.pushback.purchase;

import java.sql.SQLException;

/**
 * 采购 确认入仓 回推
 * @author zhanghx
 */
public interface ConfirmDepotPushbackService {

	/**
	 * 确认入仓 回推
	 * @throws SQLException
	 */
	void modifyStatus(String json, String keys, String topics, String tags) throws SQLException;
	
}
