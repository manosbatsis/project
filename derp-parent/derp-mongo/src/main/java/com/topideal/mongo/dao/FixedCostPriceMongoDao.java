package com.topideal.mongo.dao;

import com.topideal.mongo.entity.FixedCostPriceMongo;

import java.util.List;

public interface FixedCostPriceMongoDao  extends MongoDao<FixedCostPriceMongo>{
    void insertJson(String json);

    List<FixedCostPriceMongo> findAllByIn(String keyName, List valueList);

}
