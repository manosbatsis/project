package com.topideal.service;

/**
 *事业部-财务经销存
 * @author 
 */
public interface BuFinanceInventorySummaryService {

	/**
	 * 保存事业部-财务经销存
	 * @param json
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public void saveSummaryReport(String json, String key, String topics, String tags) throws Exception;
	
}
