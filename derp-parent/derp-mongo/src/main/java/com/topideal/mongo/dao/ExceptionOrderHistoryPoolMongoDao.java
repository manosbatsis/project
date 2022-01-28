package com.topideal.mongo.dao;

import java.util.List;
import java.util.Map;

import com.topideal.mongo.entity.ExceptionOrderHistoryPoolMongo;

public interface ExceptionOrderHistoryPoolMongoDao extends MongoDao<ExceptionOrderHistoryPoolMongo>{
	
	/**
	 * 获取特定分组list
	 * @param param 
	 * @return
	 */
	List<ExceptionOrderHistoryPoolMongo> getsSpecifiedGroupList(Map<String, Object> param) ;
}
