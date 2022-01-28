package com.topideal.mongo.dao;

import com.topideal.mongo.entity.BrandMongo;
import com.topideal.mongo.entity.BrandParentMongo;

/**
 * 品牌
 * @author lian_
 */
public interface BrandMongoDao extends MongoDao<BrandMongo>{

	public void insertJson(String json);

	public BrandMongo getBrandMongo(Long goodsId);
}
