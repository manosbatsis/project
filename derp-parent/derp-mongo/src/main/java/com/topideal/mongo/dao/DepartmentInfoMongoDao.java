package com.topideal.mongo.dao;

import com.topideal.mongo.entity.DepartmentInfoMongo;

import java.util.List;

public interface DepartmentInfoMongoDao extends MongoDao<DepartmentInfoMongo>{

    public void insertJson(String json);

    public List<DepartmentInfoMongo> findAllByIn(String keyName, List valueList);
}
