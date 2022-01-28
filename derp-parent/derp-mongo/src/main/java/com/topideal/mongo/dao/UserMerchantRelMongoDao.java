package com.topideal.mongo.dao;

import com.topideal.mongo.entity.UserMerchantRelMongo;

import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/9/9 17:29
 * @Description: 用户公司关联表
 */
public interface UserMerchantRelMongoDao extends MongoDao<UserMerchantRelMongo>{

	public void insertJson(String json);
	
	/**
	 * 根据当前用户获取对应关联公司
	 */
	public List<Long> getMerchantListByUser(Long userId) ;
	
	/**
	 * 判断该公司是否与用户关联
	 */
	public boolean isUserRelateMerchant(Long userId, Long buId) ;
}
