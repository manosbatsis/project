package com.topideal.service.pushback.purchase;

import java.sql.SQLException;

/**
 * 采购 进仓状态回推
 * @author zhanghx
 */
public interface WarehouseStatusPushbackService {

	/**
	 * 采购 进仓状态回推
	 * @throws SQLException
	 * @throws Exception 
	 */
	void modifyStatus(String json, String keys, String topics, String tags) throws Exception;
	
}
