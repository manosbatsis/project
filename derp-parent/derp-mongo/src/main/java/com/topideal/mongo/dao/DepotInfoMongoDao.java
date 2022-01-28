package com.topideal.mongo.dao;

import java.util.List;
import java.util.Map;

import com.topideal.mongo.entity.DepotInfoMongo;

/**
 * Created by weixiaolei on 2018/7/4.
 */
public interface DepotInfoMongoDao extends MongoDao<DepotInfoMongo>{

    public void insertJson(String json);


}
