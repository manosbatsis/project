package com.topideal.mongo.dao;

import com.topideal.mongo.entity.CustomerSalePriceMongo;

/**
 * Created by weixiaolei on 2018/7/4.
 */
public interface CustomerSalePriceMongoDao extends MongoDao<CustomerSalePriceMongo>{

    public void insertJson(String json);

}
