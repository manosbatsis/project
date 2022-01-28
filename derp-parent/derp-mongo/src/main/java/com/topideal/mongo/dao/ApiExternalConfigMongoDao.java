package com.topideal.mongo.dao;

import com.topideal.mongo.entity.ApiExternalConfigMongo;

/**
 * 对外接口配置Dao
 *
 */
public interface ApiExternalConfigMongoDao extends MongoDao<ApiExternalConfigMongo>{

	public void insertJson(String json);
	
}
