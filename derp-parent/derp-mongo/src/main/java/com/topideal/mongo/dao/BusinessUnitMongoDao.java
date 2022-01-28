package com.topideal.mongo.dao;

import com.topideal.mongo.entity.BusinessUnitMongo;

import java.util.List;

public interface BusinessUnitMongoDao  extends MongoDao<BusinessUnitMongo>{

    public void insertJson(String json);

    public List<BusinessUnitMongo> findAllByIn(String keyName, List valueList);
}
