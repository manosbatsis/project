package com.topideal.service;

/**
 *事业部-财务经销存利息数据处理
 * @author 
 */
public interface DataManageService {

	/**
	 * 事业部-财务经销存利息数据处理
	 * @param json
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public void saveDataManageReport(String json, String key, String topics, String tags) throws Exception;
	
}
