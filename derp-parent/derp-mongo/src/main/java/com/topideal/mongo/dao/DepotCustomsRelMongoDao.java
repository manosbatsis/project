package com.topideal.mongo.dao;

import com.topideal.mongo.entity.DepotCustomsRelMongo;

public interface DepotCustomsRelMongoDao extends MongoDao<DepotCustomsRelMongo>{
	public void insertJson(String json);
}
