package com.topideal.mongo.dao;

import com.topideal.mongo.entity.ApiSecretConfigMongo;

/**
 * 商家信息Dao
 * @author liuhanguang
 *
 */
public interface ApiSecretConfigMongoDao extends MongoDao<ApiSecretConfigMongo>{

	public void insertJson(String json);
	
}
