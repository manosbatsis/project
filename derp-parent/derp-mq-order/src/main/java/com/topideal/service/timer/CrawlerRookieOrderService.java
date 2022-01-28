package com.topideal.service.timer;

import java.sql.SQLException;
/**
 * 获取菜鸟电商订单
 * @author 杨创
 *
 */
public interface CrawlerRookieOrderService {

	void getRookieOrderData(String json, String keys, String topics, String tags) throws SQLException;


}
