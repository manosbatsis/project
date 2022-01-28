package com.topideal.mongo.dao;

import com.topideal.mongo.entity.SupplierMerchandisePriceMongo;

public interface SupplierMerchandisePriceMongoDao extends MongoDao<SupplierMerchandisePriceMongo>{

	 void insertJson(String json);
}
