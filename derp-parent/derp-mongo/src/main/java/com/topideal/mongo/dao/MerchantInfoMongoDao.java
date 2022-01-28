package com.topideal.mongo.dao;

import com.topideal.mongo.entity.MerchantInfoMongo;

import java.util.List;

/**
 * 商家信息 mongoDao
 * @author lian_
 */
public interface MerchantInfoMongoDao extends MongoDao<MerchantInfoMongo>{

	public void insertJson(String json);

	List<MerchantInfoMongo> findAllByIn(String s, List<String> topidealCodeList);
}
