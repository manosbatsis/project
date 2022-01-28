package com.topideal.mongo.dao;

import java.util.List;

import com.topideal.mongo.entity.ExceptionOrderPoolMongo;

public interface ExceptionOrderPoolMongoDao extends MongoDao<ExceptionOrderPoolMongo>{

	List<ExceptionOrderPoolMongo> getSpecifiedGroupList(String operatorType, String expType, String keywords);

}
