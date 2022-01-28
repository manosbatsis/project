package com.topideal.mongo.dao;

import com.topideal.mongo.entity.MerchantBuRelMongo;

/**
 * 公司事业部信息 mongoDao
 * @author lian_
 */
public interface MerchantBuRelMongoDao extends MongoDao<MerchantBuRelMongo>{

	public void insertJson(String json);

	//public boolean isMerchantIdRelateBuCode(Long merchantId,String buCode);

	//public MerchantBuRelMongo findBuName(Long merchantId,String buCode);
}
