package com.topideal.service.pushback.sale;

import java.sql.SQLException;

/**
 *  电商订单-装载交运库存扣减成功回推 
 * @author 杨创
 *2019/02/27
 */
public interface DSLoadingDeliveriesPushBackService {

	/**
	 * 电商订单-装载交运库存扣减成功回推 
	 * @throws SQLException
	 */
	void updateDSLoadingDeliveriesPushBackInfo(String json, String keys, String topics, String tags) throws Exception;

}
