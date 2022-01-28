package com.topideal.service;

import java.util.List;

import com.topideal.mongo.tools.PageMongo;

/**
 * Created by weixiaolei on 2018/8/6.
 */
public interface APILogService {

	PageMongo searchLogByPage(PageMongo pageMongo, String keyword, String state, String point, String derpCode,
                              String endDateStr, String id, String selectScope);

	boolean resetSend(String id, String collectionName) throws Exception;

	boolean resetSend(List<String> ids, String collectionName) throws Exception;
}
