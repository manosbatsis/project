package com.topideal.mongo.dao;

import java.util.List;

import com.topideal.mongo.entity.UserBuRelMongo;

/**
 * 用户事业部表 dao
 * @author lian_
 *
 */
public interface UserBuRelMongoDao extends MongoDao<UserBuRelMongo>{

	public void insertJson(String json);
	
	/**
	 * 根据当前用户获取对应关联事业部
	 */
	public List<Long> getBuListByUser(Long userId) ;
	
	/**
	 * 判断该事业部是否与用户关联
	 */
	public boolean isUserRelateBu(Long userId, Long buId) ;
}
