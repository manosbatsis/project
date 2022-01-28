package com.topideal.mongo.dao;

import com.topideal.mongo.entity.MerchandiseCustomsRelMongo;

/**
 * Created by weixiaolei on 2018/7/4.
 */
public interface MerchandiseCustomsRelMongoDao extends MongoDao<MerchandiseCustomsRelMongo>{

    public void insertJson(String json);

}
