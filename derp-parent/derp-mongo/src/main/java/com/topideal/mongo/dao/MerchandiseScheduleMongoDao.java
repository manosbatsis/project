package com.topideal.mongo.dao;

import com.topideal.mongo.entity.MerchandiseScheduleMongo;


public interface MerchandiseScheduleMongoDao extends MongoDao<MerchandiseScheduleMongo>{

    public void insertJson(String json);


}
