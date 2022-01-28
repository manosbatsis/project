package com.topideal.mongo.dao;

import com.topideal.mongo.entity.MerchantDepotBuRelMongo;

/**
 * 公司事业部信息 mongoDao
 * @author lian_
 */
public interface MerchantDepotBuRelMongoDao extends MongoDao<MerchantDepotBuRelMongo>{

	public void insertJson(String json);
}
