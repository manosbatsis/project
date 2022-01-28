package com.topideal.service.api.common;
/**
 * 同步数据到mongodb
 */
public interface ReportSysMongoDataService {
	/**
	 *同步数据到mongodb
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public boolean sysMongoData(String json, String keys, String topics, String tags)throws Exception;
}
