package com.topideal.mongo.dao;

import com.topideal.mongo.entity.CountryOriginMongo;

/**
 * 原产国 dao
 * @author lian_
 */
public interface CountryOriginMongoDao extends MongoDao<CountryOriginMongo>{

	public void insertJson(String json);
	
}
