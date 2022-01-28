package com.topideal.service.pushback.sale;

import java.sql.SQLException;

/**
 * 销售 中转仓出库 回推
 * @author zhanghx
 */
public interface SaleOrderStockOutPushbackService {

	/**
	 * 中转仓出库 回推
	 * @throws SQLException
	 */
	void modifyStatus(String json, String keys, String topics, String tags) throws Exception;
	
}
