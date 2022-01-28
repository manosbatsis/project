package com.topideal.mongo.dao;

import com.topideal.mongo.entity.BrandSuperiorMongo;

/**
 * 品牌
 * @author lian_
 */
public interface BrandSuperiorMongoDao extends MongoDao<BrandSuperiorMongo>{

	public void insertJson(String json);

	public BrandSuperiorMongo getBrandSuperiorByGoodsId(Long goodsId);

}
