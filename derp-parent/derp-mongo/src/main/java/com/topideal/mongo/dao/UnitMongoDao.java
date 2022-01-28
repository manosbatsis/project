package com.topideal.mongo.dao;

import com.topideal.mongo.entity.UnitMongo;

/**
 * 单位表 dao
 * @author t
 *
 */
public interface UnitMongoDao extends MongoDao<UnitMongo>{

	public void insertJson(String json);

}
