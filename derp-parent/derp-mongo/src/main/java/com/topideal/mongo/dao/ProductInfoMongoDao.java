package com.topideal.mongo.dao;

import com.topideal.mongo.entity.ProductInfoMongo;

public interface ProductInfoMongoDao extends MongoDao<ProductInfoMongo>{

	public void insertJson(String json);
}
