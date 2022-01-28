package com.topideal.mongo.dao;

import com.topideal.mongo.entity.ReptileConfigMongo;

/**
 * 爬虫配置表 dao
 * @author lian_
 *
 */
public interface ReptileConfigMongoDao extends MongoDao<ReptileConfigMongo>{

	public void insertJson(String json);
	
}
