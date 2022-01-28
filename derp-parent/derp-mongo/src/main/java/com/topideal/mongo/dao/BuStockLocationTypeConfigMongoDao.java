package com.topideal.mongo.dao;

import com.topideal.mongo.entity.BuStockLocationTypeConfigMongo;

public interface BuStockLocationTypeConfigMongoDao extends MongoDao<BuStockLocationTypeConfigMongo>{

    public void insertJson(String json);
}
