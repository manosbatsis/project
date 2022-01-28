package com.topideal.mongo.dao;

import java.util.List;
import java.util.Map;

import com.topideal.mongo.entity.AttachmentInfoMongo;

public interface AttachmentInfoMongoDao extends MongoDao<AttachmentInfoMongo>{

	public void insertJson(String json) ;
	
	public List<AttachmentInfoMongo> findAllOrderByDate(Map<String, Object> params);
}
