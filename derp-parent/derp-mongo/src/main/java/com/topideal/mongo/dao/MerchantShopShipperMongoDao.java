package com.topideal.mongo.dao;

import com.topideal.mongo.entity.MerchantShopShipperMongo;

/**
 * 店铺货主表 dao
 * @author lian_
 *
 */
public interface MerchantShopShipperMongoDao extends MongoDao<MerchantShopShipperMongo>{

	public void insertJson(String json);
	
}
