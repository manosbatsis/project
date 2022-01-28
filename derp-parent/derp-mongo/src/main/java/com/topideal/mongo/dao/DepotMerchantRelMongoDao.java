package com.topideal.mongo.dao;

import com.topideal.mongo.entity.DepotMerchantRelMongo;

/**
 * Created by weixiaolei on 2018/7/4.
 */
public interface DepotMerchantRelMongoDao extends MongoDao<DepotMerchantRelMongo>{

    public void insertJson(String json);


}
