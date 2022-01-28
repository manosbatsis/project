package com.topideal.service;

import java.util.List;

import com.topideal.mongo.tools.PageMongo;

/**
 * 报表service
 * @author zhanghx
 */
public interface ReportLogService {

	/**
	 * 分页
	 * @param pageMongo
	 * @param state
	 * @param point
	 * @param endDateStr
	 * @return
	 */
	PageMongo searchLog(PageMongo pageMongo, String state, String point, String endDateStr);
	
	/**
	 * 统计数量
	 * @param keyword
	 * @param state
	 * @param point
	 * @param endDateStr
	 * @param collectionName
	 * @return
	 */
	Long count(String state, String point);

	/**
	 * 导出
	 * @param keyword
	 * @param state
	 * @param point
	 * @param endDateStr
	 * @return
	 * @throws Exception
	 */
	List searchListLog(String state, String point) throws Exception;
	
	
}
