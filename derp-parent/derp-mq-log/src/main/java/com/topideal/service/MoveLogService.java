package com.topideal.service;

/**
 * 日志搬迁
 * @author zhanghx
 */
public interface MoveLogService {

	/**
	 * 日志搬迁
	 * @param json
	 * @param topics
	 * @param tags
	 * @throws Exception
	 */
	void synsMoveLog(String json, String topics, String tags) throws Exception;
	
}
