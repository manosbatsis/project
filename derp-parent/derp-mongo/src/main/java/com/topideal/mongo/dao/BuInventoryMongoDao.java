package com.topideal.mongo.dao;

import com.topideal.mongo.entity.BuInventoryMongo;

/**
 * Created by weixiaolei on 2018/7/4.
 */
public interface BuInventoryMongoDao extends MongoDao<BuInventoryMongo>{

    public void insertJson(String json);


}
