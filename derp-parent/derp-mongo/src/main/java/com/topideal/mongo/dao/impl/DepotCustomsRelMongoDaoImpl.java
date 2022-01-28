package com.topideal.mongo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.DepotCustomsRelMongoDao;
import com.topideal.mongo.entity.DepotCustomsRelMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

@Repository
public class DepotCustomsRelMongoDaoImpl implements DepotCustomsRelMongoDao{
	//表名
	private  String  collectionName= CollectionEnum.DEPOT_CUSTOMS_REL.getCollectionName();
	@Autowired
    private MongoTemplate mongoTemplate;
	@Override
	public void insert(DepotCustomsRelMongo object) {
		mongoTemplate.insert(object, collectionName);
	}
	@Override
	public DepotCustomsRelMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query,DepotCustomsRelMongo.class,collectionName);
	}
	@Override
	public List<DepotCustomsRelMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<DepotCustomsRelMongo>  result = mongoTemplate.find(query, DepotCustomsRelMongo.class,collectionName);
        return result;
	}
	@Override
	public void update(Map<String, Object> queryParams, Map<String, Object> data) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(queryParams);
        Update update = BaseUtils.parseUpdate(data);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        mongoTemplate.upsert(query, update,DepotCustomsRelMongo.class,collectionName);
		
	}
	@Override
	public void remove(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        mongoTemplate.remove(query,DepotCustomsRelMongo.class,collectionName);
		
	}
	@Override
	public void insertJson(String json) {
		 mongoTemplate.insert(json, collectionName);
	}
}
