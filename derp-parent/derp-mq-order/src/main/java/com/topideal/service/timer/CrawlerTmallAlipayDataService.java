package com.topideal.service.timer;

import java.sql.SQLException;

public interface CrawlerTmallAlipayDataService {
	// 同步每日结算数据汇总
	void insertDailySettleBatch(String json, String keys, String topics, String tags) throws SQLException;
	//同步每日结算明细数据
	void insertDailySettle(String json, String keys, String topics, String tags) throws SQLException;
	//同步每日结算明细数据
	void insertDailyFee(String json, String keys, String topics, String tags) throws SQLException;
	//每月结算费用
	void insertMonthlyFee(String json, String keys, String topics, String tags) throws SQLException;

	
}
