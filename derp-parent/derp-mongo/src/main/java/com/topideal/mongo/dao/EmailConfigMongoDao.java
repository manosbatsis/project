package com.topideal.mongo.dao;

import com.topideal.mongo.entity.EmailConfigMongo;

/**
 * Created by weixiaolei on 2018/7/4.
 */
public interface EmailConfigMongoDao extends MongoDao<EmailConfigMongo>{

    public void insertJson(String json);


}
