package com.topideal.service;

/**
 * 销售目标达成
 * @author Guobs
 *
 */
public interface SaleTargetAchievementService {

	void saveSummaryReport(String json, String keys, String topics, String tags) throws Exception;

}
