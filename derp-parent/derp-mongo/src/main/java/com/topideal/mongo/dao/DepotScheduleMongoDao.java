package com.topideal.mongo.dao;

import com.topideal.mongo.entity.DepotScheduleMongo;

/**
 * 仓库附表 dao
 * @author lian_
 *
 */
public interface DepotScheduleMongoDao  extends MongoDao<DepotScheduleMongo>{

	public void insertJson(String json);
}
