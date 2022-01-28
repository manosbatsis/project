package com.topideal.mongo.dao;

import com.topideal.mongo.entity.CommbarcodeMongo;

/**
 * 标准条码
 * @author lian_
 */
public interface CommbarcodeMongoDao extends MongoDao<CommbarcodeMongo>{

	public void insertJson(String json);

	
}
