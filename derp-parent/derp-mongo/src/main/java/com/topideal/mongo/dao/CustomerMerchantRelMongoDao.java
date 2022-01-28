package com.topideal.mongo.dao;

import com.topideal.mongo.entity.CustomerMerchantRelMongo;

/**
 * Created by weixiaolei on 2018/7/4.
 */
public interface CustomerMerchantRelMongoDao extends MongoDao<CustomerMerchantRelMongo>{

    public void insertJson(String json);
    
}
