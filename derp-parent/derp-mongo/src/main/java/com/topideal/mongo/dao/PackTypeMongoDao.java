package com.topideal.mongo.dao;

import com.topideal.mongo.entity.PackTypeMogo;

/**
 * 包装方式
 * @author zhanghx
 */
public interface PackTypeMongoDao extends MongoDao<PackTypeMogo>{


    void insertJson(String toString);
}
