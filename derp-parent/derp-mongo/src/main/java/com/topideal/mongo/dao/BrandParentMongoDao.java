package com.topideal.mongo.dao;

import com.topideal.mongo.entity.BrandParentMongo;

/**
 * 品牌
 * @author lian_
 */
public interface BrandParentMongoDao extends MongoDao<BrandParentMongo>{

	public void insertJson(String json);

	public BrandParentMongo getBrandParentMongo(Long goodsId);

	public BrandParentMongo getBrandParentMongoByCommbarcode(String commbarcode);

}
