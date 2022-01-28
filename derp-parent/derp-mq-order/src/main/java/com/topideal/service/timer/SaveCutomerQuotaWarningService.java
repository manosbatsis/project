package com.topideal.service.timer;

/**
 * 生成客户额度预警
 * @date 2021-04-23
 */
public interface SaveCutomerQuotaWarningService {
	 
	void saveCutomerQuotaWarning(String json, String keys, String topics, String tags) throws Exception;
}
