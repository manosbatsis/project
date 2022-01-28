package com.topideal.mongo.dao;

import com.topideal.mongo.entity.FileTempDataMongo;

/**
 * 文件模版数据jsonDao
 *
 */
public interface FileTempDataMongoDao extends MongoDao<FileTempDataMongo>{

	public void insertJson(String json);
	
}
