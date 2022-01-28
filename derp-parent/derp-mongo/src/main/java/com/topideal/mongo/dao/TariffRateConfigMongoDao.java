package com.topideal.mongo.dao;

import com.topideal.mongo.entity.TariffRateConfigMongo;

public interface TariffRateConfigMongoDao extends MongoDao<TariffRateConfigMongo>{

    public void insertJson(String json);

}
