package com.topideal.mongo.dao;

import com.topideal.mongo.entity.MerchandiseExternalWarehouseMongo;

public interface MerchandiseExternalWarehouseMongoDao extends MongoDao<MerchandiseExternalWarehouseMongo>{

    public void insertJson(String json);
}
