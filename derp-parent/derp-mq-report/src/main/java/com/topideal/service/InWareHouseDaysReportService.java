package com.topideal.service;

/**
 * 在库天数明细Service
 * @author gy
 *
 */
public interface InWareHouseDaysReportService {

	void saveSummaryReport(String json, String keys, String topics, String tags) throws Exception;

}
