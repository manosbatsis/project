package com.topideal.mongo.dao;

import com.topideal.mongo.entity.PurchaseCommissionMongo;

public interface PurchaseCommissionMongoDao extends MongoDao<PurchaseCommissionMongo>{
	
	public void insertJson(String json) ;
	
}
