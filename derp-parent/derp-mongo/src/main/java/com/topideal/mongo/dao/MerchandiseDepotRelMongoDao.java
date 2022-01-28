package com.topideal.mongo.dao;

import com.topideal.mongo.entity.MerchandiseDepotRelMongo;

/**
 * 商品仓库关系表
 * @author lian_
 */
public interface MerchandiseDepotRelMongoDao extends MongoDao<MerchandiseDepotRelMongo>{

	public void insertJson(String json);
}
