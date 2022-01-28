package com.topideal.service.pushback.sale;

import java.sql.SQLException;

/**
 * 销售订单-装载交运库存扣减成功回推 
 * 杨创
 * 2019/02/27
 */
public interface XSLoadingDeliveriesPushBackService {

	/**
	 * 销售订单-装载交运库存扣减成功回推 
	 * @throws SQLException
	 */
	void updateXSLoadingDeliveriesPushBackInfo(String json, String keys, String topics, String tags) throws Exception;

}
