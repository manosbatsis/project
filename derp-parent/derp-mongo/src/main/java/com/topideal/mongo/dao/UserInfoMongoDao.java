package com.topideal.mongo.dao;

import com.topideal.mongo.entity.UserInfoMongo;

/**
 * 用户 dao
 * @author t
 *
 */
public interface UserInfoMongoDao extends MongoDao<UserInfoMongo>{

	public void insertJson(String json);

}
