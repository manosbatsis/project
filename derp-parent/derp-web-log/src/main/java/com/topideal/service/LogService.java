package com.topideal.service;

import java.util.List;
import java.util.Map;

import com.topideal.mongo.tools.PageMongo;

/**
 * Created by weixiaolei on 2018/7/19.
 */

public interface LogService {

	PageMongo searchPushAPILog(PageMongo pageMongo, String keyword, String state, String point, String endDateStr,
                               String id, String differenceTime, String selectScope);

	PageMongo searchOrderLog(PageMongo pageMongo, String keyword, String state, String point, String endDateStr,
                             String id, String differenceTime, String selectScope);

	PageMongo searchInventoryLog(PageMongo pageMongo, String keyword, String state, String point, String endDateStr,
                                 String id, String differenceTime, String selectScope);

	PageMongo searchStorageLog(PageMongo pageMongo, String keyword, String state, String point, String endDateStr,
                               String id, String differenceTime, String selectScope);

	/**
	 * 导出
	 * @param keyword
	 * @param state
	 * @param point
	 * @param endDateStr
	 * @return
	 * @throws Exception
	 */
	List searchListLog(String keyword, String state, String point, String endDateStr, String differenceTime, String selectScope, String collectionName, String historyCollectionName) throws Exception;
	
	/**
	 * 单个推送
	 * @param id
	 * @param collectionName
	 * @return
	 * @throws Exception
	 */
	boolean resetSend(String id, String collectionName) throws Exception;
	
	/**
	 * 批量推送
	 * @param ids
	 * @param collectionName
	 * @return
	 * @throws Exception
	 */
	boolean resetSend(List<String> ids, String collectionName) throws Exception;
	
	/**
	 * 导入
	 * @param data
	 * @return
	 */
	Map importByKeyword(Map<Integer, List<List<Object>>> data, String collectionName);
	
	/**
	 * 统计数量
	 * @param keyword
	 * @param state
	 * @param point
	 * @param endDateStr
	 * @param collectionName
	 * @return
	 */
	Long count(String keyword, String state, String point, String endDateStr, String differenceTime, String selectScope, String collectionName, String historyCollectionName);

}
